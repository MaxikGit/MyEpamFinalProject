package com.max.restaurant.model.dao.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.CustomHasDishDAO;
import com.max.restaurant.model.entity.CustomHasDish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.max.restaurant.exceptions.UtilsExceptionMsgs.ID_EXC;
import static com.max.restaurant.exceptions.UtilsExceptionMsgs.USER_EXC;
import static com.max.restaurant.model.entity.UtilsEntityFields.CUSTOMHASDISH_C_ID;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD;

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
        LOGGER.info(METHOD, "findCustomHasDishByCustomId", "true");
        if (customId < 1)
            throw new DAOServiceException(ID_EXC);
        customHasDishDAO = new CustomHasDishDAO();
        List<CustomHasDish> list = customHasDishDAO.findObjByParam(CUSTOMHASDISH_C_ID, String.valueOf(customId));
        return list;
    }

    public List<CustomHasDish> findAll() throws DAOException {
        LOGGER.info(METHOD, "findAllCategories", "true");
        customHasDishDAO = new CustomHasDishDAO();
        return customHasDishDAO.findAll();
    }

    public void deleteCustomHasDish(CustomHasDish customHasDish) throws DAOException {
        LOGGER.info(METHOD, "deleteCustomHasDish", "true");
        customHasDishDAO = new CustomHasDishDAO();
        if (!customHasDishIsValid(customHasDish) && !customHasDishDAO.deleteObj(customHasDish))
            throw new DAOServiceException(USER_EXC);
    }

    public void updateCustomHasDish(CustomHasDish customHasDish) throws DAOException {
        LOGGER.info(METHOD, "updateCustomHasDish", "true");
        customHasDishDAO = new CustomHasDishDAO();
        if (!customHasDishIsValid(customHasDish) && !customHasDishDAO.updateObj(customHasDish, customHasDishDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);
    }

    public void insertCustomHasDish(CustomHasDish customHasDish) throws DAOException {
        LOGGER.info(METHOD, "insertCustomHasDish", "true");
        customHasDishDAO = new CustomHasDishDAO();
        if (!customHasDishIsValid(customHasDish) && !customHasDishDAO.insertObj(customHasDish, customHasDishDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);
    }

    private boolean customHasDishIsValid(CustomHasDish customHasDish) {
        return (customHasDish.getId() > 0 && customHasDish.getDishId() > 0 &&
                customHasDish.getCount() > 0 && customHasDish.getPrice() > 0);
    }

//    public void saveAll(List<CustomHasDish> customHasDishList) throws DAOException {
//        for (CustomHasDish customHasDish : customHasDishList){
//            customHasDishDAO = new CustomHasDishDAO();
//        }
//
//    }

    public List<CustomHasDish> insertCustomNum(int id, List<CustomHasDish> orderedDishes) {
        for (CustomHasDish customHasDish : orderedDishes)
            customHasDish.setCustomId(id);
        return orderedDishes;
    }

    public Map<String, List<CustomHasDish>> sortForInsertUpdate(List<CustomHasDish> orderedDishes) throws DAOException {
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
        Map<String, List<CustomHasDish>> map = new HashMap<>();
        map.put("update", toUpdate);
        map.put("insert", orderedDishes);
        return map;
    }
}
