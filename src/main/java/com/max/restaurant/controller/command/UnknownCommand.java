package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.utils.UtilsEmailNotificator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.max.restaurant.utils.UtilsFileNames.HOME_PAGE;
import static com.max.restaurant.utils.UtilsLoggerMsgs.FORWARD;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;

/**
 * Command that is used to forward/redirect user to main page in cases of unusual commands  
 */
public class UnknownCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnknownCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        LOGGER.info(FORWARD, HOME_PAGE);
        String from = "ertsd@ukr.com";
        String to = "masikgoog@gmail.com";
        String subject = "test probe ";
        String message = "test probe ertsd@ukr.com";

        try {
            UtilsEmailNotificator.send(List.of(to), subject, message, null, null, null);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
        request.getRequestDispatcher(HOME_PAGE).forward(request, response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        LOGGER.info(FORWARD, HOME_PAGE);
        request.getRequestDispatcher(HOME_PAGE).forward(request, response);
    }
}
