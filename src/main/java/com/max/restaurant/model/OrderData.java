package com.max.restaurant.model;

import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.Dish;
import com.max.restaurant.model.entity.Status;
import com.max.restaurant.model.entity.User;

import java.util.HashMap;
import java.util.Map;

public class OrderData {
    private Custom custom;
    private User users;
    private Map<Dish, Integer> dishes = new HashMap<>();
    private Status status;

    public OrderData(Custom custom) {
        this.custom = custom;
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
