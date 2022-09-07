package com.max.restaurant.model.dao.daoimpl;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.max.restaurant.model.entity.UtilsEntityFields.*;

public class CategoryDAO extends AbstractDAOSimpleEntity<Category> {
    private static final String genericName = Category.class.getSimpleName();

    public CategoryDAO() throws DAOException {
        super(genericName);
    }

    @Override
    protected Category getEntityFromResult(ResultSet result) throws SQLException {
        Category obj = new Category();
        obj.setId(result.getInt(CATEGORY_ID) );
        obj.setName(result.getString(CATEGORY_NAME) );
        return obj;
    }

    @Override
    protected void setStatementParams(Category obj, PreparedStatement statement) throws SQLException {
        statement.setString(1, obj.getName());
//        statement.setInt(2, obj.getId());
    }
}
