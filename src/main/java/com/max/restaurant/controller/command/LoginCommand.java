package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.CommandException;
import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.OrderData;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.User;
import com.max.restaurant.model.services.CustomService;
import com.max.restaurant.model.services.UserService;
import com.max.restaurant.utils.UtilsPasswordEncryption;
import com.max.restaurant.utils.UtilsReCaptchaVerifier;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import static com.max.restaurant.utils.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsEntityFields.USER_EMAIL;
import static com.max.restaurant.utils.UtilsEntityFields.USER_PASSWORD;
import static com.max.restaurant.utils.UtilsExceptionMsgs.FRONT_VALIDATION_EXC;
import static com.max.restaurant.utils.UtilsFileNames.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;
import static com.max.restaurant.utils.UtilsReCaptchaVerifier.reCAPTCHA_ATTR;

/**
 * Command used to log in/out User and to display brief information about his orders<br>
 * The params of request, to call this command: action=login
 */
public class LoginCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        String page;
        if (request.getSession().getAttribute(LOGGED_USER_ATTR) != null) {
            request.getSession().removeAttribute(LOGGED_USER_ATTR);
            request.getSession().invalidate();
            page = request.getContextPath() + HOME_PAGE;
            LOGGER.debug(REDIRECT, page);
            response.sendRedirect(page);
            return;
        } else {
            request.removeAttribute(UNSUCCESS_MSG);
            request.getSession().removeAttribute(UNSUCCESS_ATTR);
            page = LOGIN_PAGE;
        }
        LOGGER.debug(FORWARD, page);
        request.getRequestDispatcher(page).forward(request, response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        String notValid = preValidateUser(request);
        LOGGER.info(IS_VALID, notValid == null);
        String forwardPage;
        HttpSession session = request.getSession();
        if (notValid == null) {
            String email = request.getParameter(USER_EMAIL).strip().toLowerCase();
            UserService userService = new UserService();
            User user = userService.findUserByEmail(email);
            if (user != null) {
                String password = request.getParameter(USER_PASSWORD);
                try {
                    if(UtilsPasswordEncryption.authenticate(password.toCharArray(), user.getPassword()) ){
    //                if (password.equals(user.getPassword())) {
                        session.removeAttribute(UNSUCCESS_ATTR);
                        session.setAttribute(LOGGED_USER_ATTR, user);
                        List<OrderData> orders = setOrdersPending(user.getId());
                        session.setAttribute(CUSTOM_LIST_ATTR, orders);
                        forwardPage = request.getServletContext().getContextPath() + HOME_PAGE;
                        LOGGER.debug(TWO_PARAMS_MSG, LOGGED_USER_ATTR, user);
                        session.setMaxInactiveInterval(10);
                    } else {
                        session.setAttribute(USER_EMAIL, email);
                        session.setAttribute(UNSUCCESS_ATTR, UNSUCCESS_MSG2);
                        forwardPage = request.getServletContext().getContextPath() + LOGIN_PAGE;
                        LOGGER.debug(TWO_PARAMS_MSG, UNSUCCESS_ATTR, UNSUCCESS_MSG);
                    }
                } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                    LOGGER.error(METHOD_FAILED, "encryption");
                    throw new RuntimeException(e);
                }
            } else {
                session.setAttribute(UNSUCCESS_ATTR, UNSUCCESS_MSG);
                forwardPage = request.getServletContext().getContextPath() + SIGN_UP_PAGE;
                LOGGER.debug(TWO_PARAMS_MSG, UNSUCCESS_ATTR, UNSUCCESS_MSG);
            }
        } else {
            if (notValid.equals(reCAPTCHA_ATTR)) {
                session.setAttribute(UNSUCCESS_ATTR, UNSUCCESS_MSG4);
                LOGGER.debug(TWO_PARAMS_MSG, UNSUCCESS_ATTR, UNSUCCESS_MSG4);
                forwardPage = request.getServletContext().getContextPath() + LOGIN_PAGE;
            } else {
                LOGGER.error(METHOD_FAILED, "notValid" + notValid);
                throw new CommandException(FRONT_VALIDATION_EXC);
            }
        }
        LOGGER.debug(REDIRECT, forwardPage);
        response.sendRedirect(forwardPage);
    }

    private static String preValidateUser(HttpServletRequest request) throws IOException {
        if (request.getParameter(USER_EMAIL) == null || request.getParameter(USER_EMAIL).isBlank()) {
            return USER_EMAIL;
        } else if (request.getParameter(USER_PASSWORD) == null || request.getParameter(USER_PASSWORD).isBlank()) {
            return USER_PASSWORD;
        } else if (!UtilsReCaptchaVerifier.verify(request)) {
//            return reCAPTCHA_ATTR;
        }
        return null;
    }

    private List<OrderData> setOrdersPending(int userId) throws DAOException {
        CustomService service = new CustomService();
        List<Custom> customList = service.getUsersCustomsInProgress(userId);
        return OrderData.getOrderDataList(customList);
    }
}
