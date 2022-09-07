package com.max.restaurant.model.dao;


import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.*;

import static com.max.restaurant.model.entity.UtilsEntityFields.*;

public class UtilsSQLConstants {

    public static final String SETTINGS_FILE = "app.properties";
    //UserDAO
    private static final String SQL_FIND_ENTITY_BY_PARAM = "SELECT * FROM %s WHERE %s=?";
    private static final String SQL_FIND_ALL_ENTITIES = "SELECT * FROM %s";
    private static final String SQL_FIND_ENTITIES_MAX_ID = "SELECT MAX(id) FROM %s";//delete then
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
            "UPDATE %%s SET %s=?, %s=?, %s=?, %s=? %s=? %s=? WHERE %s=?",
            USER_EMAIL, USER_NAME, USER_LASTNAME, USER_PASSWORD, USER_DETAILS, USER_ROLE_ID, USER_ID);
    //CategoryDAO
    private static final String SQL_UPDATE_CATEGORY_BY_ID =String.format(
            "UPDATE %%s SET %s=? WHERE %s=?", CATEGORY_NAME, CATEGORY_ID);
    //CustomDAO
    private static final String SQL_UPDATE_CUSTOM_BY_ID = String.format(
            "UPDATE %%s SET %s=?, %s=?, %s=?, %s=? WHERE %s=?",
            CUSTOM_COST, CUSTOM_TIME, CUSTOM_USER_ID, CUSTOM_STATUS_ID, CUSTOM_ID);
    //Custom_has_dishDAO
    private static final String SQL_UPDATE_CUSTOMHASDISH_BY_CUSTOM_ID = String.format(
            "UPDATE %%s SET %s=?, %s=?, %s=? WHERE %s=?",
            CUSTOMHASDISH_D_ID, CUSTOMHASDISH_COUNT, CUSTOMHASDISH_PRICE, CUSTOMHASDISH_C_ID);
    //DishDAO
    private static final String SQL_UPDATE_DISH_BY_ID = String.format(
            "UPDATE %%s SET %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
            DISH_NAME, DISH_PRICE, DISH_DETAILS, DISH_CATEGORY_ID, DISH_IMAGE_PATH, DISH_ID);
    //RoleDAO
    private static final String SQL_UPDATE_ROLE_BY_ID =String.format(
            "UPDATE %%s SET %s=? WHERE %s=?", ROLE_NAME, ROLE_ID);
    //StatusDAO
    private static final String SQL_UPDATE_STATUS_BY_ID = String.format(
            "UPDATE %%s SET %s=?, %s=? WHERE %s=?",
            STATUS_NAME, STATUS_DETAILS, STATUS_ID);


    //"SELECT * FROM %s WHERE %s=?";
    public static String getSQLFindByParam(String simpleClassName, String paramName) throws DAOException {
        String ss = String.format(SQL_FIND_ENTITY_BY_PARAM, getTableNameByClass(simpleClassName), paramName);
        return ss;
    }

//    "SELECT * FROM %s";
    public static String getSQLFindAll(String simpleClassName) throws DAOException {
        return String.format(SQL_FIND_ALL_ENTITIES, getTableNameByClass(simpleClassName));
    }
    //    "SELECT MAX(id) FROM %s";
    public static String getSQLFindMaxId(String simpleClassName) throws DAOException {
        return String.format(SQL_FIND_ENTITIES_MAX_ID, getTableNameByClass(simpleClassName));
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
        return String.format(SQL_INSERT_ENTITY, getTableNameByClass(simpleClassName), params);
    }

    //"DELETE FROM %s WHERE %s=?";
    public static String getSQLDeleteByParam(String simpleClassName, String paramName) throws DAOException {
        return String.format(SQL_DELETE_ENTITY_BY_PARAM, getTableNameByClass(simpleClassName), paramName);
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
