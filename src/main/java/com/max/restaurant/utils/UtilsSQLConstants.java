package com.max.restaurant.utils;


import com.max.restaurant.exceptions.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.max.restaurant.utils.UtilsEntityFields.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.SQL_EXPR_MSG;

/**
 * Utility class used to keep all SQL expressions in one place & .
 */
public class UtilsSQLConstants {
    private static final Logger LOGGER = LoggerFactory.getLogger(UtilsSQLConstants.class);

    //UserDAO
    private static final String SQL_FIND_ENTITY_BY_PARAM = "SELECT * FROM %s WHERE %s=?";
    private static final String SQL_FIND_ALL_ENTITIES = "SELECT * FROM %s";
    private static final String SQL_INSERT_ENTITY = "INSERT INTO %s VALUES (%s)";
    private static final String SQL_DELETE_ENTITY_BY_PARAM = "DELETE FROM %s WHERE %s=?";

    private static final String USER_PARAMS_LIST =      "DEFAULT, ?, ?, ?, ?, ?, ?";
    private static final String ROLE_PARAMS_LIST =      "DEFAULT, ?";
    private static final String CATEGORY_PARAMS_LIST = ROLE_PARAMS_LIST;
    private static final String DISH_PARAMS_LIST =      "DEFAULT, ?, ?, ?, ?, ?";
    private static final String CUSTOM_PARAMS_LIST =    "DEFAULT, ?, ?, ?, ?";
    private static final String CUSTOMHASDISH_PARAMS_LIST = "?, ?, ?, ?";
    private static final String STATUS_PARAMS_LIST =    "DEFAULT, ?, DEFAULT";

    private static final String SQL_UPDATE_USER_BY_ID = String.format(
            "UPDATE %%s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
            USER_EMAIL, USER_NAME, USER_LASTNAME, USER_PASSWORD, USER_DETAILS, USER_ROLE_ID, USER_ID);
    private static final String SQL_UPDATE_CATEGORY_BY_ID =String.format(
            "UPDATE %%s SET %s=? WHERE %s=?", CATEGORY_NAME, CATEGORY_ID);
    private static final String SQL_UPDATE_CUSTOM_BY_ID = String.format(
            "UPDATE %%s SET %s=?, %s=?, %s=?, %s=? WHERE %s=?",
            CUSTOM_COST, CUSTOM_TIME, CUSTOM_USER_ID, CUSTOM_STATUS_ID, CUSTOM_ID);
    private static final String SQL_UPDATE_CUSTOMHASDISH_BY_CUSTOM_ID = String.format(
            "UPDATE %%s SET %s=?, %s=? WHERE %s=? AND %s=?",
            CUSTOMHASDISH_COUNT, CUSTOMHASDISH_PRICE, CUSTOMHASDISH_C_ID, CUSTOMHASDISH_D_ID);
    private static final String SQL_UPDATE_DISH_BY_ID = String.format(
            "UPDATE %%s SET %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
            DISH_NAME, DISH_PRICE, DISH_DETAILS, DISH_CATEGORY_ID, DISH_IMAGE_PATH, DISH_ID);
    private static final String SQL_UPDATE_ROLE_BY_ID =String.format(
            "UPDATE %%s SET %s=? WHERE %s=?", ROLE_NAME, ROLE_ID);
    private static final String SQL_UPDATE_STATUS_BY_ID = String.format(
            "UPDATE %%s SET %s=?, %s=? WHERE %s=?",
            STATUS_NAME, STATUS_DETAILS, STATUS_ID);
    private static final String SQL_RECORDS_COUNT = "SELECT count(*) FROM %s";


