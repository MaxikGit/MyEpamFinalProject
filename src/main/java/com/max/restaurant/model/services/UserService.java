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

import static com.max.restaurant.utils.UtilsEntityFields.USER_EMAIL;
import static com.max.restaurant.utils.UtilsExceptionMsgs.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * This is a service class, that presents methods to operate over {@link User} objects on requests
 * from controller to DAO layer.
 */
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private UserDAO userDAO = new UserDAO();
    private final int minNameLength = 2;
    private final int minLastNameLength = 1;
    private final int minPassLength = 4;
    private final int maxPassLength = 12;
    private final int maxRoleId = 2;

    public UserService() throws DAOException {
    }

    public User findUserByEmail(String email) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findUserByEmail", "true");
        if (!emailIsValid(email)){
            LOGGER.info(FAILED_FIND_BY_PARAM, "email: " + email);
            return null;
        }
        List<User> users = userDAO.findObjByParam(USER_EMAIL, email, userDAO.getConnection());
        return users.size() > 0 ? users.get(0) : null;
    }

    public User findUserById(int id) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "findUserById", "true");
        if (id < 1){
            LOGGER.info(FAILED_FIND_BY_ID, id);
            throw new DAOServiceException(ID_EXC);
        }
        return userDAO.findObjById(id);
    }

    public void insertUser(User user) throws DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "insertUser", "true");
        boolean isValid = userIsValid(user);
        if ( !(isValid && userDAO.insertObj(user, userDAO.getConnection())) ){
            LOGGER.info(FAILED_INSERT, user);
            throw new DAOServiceException(WRONG_INPUT_EXC);
        }
    }

    private Boolean emailIsValid(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        LOGGER.info(IS_VALID, matcher.matches());
        return matcher.matches();
    }

    /**
     * Checks the fields of <i>user</i> param for the restrictions
     * @param user
     * @return
     */
    private boolean userIsValid(User user) {
        return ( (user.getName() != null && user.getName().length() >= minNameLength)
                && (user.getLastName() != null && user.getLastName().length() >= minLastNameLength)
                && (user.getPassword() != null )
//                && user.getPassword().length() >= minPassLength && user.getPassword().length() <= maxPassLength)
                && (user.getEmail() != null && emailIsValid(user.getEmail()) )
                && (user.getRoleId() > 0 && user.getRoleId() <= maxRoleId) );
    }
}


//    public List<User> findAllUsers() throws DAOException {
//        LOGGER.info(METHOD_STARTS_MSG, "findAllUsers", "true");
//        userDAO = new UserDAO();
//        return userDAO.findAll();
//    }
//
//    public void deleteUser(User user) throws DAOException {
//        LOGGER.info(METHOD_STARTS_MSG, "deleteUser", "true");
//        userDAO = new UserDAO();
//        if (!(userIsValid(user) && userDAO.deleteObj(user, userDAO.getConnection()))){
//            LOGGER.info(FAILED_DELETE, user);
//            throw new DAOServiceException(USER_EXC);
//        }
//    }
//
//    public void updateUser(User user) throws DAOException {
//        LOGGER.info(METHOD_STARTS_MSG, "updateUser", "true");
//        userDAO = new UserDAO();
//        if (!(userIsValid(user) && userDAO.updateObj(user, userDAO.getConnection())) ){
//            LOGGER.info(FAILED_UPDATE, user);
//            throw new DAOServiceException(USER_EXC);
//        }
//    }