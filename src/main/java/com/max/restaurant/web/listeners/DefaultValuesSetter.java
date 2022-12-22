package com.max.restaurant.web.listeners;

import com.max.restaurant.model.entity.User;
import com.max.restaurant.utils.LongPollingUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;

import java.util.concurrent.ConcurrentHashMap;

import static com.max.restaurant.utils.UtilsCommandNames.*;

/**
 * initialize default values of variables:<br>
 * default language attribute is set to "uk" <br>
 * logged users counter initialized as {@code Map<userId, session>} where userId is the id of logged user and session is
 * a link to user`s HttpSession object<br>
 * new order was placed counter initialized as {@code Map<orderId, object>} where orderId is the id of new order and object is
 *  null<br>
 * @see com.max.restaurant.utils.UtilsCommandNames#LANG_ATTR
 * @see com.max.restaurant.utils.UtilsCommandNames#LOGGED_USERS_ATTR
 * @see com.max.restaurant.utils.UtilsCommandNames#NEW_ORDERS_ATTR
 */
@WebListener
public class DefaultValuesSetter implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(LANG_ATTR, UKR_ATTR);
        sce.getServletContext().setAttribute(LOGGED_USERS_ATTR, new ConcurrentHashMap<User, HttpSession>());
        sce.getServletContext().setAttribute(NEW_ORDERS_ATTR, new ConcurrentHashMap<Integer, Object>());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LongPollingUtil longPolling = LongPollingUtil.getInstance();
        longPolling.endPoll();
    }
}
