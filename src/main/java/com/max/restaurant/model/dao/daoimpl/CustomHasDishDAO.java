package com.max.restaurant.model.dao.daoimpl;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.CustomHasDish;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.max.restaurant.model.entity.UtilsEntityFields.*;

public class CustomHasDishDAO extends AbstractDAOSimpleEntity<CustomHasDish> {
    private static final String genericName = CustomHasDish.class.getSimpleName();

    public CustomHasDishDAO() throws DAOException {
        super(genericName);
    }

    @Override
    protected CustomHasDish getEntityFromResult(ResultSet result) throws SQLException {
        CustomHasDish obj = new CustomHasDish();
        obj.setId(result.getInt(CUSTOMHASDISH_C_ID) );
        obj.setDishId(result.getInt(CUSTOMHASDISH_D_ID) );
        obj.setCount(result.getInt(CUSTOMHASDISH_COUNT) );
        obj.setPrice(result.getDouble(CUSTOMHASDISH_PRICE));
        return obj;
    }

    /**
     * params = "?, ?, ?, ?"
     * */
    @Override
    protected void setStatementParams(CustomHasDish obj, PreparedStatement statement) throws SQLException {
        statement.setInt(1, obj.getCustomId());
        statement.setInt(2, obj.getDishId());
        statement.setInt(3, obj.getCount());
        statement.setDouble(4, obj.getPrice());
    }
}
