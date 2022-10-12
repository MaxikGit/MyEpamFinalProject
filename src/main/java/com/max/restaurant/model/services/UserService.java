package com.max.restaurant.model.services;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.dao.daoimpl.UserDAO;
import com.max.restaurant.model.entity.Status;
import com.max.restaurant.model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.max.restaurant.utils.UtilsExceptionMsgs.*;
import static com.max.restaurant.model.entity.UtilsEntityFields.USER_EMAIL;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * This is a service class, that presents methods to operate over {@link User} objects on requests
 * from controller to DAO layer.
 */
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private UserDAO userDAO;

    public User findUserByEmail(String email) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findUserByEmail", "true");
        if (!emailIsValid(email)){
            LOGGER.info(FAILED_FIND_BY_PARAM, "email: " + email);
            return null;
        }
        userDAO = new UserDAO();
        List<User> users = userDAO.findObjByParam(USER_EMAIL, email, userDAO.getConnection());
        return users.size() > 0 ? users.get(0) : null;
    }

    public User findUserById(int id) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findUserById", "true");
        if (id < 1){
            LOGGER.info(FAILED_FIND_BY_ID, id);
            throw new DAOServiceException(ID_EXC);
        }
        userDAO = new UserDAO();
        return userDAO.findObjById(id);
    }

    public List<User> findAllUsers() throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findAllUsers", "true");
        userDAO = new UserDAO();
        return userDAO.findAll();
    }

    public void deleteUser(User user) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "deleteUser", "true");
        userDAO = new UserDAO();
        if (!(userIsValid(user) && userDAO.deleteObj(user, userDAO.getConnection()))){
            LOGGER.info(FAILED_DELETE, user);
            throw new DAOServiceException(USER_EXC);
        }
    }

    public void updateUser(User user) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "updateUser", "true");
        userDAO = new UserDAO();
        if (!(userIsValid(user) && userDAO.updateObj(user, userDAO.getConnection())) ){
            LOGGER.info(FAILED_UPDATE, user);
            throw new DAOServiceException(USER_EXC);
        }
    }
    public void insertUser(User user) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "insertUser", "true");
        userDAO = new UserDAO();
        if ( !(userIsValid(user) && userDAO.insertObj(user, userDAO.getConnection())) ){
            LOGGER.info(FAILED_INSERT, user);
            throw new DAOServiceException(USER_EXC);
        }
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
