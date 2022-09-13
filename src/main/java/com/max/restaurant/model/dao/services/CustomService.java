package com.max.restaurant.model.dao.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.CustomDAO;
import com.max.restaurant.model.dao.daoimpl.CustomHasDishDAO;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.CustomHasDish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.max.restaurant.exceptions.UtilsExceptionMsgs.*;
import static com.max.restaurant.model.entity.UtilsEntityFields.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

public class CustomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomService.class);
    private CustomDAO customDAO;
    private String completeStatus = "4";

    public Custom findCustomById(int id) throws DAOException {
        LOGGER.info(METHOD, "findCustomById", "true");
        if (id < 1)
            throw new DAOServiceException(ID_EXC);
        customDAO = new CustomDAO();
        return customDAO.findObjById(id);
    }

    public List<Custom> getCustomsInProgress() throws DAOException {
        LOGGER.info(METHOD, "getCustomsInProgress", "true");
        customDAO = new CustomDAO();
        List<Custom> list = customDAO.findObjByParam(CUSTOM_STATUS_ID + " !", completeStatus, customDAO.getConnection());
        return list;
    }

    public void updateCustom(Custom custom) throws DAOException {
        LOGGER.info(METHOD, "updateCustom", "true");
        if (customIsValid(custom)) {
            customDAO = new CustomDAO();
            customDAO.updateObj(custom, customDAO.getConnection());
        } else throw new DAOServiceException(CUSTOM_VALID_EXC);
    }

    public List<Custom> findAllCustoms() throws DAOException {
        LOGGER.info(METHOD, "findAllCategories", "true");
        customDAO = new CustomDAO();
        return customDAO.findAll();
    }

    public void deleteCustom(Custom custom) throws DAOException {
        LOGGER.info(METHOD, "deleteCustom", "true");
        customDAO = new CustomDAO();
        if (!customIsValid(custom) && !customDAO.deleteObj(custom))
            throw new DAOServiceException(USER_EXC);
    }

    public Custom getNewCustomByUserId(int userId, List<CustomHasDish> orderedDishes) throws DAOException {
        LOGGER.info(METHOD, "getNewCustomByUserId", "true");
        Custom custom = getCustomInstanceByUserId(userId);
        if (custom.getId() > 0) {
            CustomHasDishService hasDishService = new CustomHasDishService();
            hasDishService.insertCustomNum(custom.getId(), orderedDishes);
            Map<String, List<CustomHasDish>> map = hasDishService.sortToInsertUpdate(orderedDishes);
            updateCustomTransaction(custom, map);
            custom = findCustomById(custom.getId());
        } else {
            custom = insertCustomTransaction(custom, orderedDishes);
        }
        return custom;
    }

    private Custom getCustomInstanceByUserId(int userId) throws DAOException {
        LOGGER.info(METHOD, "getCustomInstanceByUserId", "true");
        Custom custom = findNewCustomByUserIdInDB(userId, null);
        if (custom == null) {
            Timestamp creationDate = Timestamp.valueOf(LocalDateTime.now());
            custom = new Custom(creationDate, userId, 1);
        }
        return custom;
    }

    private Custom findNewCustomByUserIdInDB(int userId, Connection conn) throws DAOException {
        LOGGER.info(METHOD, "findCustomByUserId", "true");
        if (userId < 1)
            throw new DAOServiceException(ID_EXC);

        customDAO = new CustomDAO();
        if (conn == null)
            conn = customDAO.getConnection();

        List<Custom> list = customDAO.findObjByParam(CUSTOM_USER_ID, String.valueOf(userId), conn);
        Custom custom = null;
        int i = 0;
        for (Custom customTemp : list) {
            if (customTemp.getStatusId() == 1) {
                custom = customTemp;
                i++;
            }
        }
        if (i > 1) throw new DAOException(CUSTOM_DAO_EXC);
        return custom;
    }

    private void updateCustomTransaction(Custom custom, Map<String, List<CustomHasDish>> map) throws DAOException {
        LOGGER.info(METHOD, "insertCustomTransaction", true);
        customDAO = new CustomDAO();
        List<CustomHasDish> toUpdate = map.get("update");
        List<CustomHasDish> toInsert = map.get("insert");
        Connection conn = customDAO.getConnection();
        try {
            conn.setAutoCommit(false);
            CustomHasDishDAO hasDishDAO = new CustomHasDishDAO();
            double totalCost = 0;
            for (CustomHasDish c : toUpdate) {
                totalCost += c.getCount() * c.getPrice();
            }
            for (CustomHasDish c : toInsert) {
                totalCost += c.getCount() * c.getPrice();
                hasDishDAO.insertObj(c, conn);
            }
            custom.setCost(totalCost);
            customDAO.updateObj(custom, conn);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new DAOException(ex);
            }
            throw new DAOException(e);
        } finally {
            closeConn(conn);
        }
    }

    private Custom insertCustomTransaction(Custom custom, List<CustomHasDish> toInsert) throws DAOException {
        LOGGER.info(METHOD, "insertCustomTransaction", true);
        customDAO = new CustomDAO();
        Connection conn = customDAO.getConnection();
        try {
            conn.setAutoCommit(false);
            int transactionIsolation = conn.getTransactionIsolation();
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            CustomHasDishDAO hasDishDAO = new CustomHasDishDAO();
            customDAO.insertObj(custom, conn);
            custom = findNewCustomByUserIdInDB(custom.getUserId(), conn);
            conn.setTransactionIsolation(transactionIsolation);
            CustomHasDishService hasDishService = new CustomHasDishService();
            hasDishService.insertCustomNum(custom.getId(), toInsert);
            double totalCost = 0;
            for (CustomHasDish c : toInsert) {
                totalCost += c.getCount() * c.getPrice();
                hasDishDAO.insertObj(c, conn);
            }
            custom.setCost(totalCost);
            customDAO.updateObj(custom, conn);
            conn.commit();
            LOGGER.debug(TRANSACTION_MSG);
            return findCustomById(custom.getId());
        } catch (SQLException e) {
            try {
                conn.rollback();
                LOGGER.info(TRANSACTION_ROLLBACK);
            } catch (SQLException ex) {
                LOGGER.error(TRANSACTION_ROLLBACK_FAILED);
                throw new DAOException(ex);
            }
            throw new DAOException(e);
        } finally {
            closeConn(conn);
        }
    }

