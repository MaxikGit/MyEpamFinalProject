package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.DAOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.max.restaurant.utils.UtilsFileNames.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.FORWARD;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD;

public class NullCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(NullCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info(METHOD, "executeGet", "true");
        LOGGER.info(FORWARD, HOME_PAGE);
        request.getRequestDispatcher(HOME_PAGE).forward(request, response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD, "executePost", "true");
        LOGGER.info(FORWARD, HOME_PAGE);
        request.getRequestDispatcher(HOME_PAGE).forward(request, response);
    }
}
