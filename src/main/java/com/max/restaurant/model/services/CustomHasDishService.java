package com.max.restaurant.model.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.CustomDAO;
import com.max.restaurant.model.dao.daoimpl.CustomHasDishDAO;
import com.max.restaurant.model.entity.Category;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.CustomHasDish;
import com.max.restaurant.model.entity.Dish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.max.restaurant.utils.UtilsExceptionMsgs.*;
import static com.max.restaurant.model.entity.UtilsEntityFields.CUSTOMHASDISH_C_ID;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * This is a service class, that presents methods to operate over {@link CustomHasDish} objects on requests
 * from controller to DAO layer.
 */
public class CustomHasDishService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomHasDishService.class);
    private CustomHasDishDAO customHasDishDAO;

    public List<CustomHasDish> findByCustomId(int customId) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findByCustomId", "true");
        if (customId < 1)
            throw new DAOServiceException(ID_EXC);
        customHasDishDAO = new CustomHasDishDAO();
        List<CustomHasDish> list = customHasDishDAO
                .findObjByParam(CUSTOMHASDISH_C_ID, String.valueOf(customId), customHasDishDAO.getConnection());
        return list;
    }

    public CustomHasDish findByCustomIdDishId(int customId, int dishId) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findByCustomIdDishId", "true");
        if (customId < 1 || dishId < 1)
            throw new DAOServiceException(ID_EXC);
        customHasDishDAO = new CustomHasDishDAO();
        List<CustomHasDish> list = findByCustomId(customId);
        for (CustomHasDish hasDish : list) {
            if (hasDish.getDishId() == dishId)
                return hasDish;
        }
        return null;
    }

    public List<CustomHasDish> findAll() throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findAllCategories", "true");
        customHasDishDAO = new CustomHasDishDAO();
        return customHasDishDAO.findAll();
    }

    /**
     * This method delete dishes from existing order (custom) and updates custom cost
     * It can't  delete the last position in order
     */
    public boolean deleteTransactional(CustomHasDish customHasDish) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "deleteCustomHasDish", "true");
        if (!customHasDishIsValid(customHasDish))
            throw new DAOServiceException(CUSTOM_HAS_DISH_EXC + customHasDish);
        CustomDAO customDAO = new CustomDAO();
        Custom custom = customDAO.findObjById(customHasDish.getCustomId());
        customHasDishDAO = new CustomHasDishDAO();
        Connection conn = customHasDishDAO.getConnection();
        try {
            conn.setAutoCommit(false);
            List<CustomHasDish> hasDishList = findByCustomId(custom.getId());
            if (hasDishList.size() == 1) {
                return false;
            }
            customHasDishDAO.deleteObj(customHasDish, conn);
            hasDishList.remove(customHasDish);
            double total = hasDishList.stream().mapToDouble(t -> t.getPrice() * t.getCount()).sum();
            custom.setCost(total);
            customDAO.updateObj(custom, conn);
            conn.commit();
            return true;
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

    public void update(CustomHasDish customHasDish) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "updateCustomHasDish", "true");
        customHasDishDAO = new CustomHasDishDAO();
        if (!customHasDishIsValid(customHasDish) && !customHasDishDAO.updateObj(customHasDish, customHasDishDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);

    }

//    public void update(Custom custom, Map<Dish, Integer> orderedDishes) throws DAOException {
//        List<CustomHasDish> hasDishesList = new ArrayList<>();
//        for (Map.Entry<Dish, Integer> entry : orderedDishes.entrySet()) {
//            CustomHasDish customHasDish = findByCustomIdDishId(custom.getId(), entry.getKey().getId());
//            customHasDish.setCount(entry.getValue());
//            hasDishesList.add(customHasDish);
//        }
//        customHasDishDAO = new CustomHasDishDAO();
//        CustomService customService = new CustomService();
//        Connection connection = customHasDishDAO.getConnection();
//        try {
//            connection.setAutoCommit(false);
//
//        } catch (SQLException e) {
//            throw new DAOServiceException(e);
//        }
//    }

    public List<CustomHasDish> createList(int customID, Map<Dish, Integer> orderedDishes) throws DAOException {
        List<CustomHasDish> hasDishesList = new ArrayList<>();
        for (Map.Entry<Dish, Integer> entry : orderedDishes.entrySet()) {
            CustomHasDish customHasDish = new CustomHasDish(customID, entry.getKey().getId());
            customHasDish.setCount(entry.getValue());
            DishService dishService = new DishService();
            double realPrice = (dishService.findDishById(entry.getKey().getId())).getPrice();
            customHasDish.setPrice(realPrice);
            hasDishesList.add(customHasDish);
        }
        return hasDishesList;
    }

    public void insertCustomNum(int id, List<CustomHasDish> orderedDishes) {
        for (CustomHasDish customHasDish : orderedDishes) {
            customHasDish.setCustomId(id);
        }
    }

    public Map<SortToUpdate, List<CustomHasDish>> sortToInsertUpdate(List<CustomHasDish> orderedDishes) throws DAOException {
        List<CustomHasDish> oldies = findByCustomId(orderedDishes.get(0).getCustomId());
        List<CustomHasDish> toUpdate = new ArrayList<>();
        for (CustomHasDish oldOne : oldies) {
            for (CustomHasDish newOne : orderedDishes) {
                if (oldOne.equals(newOne)) {
                    oldOne.setCount(oldOne.getCount() + newOne.getCount());
                    toUpdate.add(oldOne);
                }
            }
        }
        for (CustomHasDish deleteMe : toUpdate) {
            orderedDishes.remove(deleteMe);
        }
        Map<SortToUpdate, List<CustomHasDish>> map = new HashMap<>();
        map.put(SortToUpdate.UPDATE, toUpdate);
        map.put(SortToUpdate.INSERT, orderedDishes);
        return map;
    }

    public enum SortToUpdate {
        UPDATE,
        INSERT
    }

    private boolean customHasDishIsValid(CustomHasDish customHasDish) {
        return (customHasDish.getId() > 0 && customHasDish.getDishId() > 0 &&
                customHasDish.getCount() > 0 && customHasDish.getPrice() > 0);
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
}

/* Not used methods

    public void insertCustomHasDish(CustomHasDish customHasDish) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "insertCustomHasDish", "true");
        customHasDishDAO = new CustomHasDishDAO();
        if (!customHasDishIsValid(customHasDish) && !customHasDishDAO.insertObj(customHasDish, customHasDishDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);
    }

    public List<CustomHasDish> findCustomHasDishByDishId(int dishId) throws DAOException {
        LOGGER.info(METHOD, "findCustomHasDishByUserId", "true");
        if (dishId < 1)
            throw new DAOServiceException(ID_EXC);
        customHasDishDAO = new CustomHasDishDAO();
        List<CustomHasDish> list = customHasDishDAO.findObjByParam(CUSTOMHASDISH_D_ID, String.valueOf(dishId));
        return list;
    }

 */