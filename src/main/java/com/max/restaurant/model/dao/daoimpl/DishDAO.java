package com.max.restaurant.model.dao.daoimpl;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.Dish;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.max.restaurant.model.entity.UtilsEntityFields.*;

public class DishDAO extends AbstractDAOSimpleEntity<Dish> {
    private static final String genericName = Dish.class.getSimpleName();

    public DishDAO() throws DAOException {
        super(genericName);
    }

    @Override
    protected Dish getEntityFromResult(ResultSet result) throws SQLException {
        Dish obj = new Dish();
        obj.setId(result.getInt(DISH_ID) );
        obj.setName(result.getString(DISH_NAME) );
        obj.setPrice(result.getDouble(DISH_PRICE) );
        if (result.getString(DISH_DETAILS) != null && !result.getString(DISH_DETAILS).equals("none"))
            obj.setDetails(result.getString(DISH_DETAILS));
        obj.setCategoryId(result.getInt(DISH_CATEGORY_ID));
        obj.setImagePath(result.getString(DISH_IMAGE_PATH));
        return obj;
    }

    @Override
    protected void setStatementParams(Dish obj, PreparedStatement statement) throws SQLException {
        statement.setString(1, obj.getName());
        statement.setDouble(2, obj.getPrice());
        if (obj.getDetails() != null && !obj.getDetails().equals("none"))
            statement.setString(3, obj.getDetails());
        else
            statement.setString(3, "none");
        statement.setInt(4, obj.getCategoryId());
        statement.setString(5, obj.getImagePath());
    }
}
