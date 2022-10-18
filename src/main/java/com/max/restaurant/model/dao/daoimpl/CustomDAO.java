package com.max.restaurant.model.dao.daoimpl;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.Custom;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.max.restaurant.utils.UtilsEntityFields.*;

public class CustomDAO extends AbstractDAOSimpleEntity<Custom> {
    private static final String genericName = Custom.class.getSimpleName();

    public CustomDAO() throws DAOException {
        super(genericName);
    }

    @Override
    protected Custom getEntityFromResult(ResultSet result) throws SQLException {
        Custom obj = new Custom();
        obj.setId(result.getInt(CUSTOM_ID) );
        obj.setCost(result.getDouble(CUSTOM_COST) );
        obj.setCreateTime(result.getTimestamp(CUSTOM_TIME) );
        obj.setUserId(result.getInt(CUSTOM_USER_ID));
        obj.setStatusId(result.getInt(CUSTOM_STATUS_ID));
        return obj;
    }

    @Override
    protected void setStatementParams(Custom obj, PreparedStatement statement, boolean update) throws SQLException {
        statement.setBigDecimal(1, BigDecimal.valueOf(obj.getCost()));
        statement.setTimestamp(2, obj.getCreateTime());
        statement.setInt(3, obj.getUserId());
        statement.setInt(4, obj.getStatusId());
        if (update)
            statement.setInt(5, obj.getId());
    }
}
