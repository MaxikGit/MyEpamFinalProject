package com.max.restaurant.model.dao.daoimpl;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.max.restaurant.utils.UtilsEntityFields.ROLE_ID;
import static com.max.restaurant.utils.UtilsEntityFields.ROLE_NAME;

public class RoleDAO extends AbstractDAOSimpleEntity<Role> {
    private static final String genericName = Role.class.getSimpleName();

    public RoleDAO() throws DAOException {
        super(genericName);
    }

    @Override
    protected Role getEntityFromResult(ResultSet result) throws SQLException {
        Role obj = new Role();
        obj.setId(result.getInt(ROLE_ID));
        obj.setName(result.getString(ROLE_NAME));
        return obj;
    }

    @Override
    protected void setStatementParams(Role role, PreparedStatement statement, boolean update) throws SQLException {
        statement.setString(1, role.getName());
        if (update)
            statement.setInt(2, role.getId());
    }
}
