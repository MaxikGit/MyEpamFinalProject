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

import static com.max.restaurant.model.dao.UtilsSQLConstants.*;
import static com.max.restaurant.model.entity.UtilsEntityFields.CUSTOM_ID;
import static com.max.restaurant.model.entity.UtilsEntityFields.USER_ID;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * This class provides a skeletal implementation of the DAOSimpleEntity interface to minimize the effort required
 * to implement this interface. Basically, subclass needs to implement two methods:
 * {@link #getEntityFromResult(ResultSet)}, {@link #setStatementParams(SimpleEntity, PreparedStatement, boolean)}
 *
 * @param <T> the object type of {@link SimpleEntity} interface
 */
abstract class AbstractDAOSimpleEntity<T extends SimpleEntity> implements DAOSimpleEntity<T> {
    //    private MyDBConnection myDbConnection;
    private final Logger LOGGER;
    private final String genericName;

    /**
     * The main constructor used by subclasses
     * @param genericSimpleName used to achieve SQL expression for the concrete entity.
     *                          Its value is equal to {@code Class.getSimpleName()}
     */
    protected AbstractDAOSimpleEntity(String genericSimpleName){
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
    public final T findObjById(int id) throws DAOException {
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
    public final List<T> findObjByParam(String paramName, String param, Connection conn) throws DAOException {
        LOGGER.info(genericName + FIND_BY_PARAM, paramName, param);
//        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        List<T> entities = new ArrayList<>();
        try {
//            conn = getConnection();
            String sql = getSQLFindByParam(genericName, paramName);
            LOGGER.info(genericName + FIND_BY_PARAM, paramName, param);
            statement = conn.prepareStatement(sql);
            statement.setString(1, param);
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
    public final boolean insertObj(T entity, Connection conn) throws DAOException {
        LOGGER.info(INSERT_MSG, genericName);
//        Connection conn = null;
        PreparedStatement statement = null;
        try {
//            conn = getConnection();
            statement = conn.prepareStatement(getSqlInsertEntity(genericName));
            //implement this
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
//        Connection conn = null;
        PreparedStatement statement = null;
        try {
//            conn = myDbConnection.getConnection();
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

    protected abstract T getEntityFromResult(ResultSet result) throws SQLException;

    protected abstract void setStatementParams(T entity, PreparedStatement statement, boolean update) throws SQLException;

    private T getAbstractObjFromResult(ResultSet result) throws SQLException {
        LOGGER.info(genericName + GET_FROM_RS);
        T entity = getEntityFromResult(result);
        LOGGER.debug(genericName + GET_FROM_RS + entity.toString());
        return entity;
    }

    private void setAbstractStatementParams(T entity, PreparedStatement statement, boolean update) throws SQLException {
        LOGGER.info(SET_STATEMENT, genericName);
        //implement this
        setStatementParams(entity, statement, update);
        LOGGER.debug(entity.toString());
    }

    protected void closeAll(Connection conn, Statement statement) throws DAOException {
        try {
            LOGGER.info(CLOSE_CONN_ST, genericName, conn != null, statement != null);
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
            LOGGER.info(genericName + CLOSE_RESULT_SET, rs != null);
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