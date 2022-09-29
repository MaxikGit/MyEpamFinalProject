package com.max.restaurant.model.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.DishDAO;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.CustomHasDish;
import com.max.restaurant.model.entity.Dish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.max.restaurant.exceptions.UtilsExceptionMsgs.*;
import static com.max.restaurant.model.entity.UtilsEntityFields.DISH_CATEGORY_ID;
import static com.max.restaurant.model.entity.UtilsEntityFields.DISH_NAME;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

public class DishService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DishService.class);
    private DishDAO dishDAO;

    public Dish findDishByName(String name) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findDishByName", "true");
        dishDAO = new DishDAO();
        List<Dish> list = dishDAO.findObjByParam(DISH_NAME, name, dishDAO.getConnection());
        LOGGER.debug(FIND_BY_PARAM, DISH_NAME, list);
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<Dish> findDishByCategoryId(String name) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findDishByCategoryId", "true");
        dishDAO = new DishDAO();
        List<Dish> list = dishDAO.findObjByParam(DISH_CATEGORY_ID, name, dishDAO.getConnection());
        LOGGER.debug(FIND_BY_PARAM, DISH_CATEGORY_ID, list);
        return list;
    }

    public Dish findDishById(int id) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findDishById", "true");
        if (id < 1)
            throw new DAOServiceException(ID_EXC);
        dishDAO = new DishDAO();
        Dish dish = dishDAO.findObjById(id);
        LOGGER.debug(FIND_BY_ID, dish);
        return dish;
    }

    public Map<Dish, Integer> findDishesById(List<Integer> ids) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findDishesById", "true");
        if (ids == null || ids.isEmpty())
            throw new DAOServiceException(IDS_EXC);

        Map<Integer,Integer> idsWithQuantity = ids.stream()
                .collect(Collectors
                        .toMap(x -> x, y -> 1, (oldVal, newVal) -> (oldVal + newVal)));

        Map<Dish, Integer> result = new HashMap<>();
        for (Integer id: idsWithQuantity.keySet()){
            Dish dish = findDishById(id);
            result.put(dish, idsWithQuantity.get(id));
        }
        LOGGER.debug(FIND_BY_ID, result.size());
        return result;
    }

    public List<Dish> findAllDishes() throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findAllDishes", "true");
        dishDAO = new DishDAO();
        return dishDAO.findAll();
    }

    public void deleteDish(Dish dish) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "deleteDish", "true");
        dishDAO = new DishDAO();
        if (!dishIsValid(dish) && !dishDAO.deleteObj(dish, dishDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);
    }

    public void updateDish(Dish dish) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "updateDish", "true");
        dishDAO = new DishDAO();
        if (!dishIsValid(dish) && !dishDAO.updateObj(dish, dishDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);
    }
    public void insertDish(Dish dish) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "insertDish", "true");
        dishDAO = new DishDAO();
        if (!dishIsValid(dish) && !dishDAO.insertObj(dish, dishDAO.getConnection()))
            throw new DAOServiceException(USER_EXC);
    }

    public Dish getDishByIdFromMap(Map<Dish, Integer> map, int id) {
        return map.entrySet().stream()
                .filter(dishEntry -> dishEntry.getKey().getId() == id)
                .map(x -> x.getKey()).findFirst().orElse(null);
    }

    public Map<Dish, Integer> getDishesInOrder(Custom custom) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "getDishesInOrder", "true");
        Map<Dish, Integer> result = new HashMap<>();
        DishService dishService = new DishService();
        CustomHasDishService customHasDishService = new CustomHasDishService();
        List<CustomHasDish> customHasDishes = customHasDishService.findByCustomId(custom.getId());
        for (CustomHasDish customHasDish : customHasDishes) {
            result.put(dishService.findDishById(customHasDish.getDishId()), customHasDish.getCount());
        }
        return result;
    }

    private boolean dishIsValid(Dish dish) {
        return (dish.getName() != null && (dish.getId() >= 0 ) &&
                dish.getPrice() > 0 && dish.getCategoryId() > 0);
    }
}
