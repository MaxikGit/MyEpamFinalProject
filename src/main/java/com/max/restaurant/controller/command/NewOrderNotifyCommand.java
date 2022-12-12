package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.CommandException;
import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import static com.max.restaurant.utils.UtilsCommandNames.LOGGED_USER_ATTR;
import static com.max.restaurant.utils.UtilsCommandNames.NEW_ORDERS_ATTR;
import static com.max.restaurant.utils.UtilsExceptionMsgs.UNUSED_METHOD_WORKS;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;
import static com.max.restaurant.utils.UtilsLoggerMsgs.TWO_PARAMS_MSG;

/**
 * Command that is used to check if there are new orders. If they are it sends response status 200 to manager`s page.
 * If not - status 202 or 404 if manager logged out.
 * The params of request, to call this command: action=notify
 */
public class NewOrderNotifyCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        @SuppressWarnings("unchecked")
        Map<Integer, Object> newOrdersMap = (Map<Integer, Object>) request.getServletContext().getAttribute(NEW_ORDERS_ATTR);
        HttpSession session = request.getSession(false);
        User user = null;
        if (session !=null)
            user = (User) session.getAttribute(LOGGED_USER_ATTR);
        int responseStatus;
        if (user == null || user.getRoleId() != 1) {
            responseStatus = 404;
        } else if (newOrdersMap.size() < 1) {
            responseStatus = 202;
        } else {
            responseStatus = 200;
        }
        LOGGER.debug(TWO_PARAMS_MSG, responseStatus, user == null ? "no User" : user.getEmail());
        response.setStatus(responseStatus);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        throw new CommandException(UNUSED_METHOD_WORKS);
    }
}
