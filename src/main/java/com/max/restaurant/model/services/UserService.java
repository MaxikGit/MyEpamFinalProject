package com.max.restaurant.model.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.UserDAO;
import com.max.restaurant.model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.max.restaurant.exceptions.UtilsExceptionMsgs.*;
import static com.max.restaurant.model.entity.UtilsEntityFields.USER_EMAIL;

public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private UserDAO userDAO;

    public User findUserByEmail(String email) throws DAOException {
        if (!emailIsValid(email))
            throw new DAOServiceException(EMAIL_EXC);
        userDAO = new UserDAO();
        List<User> users = userDAO.findObjByParam(USER_EMAIL, email, userDAO.getConnection());
        return users.size() > 0 ? users.get(0) : null;
    }

    public User findUserById(int id) throws DAOException {
        if (id < 1)
            throw new DAOServiceException(ID_EXC);
        userDAO = new UserDAO();
        return userDAO.findObjById(id);
    }

    public List<User> findAllUsers() throws DAOException {
        userDAO = new UserDAO();
        return userDAO.findAll();
    }

    public void deleteUser(User user) throws DAOException {
        userDAO = new UserDAO();
        if (!(userIsValid(user) && userDAO.deleteObj(user)))
            throw new DAOServiceException(USER_EXC);
    }

    public void updateUser(User user) throws DAOException {
        userDAO = new UserDAO();
        if (!(userIsValid(user) && userDAO.updateObj(user, userDAO.getConnection())) )
            throw new DAOServiceException(USER_EXC);
    }
    public void insertUser(User user) throws DAOException {
        userDAO = new UserDAO();
        if ( !(userIsValid(user) && userDAO.insertObj(user, userDAO.getConnection())) )
            throw new DAOServiceException(USER_EXC);
    }

    private boolean emailIsValid(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean userIsValid(User user) {
        return (user.getName() != null && user.getLastName() != null && user.getPassword() !=null
                && (user.getEmail() != null && emailIsValid(user.getEmail()) )
                && (user.getRoleId() > 0 && user.getRoleId() < 3) );
    }
}
