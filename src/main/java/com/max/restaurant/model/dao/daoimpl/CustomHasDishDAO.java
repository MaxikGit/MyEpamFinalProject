package com.max.restaurant.model.dao.daoimpl;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.CustomHasDish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.max.restaurant.model.dao.UtilsSQLConstants.getSQLDeleteByParam;
import static com.max.restaurant.model.dao.UtilsSQLConstants.getSQLUpdateEntityById;
import static com.max.restaurant.model.entity.UtilsEntityFields.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

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
    protected void setStatementParams(CustomHasDish obj, PreparedStatement statement, boolean update) throws SQLException {
        statement.setInt(1, obj.getCustomId());
        statement.setInt(2, obj.getDishId());
        statement.setInt(3, obj.getCount());
        statement.setDouble(4, obj.getPrice());
    }

    @Override
    public boolean deleteObj(CustomHasDish entity, Connection conn) throws DAOException {
        Logger LOGGER = LoggerFactory.getLogger(this.getClass());
        LOGGER.info(METHOD_STARTS_MSG, "deleteObj", true);
        PreparedStatement statement = null;
        try {
            String specialParam = String.format("%s=? AND %s", CUSTOMHASDISH_D_ID, CUSTOMHASDISH_C_ID);
            statement = conn.prepareStatement(
                    getSQLDeleteByParam(genericName, specialParam));
            statement.setInt(1, entity.getDishId());
            statement.setInt(2, entity.getCustomId());
            LOGGER.debug(SQL_EXPR_MSG, "deleteObj", genericName, statement);
            int changesMade = statement.executeUpdate();
            return 1 == changesMade;
        } catch (SQLException e) {
            LOGGER.error(FAILED_DELETE, genericName, e);
            throw new DAOException(e);
        } finally {
            closeAll(conn, statement);
        }
    }

    @Override
    public final boolean updateObj(CustomHasDish entity, Connection conn) throws DAOException {
        Logger LOGGER = LoggerFactory.getLogger(this.getClass());
        LOGGER.info(METHOD_STARTS_MSG, "updateObj", true);
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(getSQLUpdateEntityById(genericName));
            //CUSTOMHASDISH_COUNT, CUSTOMHASDISH_PRICE, CUSTOMHASDISH_C_ID, CUSTOMHASDISH_D_ID
            statement.setInt(1, entity.getCount());
            statement.setDouble(2, entity.getPrice());
            statement.setInt(3, entity.getCustomId());
            statement.setInt(4, entity.getDishId());
            LOGGER.debug(SQL_EXPR_MSG, "update", genericName, statement);
            return 1 == statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(FAILED_UPDATE, genericName, e);
            throw new DAOException(e);
        } finally {
            closeAll(conn, statement);
        }
    }
}
