package com.max.restaurant.model;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.Dish;
import com.max.restaurant.model.entity.Status;
import com.max.restaurant.model.entity.User;
import com.max.restaurant.model.services.CustomService;
import com.max.restaurant.model.services.DishService;
import com.max.restaurant.model.services.StatusService;
import com.max.restaurant.model.services.UserService;

import java.io.Serializable;
import java.util.*;

/**
 * This is a data transfer class, used to transfer special kit of
 * {@link com.max.restaurant.model.entity.SimpleEntity} objects to the view layer
 */
public class OrderData implements Serializable {
    private Custom custom;
    private User users;
    private Map<Dish, Integer> dishes = new HashMap<>();
    private Status status;

    public OrderData(Custom custom) throws DAOException {
        this.custom = custom;
        initialize();
    }

    private void initialize() throws DAOException {
        StatusService statusService = new StatusService();
        UserService userService = new UserService();
        DishService dishService = new DishService();

        setStatus(statusService.findStatusById(custom.getStatusId()));
        setUser(userService.findUserById(custom.getUserId()));
        setDishes(dishService.getDishesInOrder(custom));
    }

    public static List<OrderData> getOrderDataList(List<Custom> customs) throws DAOException {
        List<OrderData> ordersList = new ArrayList<>();
        for (Custom custom : customs){
            ordersList.add(new OrderData(custom));
        }
        Collections.sort(ordersList, Comparator.comparing(x -> x.getStatus().getId()));
        return ordersList;
    }

    public Custom getCustom() {
        return custom;
    }

    public void setCustom(Custom custom) {
        this.custom = custom;
    }

    public User getUser() {
        return users;
    }

    public void setUser(User users) {
        this.users = users;
    }

    public Map<Dish, Integer> getDishes() {
        return dishes;
    }

    public void setDishes(Map<Dish, Integer> dishes) {
        this.dishes = dishes;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
