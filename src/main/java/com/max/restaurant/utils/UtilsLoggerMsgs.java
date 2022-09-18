package com.max.restaurant.utils;

public class UtilsLoggerMsgs {
    public static final String CONSTRUCTOR = "constructor started";
    public static final String FAILED_CONSTRUCTOR = " constructor failed";
    public static final String STATIC_INI = " static initialization block";

    public static final String ONE_PARAM = "set param is: {}";
    public static final String TWO_PARAMS_MSG = "set params are: {}, {}";
    public static final String IS_VALID = " data params are valid: {}";
    public static final String TRANSACTION_ROLLBACK = " transaction rollback";
    public static final String TRANSACTION_ROLLBACK_FAILED = " transaction rollback was unsuccessful";
    public static final String TRANSACTION_MSG = "transaction done successfully {}";


    public static final String METHOD_STARTS_MSG = "method {} has been started = {}";
    public static final String METHOD_FAILED = " method failed {}";
    public static final String GET_FROM_RS = " receiving from the result ";
    public static final String SET_STATEMENT = "{} set statement params";
    public static final String FORWARD = "forwarding to {}";
    public static final String REDIRECT = "redirecting to {}";
    public static final String SQL_EXPR_MSG = "for {} operation, entity {}, expr = {}";

    public static final String FIND_BY_ID = "find by Id = {}";
    public static final String FAILED_FIND_BY_ID = "{} can't find By Id";
    public static final String FIND_BY_PARAM = " find by {} = {}";
    public static final String FAILED_FIND_BY_PARAM = " can't be find By {}";
    public static final String FIND_ALL = "finding every {}";
    public static final String FAILED_FIND_ALL = "failed find every {}";
    public static final String FIND_MAX_ID = "finding max ID of {}";
    public static final String FAILED_FIND_MAX_ID = "failed finding max ID of {}";

    public static final String CLOSE_CONN_ST = "{} close all, conn exist - {}, statement exist - {}";
    public static final String CLOSE_TRANSACTION = "close transaction, conn exist - {}";
    public static final String CLOSE_RESULT_SET = " close all, resultSet exist - {}";
    public static final String FAILED_CLOSE_ST_CONN = " failed to close conn & statement ";
    public static final String FAILED_CLOSE_ST_CONN_RS = "{} failed to close resultSet conn & statement ";

    public static final String INSERT_MSG = "Insert {}";
    public static final String FAILED_INSERT = "failed insert {}";
    public static final String UPDATE_MSG = "Update {}";
    public static final String FAILED_UPDATE = "Failed to update {}";
    public static final String DELETE_MSG = "Delete {}";
    public static final String FAILED_DELETE = "failed to delete {}";

}
