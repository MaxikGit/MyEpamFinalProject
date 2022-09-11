package com.max.restaurant.model.dao.daoimpl;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.max.restaurant.model.entity.UtilsEntityFields.*;

public class UserDAO extends AbstractDAOSimpleEntity<User> {
    //    private final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private static final String genericName = User.class.getSimpleName();

    public UserDAO() throws DAOException {
        super(genericName);
//        super(genericName, LoggerFactory.getLogger(UserDAO.class));
    }

//public User findUserByEmail(String email) throws DAOException {
//        return findObjByParam(USER_EMAIL, email);
//}

    @Override
    protected User getEntityFromResult(ResultSet result) throws SQLException {
        User user = new User();
        user.setId(result.getInt(USER_ID));
        user.setEmail(result.getString(USER_EMAIL));
        user.setName(result.getString(USER_NAME));
        user.setLastName(result.getString(USER_LASTNAME));
        user.setPassword(result.getString(USER_PASSWORD));
        if (result.getString(USER_DETAILS) != null && !result.getString(USER_DETAILS).equals("none"))
            user.setDetails(result.getString(USER_DETAILS));
        user.setRoleId(result.getInt(USER_ROLE_ID));
        return user;
    }

    @Override
    protected void setStatementParams(User user, PreparedStatement statement, boolean update) throws SQLException {
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getName());
        statement.setString(3, user.getLastName());
        statement.setString(4, user.getPassword());
        if (user.getDetails() != null && !user.getDetails().equals("none"))
            statement.setString(5, user.getDetails());
        else
            statement.setString(5, "none");
        statement.setInt(6, user.getRoleId());
        if (update)
            statement.setInt(7, user.getId());
    }

}
