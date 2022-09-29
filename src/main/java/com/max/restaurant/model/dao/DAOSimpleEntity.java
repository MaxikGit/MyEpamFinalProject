package com.max.restaurant.model.dao;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.SimpleEntity;

import java.sql.Connection;
import java.util.List;


/**
 * The {@code DAOSimpleEntity} interface provides the parameterized methods for interaction with database.
 * All methods suppress all SQL exceptions and throws own general one {@link DAOException}.
 *
 * @param <T> the object type of {@link SimpleEntity} interface
 */
public interface DAOSimpleEntity<T extends SimpleEntity> {

    /**
     * Used to find in database a single object of {@link SimpleEntity} class.
     *
     * @param id the record unique identifier
     * @return {@link SimpleEntity} interface object from database
     * @throws DAOException if any SQLException occurs
     */
    T findObjById(int id) throws DAOException;

    /**
     * Used to find in database {@link List} of objects of {@link SimpleEntity} class with certain params.
     *
     * @param paramName the name of database table's field
     * @param param     the value of database table's field
     * @param conn      instance of {@link Connection} class to connect the database
     * @return {@link List} of {@link SimpleEntity} class objects, or empty {@code List}
     * @throws DAOException if any SQLException occurs
     */
    List<T> findObjByParam(String paramName, String param, Connection conn) throws DAOException;

    /**
     * Finds all objects of {@link SimpleEntity} class in database.
     * @return {@link List} of {@link SimpleEntity} class objects, or empty {@code List}
     * @throws DAOException if any SQLException occurs
     */
    List<T> findAll() throws DAOException;

    /**
     * Inserts {@link SimpleEntity} object into a database.
     * @param obj object of {@link SimpleEntity} class to be inserted
     * @param conn instance of {@link Connection} class to connect the database
     * @return {@code true} if a record was inserted into a table, {@code false} otherwise
     * @throws DAOException if any SQLException occurs
     */
    boolean insertObj(T obj, Connection conn) throws DAOException;

    /**
     * Updates a record in a database.
     * @param obj object of {@link SimpleEntity} class to be updated
     * @param conn instance of {@link Connection} class to connect the database
     * @return {@code true} if a record was updated in a table, {@code false} otherwise
     * @throws DAOException if any SQLException occurs
     */
    boolean updateObj(T obj, Connection conn) throws DAOException;

    /**
     * Deletes a record in a database.
     * @param obj object of {@link SimpleEntity} class to be deleted
     * @param conn instance of {@link Connection} class to connect the database
     * @return {@code true} if a record was deleted in a table, {@code false} otherwise
     * @throws DAOException if any SQLException occurs
     */
    boolean deleteObj(T obj, Connection conn) throws DAOException;

}
