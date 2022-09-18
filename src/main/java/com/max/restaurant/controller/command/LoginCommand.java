package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.dao.services.UserService;
import com.max.restaurant.model.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.max.restaurant.controller.command.UtilsCommandNames.*;
import static com.max.restaurant.model.entity.UtilsEntityFields.*;
import static com.max.restaurant.utils.UtilsFileNames.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

public class LoginCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        request.removeAttribute(UNSUCCESS_MSG);
        request.getSession().removeAttribute(UNSUCCESS_ATTR);
        request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        boolean isValid = validateUser(request);
        LOGGER.info(IS_VALID, isValid);
        String forwardPage;
        HttpSession session = request.getSession();
//        session.removeAttribute(LOGGED_USER_ATTR);
        if (isValid) {
            String email = request.getParameter(USER_EMAIL).strip().toLowerCase();
            UserService userService = new UserService();
            User user = userService.findUserByEmail(email);
            if (user != null) {
                String password = request.getParameter(USER_PASSWORD);
                if (password.equals(user.getPassword())) {
                    session.removeAttribute(UNSUCCESS_ATTR);
                    session.setAttribute(LOGGED_USER_ATTR, user);
                    forwardPage = request.getServletContext().getContextPath() + HOME_PAGE;
                    LOGGER.debug(TWO_PARAMS_MSG, LOGGED_USER_ATTR, user);
                } else {
                    session.setAttribute(USER_EMAIL, email);
                    session.setAttribute(UNSUCCESS_ATTR, UNSUCCESS_MSG2);
                    forwardPage = request.getServletContext().getContextPath() + LOGIN_PAGE;
                    LOGGER.debug(TWO_PARAMS_MSG, UNSUCCESS_ATTR, UNSUCCESS_MSG);
                }
            } else {
                session.setAttribute(UNSUCCESS_ATTR, UNSUCCESS_MSG);
                forwardPage = request.getServletContext().getContextPath() + SIGN_UP_PAGE;
                LOGGER.debug(TWO_PARAMS_MSG, UNSUCCESS_ATTR, UNSUCCESS_MSG);
            }
        } else {
            session.setAttribute(UNSUCCESS_ATTR, UNSUCCESS_MSG2);
            forwardPage = request.getServletContext().getContextPath() + LOGIN_PAGE;
            LOGGER.debug(TWO_PARAMS_MSG, UNSUCCESS_ATTR, UNSUCCESS_MSG);
        }
        LOGGER.debug(REDIRECT, forwardPage);
        response.sendRedirect(forwardPage);
    }

    private static boolean validateUser(HttpServletRequest request) {
        return !(request.getParameter(USER_EMAIL) == null || request.getParameter(USER_EMAIL).isBlank()
                || request.getParameter(USER_PASSWORD) == null || request.getParameter(USER_PASSWORD).isBlank());
    }
}
