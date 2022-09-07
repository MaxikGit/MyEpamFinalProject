package com.max.restaurant.model.dao;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.SimpleEntity;

import java.sql.Connection;
import java.util.List;

public interface DAOSimpleEntity<T extends SimpleEntity> {

    T findObjById(int id) throws DAOException;

//    T findObjByParam(String param) throws DAOException;

    //need to introduce params in util to insert
    List<T> findObjByParam(String paramName, String param) throws DAOException;

    List<T> findAll() throws DAOException;

//    int findMaxId() throws DAOException;

    boolean insertObj(T obj, Connection conn) throws DAOException;

//    boolean insertBatchOfObj(List<T> obj) throws DAOException;

    boolean updateObj(T obj, Connection conn) throws DAOException;

//    boolean updateBatchOfObj(List<T> obj) throws DAOException;

    boolean deleteObj(T obj) throws DAOException;

    }
