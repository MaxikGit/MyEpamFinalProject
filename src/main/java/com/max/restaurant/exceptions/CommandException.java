package com.max.restaurant.exceptions;

public class CommandException extends Exception{
    public CommandException(){}
    public CommandException(String message){
        super(message);
    }
    public CommandException(Exception e) {
        super(e);
    }
}
