package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.CommandException;
import com.max.restaurant.exceptions.DAOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * This interface used to mark classes, that are used as servlet commands.
 */
public interface Command {
    /**
     * Method to process GET requests from client
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws DAOException
     */
    void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException;

    /**
     * Method to process POST requests from client
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws DAOException
     * @throws CommandException
     */
    void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException, CommandException;
}
