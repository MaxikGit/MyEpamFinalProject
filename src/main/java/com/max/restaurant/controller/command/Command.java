package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.CommandException;
import com.max.restaurant.exceptions.DAOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface Command {
    void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException;

    void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException, CommandException;
}
