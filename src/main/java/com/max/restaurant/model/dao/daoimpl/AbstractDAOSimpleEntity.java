package com.max.restaurant.model.dao.daoimpl;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.dao.DAOSimpleEntity;
import com.max.restaurant.model.dao.MyDBConnection;
import com.max.restaurant.model.entity.SimpleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.max.restaurant.utils.UtilsEntityFields.USER_ID;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;
import static com.max.restaurant.utils.UtilsSQLConstants.*;

/**
 * This class provides a skeletal implementation of the DAOSimpleEntity interface to minimize the effort required
 * to implement this interface. Basically, subclass needs to implement two methods:
 * {@link #getEntityFromResult(ResultSet)}, {@link #setStatementParams(SimpleEntity, PreparedStatement, boolean)}
 *
 * @param <T> the object type of {@link SimpleEntity} interface
 */
public abstract class AbstractDAOSimpleEntity<T extends SimpleEntity> implements DAOSimpleEntity<T> {
    //    private MyDBConnection myDbConnection;
    private final Logger LOGGER;
    private final String genericName;

    /**
     * The main constructor used by subclasses
     * @param genericSimpleName used to achieve SQL expression for the concrete entity.
     *                          Its value is equal to {@code Class.getSimpleName()}
     */
    public AbstractDAOSimpleEntity(String genericSimpleName){
        this.LOGGER = LoggerFactory.getLogger(this.getClass());
        this.genericName = genericSimpleName;
        LOGGER.info(CONSTRUCTOR);
    }

    public Connection getConnection() throws DAOException {
        try {
            LOGGER.info(METHOD_STARTS_MSG, "getConnection()", true);
            MyDBConnection myDbConnection = MyDBConnection.getInstance();
            return myDbConnection.getConnection();
        } catch (SQLException | NamingException e) {
            LOGGER.error(METHOD_FAILED, "getConnection()", e);
            throw new DAOException(e);
        }
    }

    @Override
    public T findObjById(int id) throws DAOException {
        LOGGER.info(genericName + FIND_BY_ID, id);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        T entity = null;
        try {
            conn = getConnection();
            statement = conn.prepareStatement(getSQLFindByParam(genericName, USER_ID));
            statement.setInt(1, id);
            result = statement.executeQuery();
            if (result.next()) {
                entity = getAbstractObjFromResult(result);
            }
        } catch (SQLException e) {
            LOGGER.error(FAILED_FIND_BY_ID, genericName, e);
            throw new DAOException(e);
        } finally {
            closeAll(conn, statement, result);
        }
        return entity;
    }

    @Override
    public List<T> findObjByParam(String paramName, String param, Connection conn) throws DAOException {
        LOGGER.info(genericName + FIND_BY_PARAM, paramName, param);
        PreparedStatement statement = null;
        ResultSet result = null;
        List<T> entities = new ArrayList<>();
        try {
            String sql = getSQLFindByParam(genericName, paramName);
            LOGGER.info(genericName + FIND_BY_PARAM, paramName, param);
            statement = conn.prepareStatement(sql);
            statement.setString(1, param);
            LOGGER.info(SQL_EXPR_MSG, "find", genericName, statement);
            result = statement.executeQuery();
            while (result.next()) {
                T entity = getAbstractObjFromResult(result);
                entities.add(entity);
            }
        } catch (SQLException e) {
            LOGGER.error(FAILED_FIND_BY_PARAM, "param", e);
            throw new DAOException(e);
        } finally {
            closeAll(conn, statement, result);
        }
        return entities;
    }


    @Override
    public List<T> findAll() throws DAOException {
        LOGGER.info(FIND_ALL, genericName);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        List<T> entities = new ArrayList<>();
        try {
            conn = getConnection();
            statement = conn.prepareStatement(getSQLFindAll(genericName));
            result = statement.executeQuery();
            while (result.next()) {
                T entity = getAbstractObjFromResult(result);
                entities.add(entity);
            }
            return entities;
        } catch (SQLException e) {
            LOGGER.error(FAILED_FIND_ALL, genericName, e);
            throw new DAOException(e);
        } finally {
            closeAll(conn, statement, result);
        }
    }

