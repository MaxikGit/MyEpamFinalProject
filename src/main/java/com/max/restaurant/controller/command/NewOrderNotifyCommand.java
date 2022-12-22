package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.CommandException;
import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.User;
import com.max.restaurant.model.services.CustomService;
import com.max.restaurant.utils.LongPollingUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.max.restaurant.utils.UtilsCommandNames.LOGGED_USER_ATTR;
import static com.max.restaurant.utils.UtilsCommandNames.NEW_ORDERS_ATTR;
import static com.max.restaurant.utils.UtilsExceptionMsgs.UNUSED_METHOD_WORKS;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;

/**
 * Command that is used to check if there are new orders. It is based on long polling mechanism.<br>
 * When new order accepted by client, this command sends response status 200 to manager`s page.
 * If not - it will wait and continue checking.
 * If manager logged out it will send response status 404.
 * The params of request, to call this command: action=notify
 */
public class NewOrderNotifyCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewOrderNotifyCommand.class);
    private final int newOrderId = 1;

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DAOException {

        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        HttpSession session = request.getSession(false);
        User user = null;
        if (session != null)
            user = (User) session.getAttribute(LOGGED_USER_ATTR);
        LongPollingUtil longPolling = LongPollingUtil.getInstance();
        checkNewOrdersInDB(request);
        if (user == null || user.getRoleId() != 1) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            longPolling.endPoll();
        } else {
            longPolling.startPoll(request.startAsync());
        }
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        throw new CommandException(UNUSED_METHOD_WORKS);
    }


    public static void checkManagersOnline(ConcurrentHashMap<User, HttpSession> contextSessionCounter) {
        LOGGER.debug(METHOD_STARTS_MSG, "checkManagersOnline", true);
        int managerId = 1;
        if (contextSessionCounter.size() > 0) {
            for (Map.Entry<User, HttpSession> entry : contextSessionCounter.entrySet()) {
                if (entry.getKey().getRoleId() == managerId) {
                    return;
                }
            }
        } else {
            LongPollingUtil longPollingUtil = LongPollingUtil.getInstance();
            longPollingUtil.endPoll();
        }
    }
    private void checkNewOrdersInDB(HttpServletRequest request) throws DAOException {
        CustomService customs = new CustomService();
        List<Custom> customList = customs.getCustomsInProgress();
        Map<Integer, Object> newOrdersMap = (Map<Integer, Object>) request.getServletContext().getAttribute(NEW_ORDERS_ATTR);
        for (Custom custom : customList) {
            if (custom.getStatusId() == newOrderId) {
                newOrdersMap.put(custom.getId(), "null");
            }
        }
        LOGGER.debug(METHOD_STARTS_MSG, "checkNewOrdersInDB", newOrdersMap.size());
    }
}

