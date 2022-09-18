package com.max.restaurant.model.dao.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.CustomHasDishDAO;
import com.max.restaurant.model.entity.CustomHasDish;
import com.max.restaurant.model.entity.Dish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.max.restaurant.exceptions.UtilsExceptionMsgs.ID_EXC;
import static com.max.restaurant.exceptions.UtilsExceptionMsgs.USER_EXC;
import static com.max.restaurant.model.entity.UtilsEntityFields.CUSTOMHASDISH_C_ID;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

public class CustomHasDishService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomHasDishService.class);
    private CustomHasDishDAO customHasDishDAO;

//    public List<CustomHasDish> findCustomHasDishByDishId(int dishId) throws DAOException {
//        LOGGER.info(METHOD, "findCustomHasDishByUserId", "true");
//        if (dishId < 1)
//            throw new DAOServiceException(ID_EXC);
//        customHasDishDAO = new CustomHasDishDAO();
//        List<CustomHasDish> list = customHasDishDAO.findObjByParam(CUSTOMHASDISH_D_ID, String.valueOf(dishId));
//        return list;
//    }

    public List<CustomHasDish> findCustomHasDishByCustomId(int customId) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findCustomHasDishByCustomId", "true");
        if (customId < 1)
            throw new DAOServiceException(ID_EXC);
        customHasDishDAO = new CustomHasDishDAO();
        List<CustomHasDish> list = customHasDishDAO
                .findObjByParam(CUSTOMHASDISH_C_ID, String.valueOf(customId), customHasDishDAO.getConnection());
        return list;
    }

    public List<CustomHasDish> findAll() throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findAllCategories", "true");
        customHasDishDAO = new CustomHasDishDAO();
        return customHasDishDAO.findAll();
    }

    public void deleteCustomHasDish(CustomHasDish customHasDish) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "deleteCustomHasDish", "true");
        customHasDishDAO = new CustomHasDishDAO();
        if (!customHasDishIsValid(customHasDish) && !customHasDishDAO.deleteObj(customHasDish))
            throw new DAOServiceException(USER_EXC);
    }

    public void updateCustomHasDish(CustomHasDish customHasDish) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "updateCustomHasDish", "true");
        customHasDishDAO = new CustomHasDishDAO();
        if (!customHasDishIsValid(customHasDish) && !customHasDishDAO.updateObj(customHasDish, customHasDishDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);
    }

    public void insertCustomHasDish(CustomHasDish customHasDish) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "insertCustomHasDish", "true");
        customHasDishDAO = new CustomHasDishDAO();
        if (!customHasDishIsValid(customHasDish) && !customHasDishDAO.insertObj(customHasDish, customHasDishDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);
    }

    private boolean customHasDishIsValid(CustomHasDish customHasDish) {
        return (customHasDish.getId() > 0 && customHasDish.getDishId() > 0 &&
                customHasDish.getCount() > 0 && customHasDish.getPrice() > 0);
    }

    public List<CustomHasDish> fillCustomHasDishesList(int userId, Map<Dish, Integer> orderedDishes) throws DAOException {
        List<CustomHasDish> hasDishesList = new ArrayList<>();
        for (Map.Entry<Dish, Integer> entry : orderedDishes.entrySet()) {
            CustomHasDish customHasDish = new CustomHasDish(userId);
            customHasDish.setDishId(entry.getKey().getId());
            customHasDish.setCount(entry.getValue());
            DishService dishService = new DishService();
            double realPrice = (dishService.findDishById(entry.getKey().getId())).getPrice();
            customHasDish.setPrice(realPrice);
            hasDishesList.add(customHasDish);
        }
        return hasDishesList;
    }

    public void insertCustomNum(int id, List<CustomHasDish> orderedDishes) {
        for (CustomHasDish customHasDish : orderedDishes){
            customHasDish.setCustomId(id);
        }
    }

    public Map<SortToUpdate, List<CustomHasDish>> sortToInsertUpdate(List<CustomHasDish> orderedDishes) throws DAOException {
        List<CustomHasDish> oldies = findCustomHasDishByCustomId(orderedDishes.get(0).getCustomId());
        List<CustomHasDish> toUpdate = new ArrayList<>();
        for (CustomHasDish oldOne : oldies) {
            for (CustomHasDish newOne : orderedDishes) {
                if (oldOne.equals(newOne)) {
                    oldOne.setCount(oldOne.getCount() + newOne.getCount());
                    toUpdate.add(oldOne);
                }
            }
        }
        for (CustomHasDish deleteMe : toUpdate){
            orderedDishes.remove(deleteMe);
        }
        Map<SortToUpdate, List<CustomHasDish>> map = new HashMap<>();
        map.put(SortToUpdate.UPDATE, toUpdate);
        map.put(SortToUpdate.INSERT, orderedDishes);
        return map;
    }

    public enum SortToUpdate{
        UPDATE,
        INSERT
    }
}
