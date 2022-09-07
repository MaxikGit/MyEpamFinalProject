package com.max.restaurant.model.dao.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.DishDAO;
import com.max.restaurant.model.entity.Dish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.max.restaurant.exceptions.UtilsExceptionMsgs.ID_EXC;
import static com.max.restaurant.exceptions.UtilsExceptionMsgs.USER_EXC;
import static com.max.restaurant.model.entity.UtilsEntityFields.DISH_CATEGORY_ID;
import static com.max.restaurant.model.entity.UtilsEntityFields.DISH_NAME;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

public class DishService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DishService.class);
    private DishDAO dishDAO;

    public Dish findDishByName(String name) throws DAOException {
        LOGGER.info(METHOD, "findDishByName", "true");
        dishDAO = new DishDAO();
        List<Dish> list = dishDAO.findObjByParam(DISH_NAME, name);
        LOGGER.debug(FIND_BY_PARAM, DISH_NAME, list);
        return list.size() > 0 ? list.get(0) : null;
    }
    public List<Dish> findDishByCategoryId(String name) throws DAOException {
        LOGGER.info(METHOD, "findDishByCategoryId", "true");
        dishDAO = new DishDAO();
        List<Dish> list = dishDAO.findObjByParam(DISH_CATEGORY_ID, name);
        LOGGER.debug(FIND_BY_PARAM, DISH_CATEGORY_ID, list);
        return list;
    }

    public Dish findDishById(int id) throws DAOException {
        LOGGER.info(METHOD, "findDishById", "true");
        if (id < 1)
            throw new DAOServiceException(ID_EXC);
        dishDAO = new DishDAO();
        Dish dish = dishDAO.findObjById(id);
        LOGGER.debug(FIND_BY_ID, dish);
        return dish;
    }

    public List<Dish> findAllDishes() throws DAOException {
        LOGGER.info(METHOD, "findAllDishes", "true");
        dishDAO = new DishDAO();
        return dishDAO.findAll();
    }

    public void deleteDish(Dish dish) throws DAOException {
        LOGGER.info(METHOD, "deleteDish", "true");
        dishDAO = new DishDAO();
        if (!dishIsValid(dish) && !dishDAO.deleteObj(dish))
            throw new DAOServiceException(USER_EXC);
    }

    public void updateDish(Dish dish) throws DAOException {
        LOGGER.info(METHOD, "updateDish", "true");
        dishDAO = new DishDAO();
        if (!dishIsValid(dish) && !dishDAO.updateObj(dish, dishDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);
    }
    public void insertDish(Dish dish) throws DAOException {
        LOGGER.info(METHOD, "insertDish", "true");
        dishDAO = new DishDAO();
        if (!dishIsValid(dish) && !dishDAO.insertObj(dish, dishDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);
    }

    private boolean dishIsValid(Dish dish) {
        return (dish.getName() != null && (dish.getId() >= 0 ) &&
                dish.getPrice() > 0 && dish.getCategoryId() > 0);
    }
}