//    public List<Custom> findCustomsByCreateTime(Timestamp timestamp) throws DAOException {
//        LOGGER.info(METHOD, "findCustomsByCreateTime", "true");
//        customDAO = new CustomDAO();
//        List<Custom> list = customDAO.findObjByParam(CUSTOM_TIME, timestamp.toString());
//        return list;
//    }

//    public int getNewCustomNumber() throws DAOException {
//        customDAO = new CustomDAO();
//        return customDAO.findMaxId();
//    }


    //    private void insertCustom(Custom custom) throws DAOException {
//        LOGGER.info(METHOD, "insertCustom", "true");
//        customDAO = new CustomDAO();
//        if (!customIsValid(custom) && !customDAO.insertObj(custom, customDAO.getConnection()))
//            throw new DAOServiceException(USER_EXC);
//    }
    private void closeConn(Connection conn) throws DAOException {
        if (conn != null) {
            try {
                LOGGER.info(CLOSE_TRANSACTION, true);
//                    conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                LOGGER.error(FAILED_CLOSE_ST_CONN, e);
                throw new DAOException(e);
            }
        }
    }

    private boolean customIsValid(Custom custom) {
        if (custom.getId() > 0 && custom.getCreateTime() != null &&
                custom.getUserId() > 0 ){
            if (custom.getCost() > 0)
                return true;
            else customNeedsToUpdateCost(custom);
        }
        return true;
    }

    private void customNeedsToUpdateCost(Custom custom) {

    }
}
