package com.max.restaurant.exceptions;

public class DAOException extends Exception{
    public DAOException(){}
    public DAOException(String message){
        super(message);
    }
    public DAOException(Exception e) {
        super(e);
    }
    public DAOException(String s, Exception e) {
        super(s, e);
    }
}
