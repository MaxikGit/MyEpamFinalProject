package com.max.restaurant.exceptions;

public class CommandException extends DAOException{
    public CommandException(String message){
        super(message);
    }

    public CommandException(String s, Exception e) {
        super(s, e);
    }
}
