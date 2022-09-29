package com.max.restaurant.exceptions;

public class DAOServiceException extends DAOException{
    public DAOServiceException(String message) {
        super(message);
    }
    public DAOServiceException(Exception exception) {
        super(exception);
    }
}