    //"SELECT * FROM %s WHERE %s=?";
    /**
     *
     * @param simpleClassName String that equals to the result of {@link Class#getSimpleName()} method
     * @param paramName one of the constants of {@link UtilsEntityFields} class
     * @return {@link String} that looks like: <i>SELECT * FROM <strong>simpleClassName</strong> WHERE <strong>paramName</strong>=?<i/>
     * @throws DAOException throws {@link DAOException} if simpleClassName is unknown
     */
    public static String getSQLFindByParam(String simpleClassName, String paramName) throws DAOException {
        String ss = String.format(SQL_FIND_ENTITY_BY_PARAM, getTableNameByClass(simpleClassName), paramName);
        LOGGER.trace(SQL_EXPR_MSG, "FIND", simpleClassName, ss);
        return ss;
    }

//    "SELECT * FROM %s";
    public static String getSQLFindAll(String simpleClassName) throws DAOException {
        String ss = String.format(SQL_FIND_ALL_ENTITIES, getTableNameByClass(simpleClassName));
        LOGGER.trace(SQL_EXPR_MSG, "FIND ALL", simpleClassName, ss);
        return ss;
    }

    //    "SELECT count(*) FROM %s";
    public static String getSqlNumberOfRecords(String simpleClassName) throws DAOException {
        String ss = String.format(SQL_RECORDS_COUNT, getTableNameByClass(simpleClassName));
        LOGGER.trace(SQL_EXPR_MSG, "getSqlNumberOfRecords", simpleClassName, ss);
        return ss;
    }

    public static String getSqlInsertEntity(String simpleClassName) throws DAOException {
        String params;
        switch (getTableNameByClass(simpleClassName)) {
            case USER_TABLE:
                params = USER_PARAMS_LIST;
                break;
            case ROLE_TABLE:
            case CATEGORY_TABLE:
                params = ROLE_PARAMS_LIST;
                break;
            case CUSTOM_TABLE:
                params = CUSTOM_PARAMS_LIST;
                break;
            case DISH_TABLE:
                params = DISH_PARAMS_LIST;
                break;
            case CUSTOM_HAS_DISH_TABLE:
                params = CUSTOMHASDISH_PARAMS_LIST;
                break;
            case STATUS_TABLE:
                params = STATUS_PARAMS_LIST;
                break;
            default: throw new DAOException("wrong table name");
        }
        String ss = String.format(SQL_INSERT_ENTITY, getTableNameByClass(simpleClassName), params);
        LOGGER.debug(SQL_EXPR_MSG, "INSERT", simpleClassName, ss);
        return ss;
    }

    //"DELETE FROM %s WHERE %s=?";
    public static String getSQLDeleteByParam(String simpleClassName, String paramName) throws DAOException {
        String ss = String.format(SQL_DELETE_ENTITY_BY_PARAM, getTableNameByClass(simpleClassName), paramName);
        LOGGER.trace(SQL_EXPR_MSG, "DELETE", simpleClassName, ss);
        return ss;
    }
    //"DELETE FROM %s WHERE %s=?";
    public static String getSQLUpdateEntityById(String simpleClassName) throws DAOException {
        switch (getTableNameByClass(simpleClassName)) {
            case USER_TABLE: return String.format(SQL_UPDATE_USER_BY_ID, USER_TABLE);
            case ROLE_TABLE: return String.format(SQL_UPDATE_ROLE_BY_ID, ROLE_TABLE);
            case CATEGORY_TABLE: return String.format(SQL_UPDATE_CATEGORY_BY_ID, CATEGORY_TABLE);
            case CUSTOM_TABLE: return String.format(SQL_UPDATE_CUSTOM_BY_ID, CUSTOM_TABLE);
            case DISH_TABLE: return String.format(SQL_UPDATE_DISH_BY_ID, DISH_TABLE);
            case CUSTOM_HAS_DISH_TABLE: return String.format(SQL_UPDATE_CUSTOMHASDISH_BY_CUSTOM_ID, CUSTOM_HAS_DISH_TABLE);
            case STATUS_TABLE: return String.format(SQL_UPDATE_STATUS_BY_ID, STATUS_TABLE);
            default: throw new DAOException("something wrong in SQL update building");
        }
    }
}