    @Override
    public boolean insertObj(T entity, Connection conn) throws DAOException {
        LOGGER.info(INSERT_MSG, genericName);
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(getSqlInsertEntity(genericName));
            setAbstractStatementParams(entity, statement, false);
            return 1 == statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(FAILED_INSERT, genericName, e);
            throw new DAOException(e);
        } finally {
            closeAll(conn, statement);
        }
    }

    @Override
    public boolean updateObj(T entity, Connection conn) throws DAOException {
        LOGGER.info(UPDATE_MSG, genericName);
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(getSQLUpdateEntityById(genericName));
            setAbstractStatementParams(entity, statement, true);
            LOGGER.debug(SQL_EXPR_MSG, "update", genericName, statement);
            return 1 == statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(FAILED_UPDATE, genericName, e);
            throw new DAOException(e);
        } finally {
            closeAll(conn, statement);
        }
    }

    @Override
    public boolean deleteObj(T entity, Connection conn) throws DAOException {
        LOGGER.info(DELETE_MSG, genericName);
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(getSQLDeleteByParam(genericName, USER_ID));
            statement.setInt(1, entity.getId());
            return 1 == statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(FAILED_DELETE, genericName, e);
            throw new DAOException(e);
        } finally {
            closeAll(conn, statement);
        }
    }

    @Override
    public int getNumOfRecords() throws DAOException {
        LOGGER.info(DELETE_MSG, genericName);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = getConnection();
            statement = conn.prepareStatement(getSqlNumberOfRecords(genericName));
            result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }

        } catch (SQLException e) {
            LOGGER.error(FAILED_DELETE, genericName, e);
            throw new DAOException(e);
        } finally {
            closeAll(conn, statement, result);
        }
        return 0;
    }

    /**
     * Implement this method according to your entity class and get params from {@link ResultSet}
     * according to fields of the entity class
     * @param result - {@link ResultSet} object, containing fields of the entity
     * @return object of {@link SimpleEntity} interface
     * @throws SQLException
     */
    protected abstract T getEntityFromResult(ResultSet result) throws SQLException;

    /**
     * Implement this method according to your entity class and set params of prepared statement
     * according to fields of the entity class
     * @param entity object of {@link SimpleEntity} interface
     * @param statement prepared statement to set params
     * @param update true only for update operation
     * @throws SQLException
     */
    protected abstract void setStatementParams(T entity, PreparedStatement statement, boolean update) throws SQLException;

    private T getAbstractObjFromResult(ResultSet result) throws SQLException {
        LOGGER.trace(genericName + GET_FROM_RS);
        T entity = getEntityFromResult(result);
        LOGGER.trace(genericName + GET_FROM_RS + entity.toString());
        return entity;
    }

    private void setAbstractStatementParams(T entity, PreparedStatement statement, boolean update) throws SQLException {
        LOGGER.trace(SET_STATEMENT, genericName);
        //implement this
        setStatementParams(entity, statement, update);
        LOGGER.trace(entity.toString());
    }

    protected void closeAll(Connection conn, Statement statement) throws DAOException {
        try {
            LOGGER.trace(CLOSE_CONN_ST, genericName, conn != null, statement != null);
            if (statement != null)
                statement.close();
            if (conn != null && conn.getAutoCommit()) {
                conn.close();
            }
        } catch (SQLException e) {
            LOGGER.error(FAILED_CLOSE_ST_CONN, e);
            throw new DAOException(e);
        }
    }

    private void closeAll(Connection conn, Statement statement, ResultSet rs) throws DAOException {
        try {
            LOGGER.trace(genericName + CLOSE_RESULT_SET, rs != null);
            if (rs != null)
                rs.close();
            closeAll(conn, statement);
        } catch (SQLException e) {
            LOGGER.error(FAILED_CLOSE_ST_CONN_RS, genericName, e);
            throw new DAOException(e);
        }
    }


}

//    @Override
//    public int findMaxId() throws DAOException {
//        LOGGER.info(FIND_MAX_ID, genericName);
//        Connection conn = null;
//        PreparedStatement statement = null;
//        ResultSet result = null;
//        int maxId = 0;
//        try {
//            conn = myDbConnection.getConnection();
//            statement = conn.prepareStatement(getSQLFindMaxId(genericName));
//            result = statement.executeQuery();
//            if (result.next()) {
//                maxId = result.getInt(USER_ID);
//            }
//            return maxId;
//        } catch (SQLException | NamingException e) {
//            LOGGER.error(FAILED_FIND_MAX_ID, genericName, e);
//            throw new DAOException(e);
//        } finally {
//            closeAll(conn, statement, result);
//        }
//    }

/*
    @Override
    public final boolean updateBatchOfObj(List<T> entities) throws DAOException {
        LOGGER.info(UPDATE, genericName);
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = myDbConnection.getConnection();
            statement = conn.prepareStatement(getSQLUpdateEntityById(genericName));
            return setBatchOfEntities(entities, conn, statement);
        } catch (SQLException | NamingException e) {
            LOGGER.error(FAILED_UPDATE + genericName, e);
            throw new DAOException(e);
        } finally {
            closeAll(conn, statement);
        }
    }

    @Override
    public final boolean insertBatchOfObj(List<T> entities) throws DAOException {
        LOGGER.info(INSERT, genericName);
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = myDbConnection.getConnection();
            statement = conn.prepareStatement(getSqlInsertEntity(genericName));
            return setBatchOfEntities(entities, conn, statement);
        } catch (SQLException | NamingException e) {
            LOGGER.error(FAILED_INSERT + genericName, e);
            conn.rollback();
            throw new DAOException(e);
        } finally {
            closeAll(conn, statement);
        }
    }

    private boolean setBatchOfEntities(List<T> entities, Connection conn, PreparedStatement statement) throws SQLException {
        conn.setAutoCommit(false);
        for (T entity : entities) {
            setAbstractStatementParams(entity, statement);
            statement.addBatch();
        }
        boolean doneTransaction = entities.size() == Arrays.stream(statement.executeBatch()).sum();
        LOGGER.debug(TRANSACTION_MSG, doneTransaction);
        if (doneTransaction) {
            conn.commit();
            return true;
        } else {
            conn.rollback();
            return false;
        }
    }
*/