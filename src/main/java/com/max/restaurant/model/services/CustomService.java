package com.max.restaurant.model.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.CustomDAO;
import com.max.restaurant.model.dao.daoimpl.CustomHasDishDAO;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.CustomHasDish;
import com.max.restaurant.model.entity.Dish;
import com.max.restaurant.model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.max.restaurant.utils.UtilsEntityFields.*;
import static com.max.restaurant.utils.UtilsExceptionMsgs.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * This is a service class, that presents methods to operate over {@link Custom} objects on requests
 * from controller to DAO layer.
 */
public class CustomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomService.class);
    private final CustomDAO customDAO = new CustomDAO();
    private final String completeStatus = "4";
    private final int newStatus = 1;

    public CustomService() throws DAOException {
    }

    public Custom findCustomById(int id) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findCustomById", "true");
        if (id < 1)
            throw new DAOServiceException(ID_EXC);
        return customDAO.findObjById(id);
    }

    public void update(Custom custom) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "updateCustom", "true");
        if (customIsValid(custom)) {
            customDAO.updateObj(custom, customDAO.getConnection());
        } else throw new DAOServiceException(CUSTOM_VALID_EXC);
    }

    public void deleteCustom(Custom custom) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "deleteCustom", "true");
        if (!customIsValid(custom))
            throw new DAOServiceException(CUSTOM_VALID_EXC);
        LOGGER.debug(TWO_PARAMS_MSG, "custom with id", custom.getId());
        customDAO.deleteObj(custom, customDAO.getConnection());
    }

    public void deleteCustom(int id) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "deleteCustom", "true");
        if (id < 1)
            throw new DAOServiceException(ID_EXC);
        LOGGER.debug(TWO_PARAMS_MSG, "id", id);
        Custom custom = customDAO.findObjById(id);
        deleteCustom(custom);
    }

    /**
     * returns all, not completed customs from database
     *
     * @return returns {@link List} of all not completed customs
     * @throws DAOException when have problems on DAO layer
     */
    public List<Custom> getCustomsInProgress() throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "getCustomsInProgress", "true");
        List<Custom> list = customDAO.findObjByParam(CUSTOM_STATUS_ID + " !", completeStatus, customDAO.getConnection());
        return list;
    }

    /**
     * returns not completed customs from database for User with id = userId
     * The result of this method is equal to next SQL expression:<br>
     * <strong>{@code SELECT * FROM customs WHERE user_id=userId AND status_id!=}{@link #completeStatus}</strong>
     *
     * @param userId the result of {@link User#getId()}
     * @return returns {@link List} of all not completed customs
     * @throws DAOException when have problems on DAO layer
     */
    public List<Custom> getUsersCustomsInProgress(int userId) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "getCustomsInProgress", "true");
        String specialParamName = String.format("%s=%d AND %s", CUSTOM_USER_ID, userId, CUSTOM_STATUS_ID + " !");
        List<Custom> list = customDAO.findObjByParam(specialParamName, completeStatus, customDAO.getConnection());
        return list;
    }

    /**
     * returns all completed customs from database
     *
     * @return returns {@link List} of all completed customs
     * @throws DAOException when have problems on DAO layer
     */
    public List<Custom> getCustomsCompleted() throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "getCustomsCompleted", "true");
        List<Custom> list = customDAO.findObjByParam(CUSTOM_STATUS_ID, completeStatus, customDAO.getConnection());
        return list;
    }

    /**
     * updates custom in database, changing some of it`s parameters (transactional operation)
     *
     * @param custom        custom to update
     * @param orderedDishes pairs of {@link Dish} objects & their quantities
     * @throws DAOException when have problems on DAO layer
     */
    public void updateTransaction(Custom custom, Map<Dish, Integer> orderedDishes) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "updateTransaction", "true");
        if (customIsValid(custom)) {
            CustomHasDishService hasDishService = new CustomHasDishService();
            List<CustomHasDish> hasDishList = hasDishService.createList(custom.getId(), orderedDishes);
            Connection connection = customDAO.getConnection();
            try {
                connection.setAutoCommit(false);
                CustomHasDishDAO hasDishDAO = new CustomHasDishDAO();
                double totalCost = 0;
                for (CustomHasDish customHasDish : hasDishList) {
                    hasDishDAO.updateObj(customHasDish, connection);
                    totalCost += customHasDish.getPrice() * customHasDish.getCount();
                }
                custom.setCost(totalCost);
                customDAO.updateObj(custom, connection);
                connection.commit();
                LOGGER.debug(TRANSACTION_MSG, "updateTransaction");
            } catch (SQLException e) {
                try {
                    connection.rollback();
                    LOGGER.info(TRANSACTION_ROLLBACK);
                } catch (SQLException ex) {
                    LOGGER.error(TRANSACTION_ROLLBACK_FAILED);
                    throw new DAOServiceException(ex);
                }
                throw new DAOServiceException(e);
            } finally {
                closeConn(connection);
            }

        } else throw new DAOServiceException(CUSTOM_VALID_EXC);
    }

    /**
     * returns new custom, or a custom from database with status id = 1, for {@link User} with id = <strong>userId</strong>.
     * If it returns custom from database, it will combine its map of dishes with one from <strong>orderedDishes</strong> param.<br>
     * In any case, custom will be registered in database <strong>(transactional operation)</strong>
     *
     * @param userId        id of user object
     * @param orderedDishes pairs of {@link Dish} objects & their quantities
     * @return new custom, or a custom with status id = 1
     * @throws DAOException
     */
    public Custom getNewCustom(int userId, Map<Dish, Integer> orderedDishes) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "getNewCustomByUserId", "true");
        List<Custom> customs = findByUserAndStatus(userId, newStatus, null);
        Custom custom;
        if (customs.size() == 0) {
            custom = createNewCustom(userId);
        } else if (customs.size() == 1) {
            custom = customs.get(0);
        } else {
            LOGGER.error(FAILED_FIND_BY_ID, CUSTOM_DAO_EXC);
            throw new DAOException(CUSTOM_DAO_EXC);
        }

        CustomHasDishService hasDishService = new CustomHasDishService();
        List<CustomHasDish> hasDishesList = hasDishService.createList(custom.getId(), orderedDishes);
        if (custom.getId() > 0) {
            Map<CustomHasDishService.SortToUpdate, List<CustomHasDish>> map = hasDishService.sortToInsertUpdate(hasDishesList);
            combineCustomsTransaction(custom, map);
            custom = findCustomById(custom.getId());
        } else {
            custom = insertCustomTransaction(custom, hasDishesList);
        }
        return custom;
    }

    private Custom createNewCustom(int userId) {
        LOGGER.info(METHOD_STARTS_MSG, "createNewCustom", "true");
        Timestamp creationDate = Timestamp.valueOf(LocalDateTime.now());
        return new Custom(creationDate, userId, 1);
    }

    private List<Custom> findByUserAndStatus(int userId, int statusId, Connection conn) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findByUserAndStatus", "true");
        if (userId < 1 || statusId < 1) {
            LOGGER.error(FAILED_FIND_BY_PARAM, "userId=" + userId + "statusId=" + statusId);
            throw new DAOServiceException(ID_EXC);
        }
        if (conn == null) {
            conn = customDAO.getConnection();
        }
        String specialParamName = String.format("%s=%d AND %s", CUSTOM_USER_ID, userId, CUSTOM_STATUS_ID);
        List<Custom> list = customDAO.findObjByParam(specialParamName, String.valueOf(statusId), conn);
        return list;
    }

    private void combineCustomsTransaction(Custom custom, Map<CustomHasDishService.SortToUpdate, List<CustomHasDish>> map) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "combineCustomsTransaction", true);
        List<CustomHasDish> toUpdate = map.get(CustomHasDishService.SortToUpdate.UPDATE);
        List<CustomHasDish> toInsert = map.get(CustomHasDishService.SortToUpdate.INSERT);
        Connection conn = customDAO.getConnection();
        try {
            conn.setAutoCommit(false);
            CustomHasDishDAO hasDishDAO = new CustomHasDishDAO();
            double totalCost = 0;
            for (CustomHasDish c : toUpdate) {
                totalCost += c.getCount() * c.getPrice();
                hasDishDAO.updateObj(c, conn);
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
                throw new DAOServiceException(ex);
            }
            throw new DAOException(e);
        } finally {
            closeConn(conn);
        }
    }

    private Custom insertCustomTransaction(Custom custom, List<CustomHasDish> toInsert) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "insertCustomTransaction", true);
        Connection conn = customDAO.getConnection();
        try {
            conn.setAutoCommit(false);
            int transactionIsolation = conn.getTransactionIsolation();
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            CustomHasDishDAO hasDishDAO = new CustomHasDishDAO();
            customDAO.insertObj(custom, conn);
            List<Custom> customs = findByUserAndStatus(custom.getUserId(), newStatus, conn);
            if (customs.size() > 1) {
                LOGGER.error(FAILED_FIND_BY_ID, CUSTOM_DAO_EXC);
                throw new DAOException(CUSTOM_DAO_EXC);
            }
            custom = customs.get(0);
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

    private boolean customIsValid(Custom custom) throws DAOException {
        if (custom.getId() > 0 && custom.getCreateTime() != null
                && custom.getUserId() > 0) {
            if (custom.getCost() > 0)
                return true;
            else customNeedsToUpdateCost(custom);
        }
        return false;
    }

    /*
     * This method updates initial custom cost = 0. This case happens when an order is placed
     * directly in database without calculating, 'by hands'.
     */
    private void customNeedsToUpdateCost(Custom custom) throws DAOException {
        CustomHasDishService customHasDishService = new CustomHasDishService();
        List<CustomHasDish> hasDishesList = customHasDishService.findByCustomId(custom.getId());
        double totalCost = 0;
        for (CustomHasDish hasDish : hasDishesList) {
            totalCost += hasDish.getPrice() * hasDish.getCount();
        }
        custom.setCost(totalCost);
        customDAO.updateObj(custom, customDAO.getConnection());
    }
}

/* Not used methods

    public List<Custom> findCustomsByCreateTime(Timestamp timestamp) throws DAOException {
        LOGGER.info(METHOD, "findCustomsByCreateTime", "true");
        customDAO = new CustomDAO();
        List<Custom> list = customDAO.findObjByParam(CUSTOM_TIME, timestamp.toString());
        return list;
    }

    public int getNewCustomNumber() throws DAOException {
        customDAO = new CustomDAO();
        return customDAO.findMaxId();
    }


        private void insertCustom(Custom custom) throws DAOException {
        LOGGER.info(METHOD, "insertCustom", "true");
        customDAO = new CustomDAO();
        if (!customIsValid(custom) && !customDAO.insertObj(custom, customDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);
    }


    public List<Custom> findAllCustoms() throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findAllCategories", "true");
        customDAO = new CustomDAO();
        return customDAO.findAll();
    }
        private Custom findNewCustomByUserIdInDB(int userId, Connection conn) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findCustomByUserId", "true");
        if (userId < 1)
            throw new DAOServiceException(ID_EXC);
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

*/
