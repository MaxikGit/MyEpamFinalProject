package com.max.restaurant.model;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.dao.daoimpl.UserDAO;
import com.max.restaurant.model.dao.MyDBConnection;
import com.max.restaurant.model.entity.Category;
import com.max.restaurant.model.entity.Dish;
import com.max.restaurant.model.entity.User;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Model {
    private static volatile Model instance;
    private List<User> users = new ArrayList<>();
    private List<Dish> faculties = new ArrayList<>();
    private List<Category> subjects = new ArrayList<>();

    public static Model getInstance() {
        if (instance != null)
            return instance;

        synchronized (MyDBConnection.class) {
            instance = new Model();
        }
        return instance;
    }

    private Model() {}

    public boolean add(User user) {
        return users.add(user);
    }

    public List<String> list() throws SQLException, NamingException, DAOException {
        users = new UserDAO().findAll();
        return users.stream()
                .map(User::toString)
                .collect(Collectors.toList());
    }
}
