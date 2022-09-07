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
import static com.max.restaurant.model.entity.UtilsEntityFields.CUSTOM_USER_ID;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD;

public class CustomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomService.class);
    private CustomDAO customDAO;

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

    public Custom findCustomById(int id) throws DAOException {
        LOGGER.info(METHOD, "findCustomById", "true");
        if (id < 1)
            throw new DAOServiceException(ID_EXC);
        customDAO = new CustomDAO();
        return customDAO.findObjById(id);
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
        Custom custom = getNewCustomByUserId(userId);
        if (custom.getId() > 0){
            //need to get old ones from DB, check for equal with new:
//            if equal - form list for update
//            if not equal - form list for insert
            CustomHasDishService  hasDishService = new CustomHasDishService();
            orderedDishes = hasDishService.insertCustomNum(custom.getId(), orderedDishes);
            Map<String, List<CustomHasDish>> map = hasDishService.sortForInsertUpdate(orderedDishes);
            List<CustomHasDish> toUpdate = map.get("update");
            List<CustomHasDish> toInsert = map.get("insert");
        }
        return null;
    }
    public Custom getNewCustomByUserId(int userId) throws DAOException {
        Custom custom = null;
        int i = 0;
        List<Custom> customList = findCustomsByUserId(userId);
        for (Custom customTemp : customList) {
            if (customTemp.getStatusId() == 1) {
                custom = customTemp;
                i++;
            }
        }
        if (i > 1) throw new DAOException(CUSTOM_DAO_EXC);
        if (custom == null) {
            Timestamp creationDate = Timestamp.valueOf(LocalDateTime.now() );
            custom = new Custom(creationDate, userId, 1);
            //insertCustom(custom);
            //custom = getNewCustomByUserId(userId);;
        }
        return custom;
    }

    private void updateCustomTransaction(Custom custom, Map<String, List<CustomHasDish>> map) throws DAOException {
        customDAO = new CustomDAO();
        List<CustomHasDish> toUpdate = map.get("update");
        List<CustomHasDish> toInsert = map.get("insert");
        Connection conn = customDAO.getConnection();
        try {
            conn.setAutoCommit(false);
            CustomHasDishDAO hasDishDAO = new CustomHasDishDAO();
            int totalCost = 0;
            for (CustomHasDish c : toUpdate ){
                totalCost += c.getCount() * c.getPrice();
                hasDishDAO.updateObj(c, conn);
            }
            for (CustomHasDish c : toInsert ){
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
        }

    }


    private List<Custom> findCustomsByUserId(int userId) throws DAOException {
        LOGGER.info(METHOD, "findCustomByUserId", "true");
        if (userId < 1)
            throw new DAOServiceException(ID_EXC);
        customDAO = new CustomDAO();
        List<Custom> list = customDAO.findObjByParam(CUSTOM_USER_ID, String.valueOf(userId));
        return list;
    }

//                if (!conn.getAutoCommit()) {
//        conn.setAutoCommit(true);
//        LOGGER.debug(TWO_PARAMS_MSG, "setAutoCommit", conn.getAutoCommit());
//    }

    public void saveCustom(Custom custom) throws DAOException {
        LOGGER.info(METHOD, "saveCustom", "true");
        if (customIsValid(custom)){
            customDAO = new CustomDAO();
            customDAO.updateObj(custom, customDAO.getConnection());
        }
        else throw new DAOServiceException(CUSTOM_VALID_EXC);
    }

    private void insertCustom(Custom custom) throws DAOException {
        LOGGER.info(METHOD, "insertCustom", "true");
        customDAO = new CustomDAO();
        if (!customIsValid(custom) && !customDAO.insertObj(custom, customDAO.getConnection()) )
            throw new DAOServiceException(USER_EXC);
    }

    private boolean customIsValid(Custom custom) {
        return (custom.getId() >= 0 && custom.getCreateTime() != null &&
                custom.getUserId() > 0 && custom.getCost() > 0);
    }
}
