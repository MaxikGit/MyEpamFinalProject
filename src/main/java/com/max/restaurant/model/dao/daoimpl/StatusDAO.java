package com.max.restaurant.model.dao.daoimpl;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.Status;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.max.restaurant.model.entity.UtilsEntityFields.*;

public class StatusDAO extends AbstractDAOSimpleEntity<Status> {
    private static final String genericName = Status.class.getSimpleName();

    public StatusDAO() throws DAOException {
        super(genericName);
    }

    @Override
    protected Status getEntityFromResult(ResultSet result) throws SQLException {
        Status obj = new Status();
        obj.setId(result.getInt(STATUS_ID));
        obj.setName(result.getString(STATUS_NAME));
        obj.setDetails(result.getString(STATUS_DETAILS));
        return obj;
    }

    @Override
    protected void setStatementParams(Status obj, PreparedStatement statement, boolean update) throws SQLException {
        statement.setString(1, obj.getName());
        if (update)
            statement.setInt(2, obj.getId());

        if (obj.getDetails() != null && !obj.getDetails().equals("none"))
            statement.setString(3, obj.getDetails());
        else
            statement.setString(3, "none");
    }
}
