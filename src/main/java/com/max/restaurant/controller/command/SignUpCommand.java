package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.CommandException;
import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.exceptions.DAOServiceException;
import com.max.restaurant.model.entity.User;
import com.max.restaurant.model.services.UserService;
import com.max.restaurant.utils.UtilsPasswordEncryption;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.max.restaurant.utils.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsEntityFields.*;
import static com.max.restaurant.utils.UtilsFileNames.LOGIN_PAGE;
import static com.max.restaurant.utils.UtilsFileNames.SIGN_UP_PAGE;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * Command to sign up new users.<br>
 * The params of request, to call this command: action=signUp
 */
public class SignUpCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        request.removeAttribute(UNSUCCESS_MSG);
        request.getSession().removeAttribute(UNSUCCESS_ATTR);
        LOGGER.info(FORWARD, SIGN_UP_PAGE);
        request.getRequestDispatcher(SIGN_UP_PAGE).forward(request, response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        boolean isValid = validateUser(request);
        LOGGER.info(IS_VALID, isValid);
        String forwardPage;
        HttpSession session = request.getSession();
        if (isValid) {
            String email = request.getParameter(USER_EMAIL);
            UserService userService = new UserService();
            User user = userService.findUserByEmail(email);
            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setName(request.getParameter(USER_NAME));
                user.setLastName(request.getParameter(USER_LASTNAME));
                try {
                    String password = UtilsPasswordEncryption
                            .getNewEncryptedPass(request.getParameter(USER_PASSWORD).toCharArray());
                    user.setPassword(password);
                } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                    LOGGER.error(METHOD_FAILED, "getNewEncryptedPass", e);
                    throw new CommandException(e);
                }
                user.setRoleId(ROLE_CLIENT_ID);
                try {
                    userService.insertUser(user);
                    session.setAttribute(USER_EMAIL, email);
                    forwardPage = request.getServletContext().getContextPath() + LOGIN_PAGE;
                } catch (DAOServiceException e) {
                    session.setAttribute(UNSUCCESS_ATTR, UNSUCCESS_MSG2);
                    forwardPage = request.getServletContext().getContextPath() + SIGN_UP_PAGE;
                }
            } else {
                session.setAttribute(USER_EMAIL, email);
                session.setAttribute(UNSUCCESS_ATTR, UNSUCCESS_MSG3);
                forwardPage = request.getServletContext().getContextPath() + LOGIN_PAGE;
            }
        } else {
            session.setAttribute(UNSUCCESS_ATTR, UNSUCCESS_MSG2);
            forwardPage = request.getServletContext().getContextPath() + SIGN_UP_PAGE;
        }
        LOGGER.info(REDIRECT, forwardPage);
        response.sendRedirect(forwardPage);
    }

    private static boolean validateUser(HttpServletRequest request) {
        return !(request.getParameter(USER_EMAIL) == null || request.getParameter(USER_EMAIL).isBlank()
                || request.getParameter(USER_NAME) == null || request.getParameter(USER_NAME).isBlank()
                || request.getParameter(USER_LASTNAME) == null || request.getParameter(USER_LASTNAME).isBlank()
                || request.getParameter(USER_PASSWORD) == null || request.getParameter(USER_PASSWORD).isBlank()
                || !request.getParameter(USER_PASSWORD).equals(request.getParameter(USER_REPASSWORD)));
    }
}
