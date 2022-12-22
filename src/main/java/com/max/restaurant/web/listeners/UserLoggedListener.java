package com.max.restaurant.web.listeners;

import com.max.restaurant.controller.command.NewOrderNotifyCommand;
import com.max.restaurant.model.entity.User;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.max.restaurant.utils.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * Listener that controls the uniqueness of logged user in the app. If the same user will log in from another device,
 * the old session invalidates. Shopping cart (if exist) will be moved to new device, same as chosen language.
 */
@WebListener
public class UserLoggedListener implements HttpSessionAttributeListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoggedListener.class);

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        if (sbe.getName().equals(LOGGED_USER_ATTR)) {
            LOGGER.debug(METHOD_STARTS_MSG, "attributeAdded", sbe.getValue());
            User user = (User) sbe.getValue();
            HttpSession newSession = sbe.getSession();
            ServletContext servletContext = newSession.getServletContext();
            @SuppressWarnings("unchecked")
            ConcurrentHashMap<User, HttpSession> contextSessionCounter =
                    (ConcurrentHashMap<User, HttpSession>) servletContext.getAttribute(LOGGED_USERS_ATTR);
            HttpSession oldSession = contextSessionCounter.put(user, newSession);
            LOGGER.debug(TWO_PARAMS_MSG, "old session", oldSession);
            if (oldSession != null) {
                @SuppressWarnings("unchecked")
                Map<Integer, Integer> dishIds = ((Map<Integer, Integer>) oldSession.getAttribute(DISH_IDS_LIST_ATTR));
                String lang = (String) oldSession.getAttribute(LANG_ATTR);
                if (dishIds != null && dishIds.size() > 0)
                    newSession.setAttribute(DISH_IDS_LIST_ATTR, dishIds);
                if (lang != null)
                    newSession.setAttribute(LANG_ATTR, lang);
                LOGGER.debug(TWO_PARAMS_MSG, "order exist - " + (dishIds == null), "lang=" + lang);
                LOGGER.debug(TWO_PARAMS_MSG, "old session id=" + oldSession.getId(), "new session id=" + newSession.getId());
                oldSession.invalidate();
            }
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        if (sbe.getName().equals(LOGGED_USER_ATTR)) {
            User user = (User) sbe.getValue();
            HttpSession loggedFromSession = sbe.getSession();
            @SuppressWarnings("unchecked")
            ConcurrentHashMap<User, HttpSession> contextSessionCounter =
                    (ConcurrentHashMap<User, HttpSession>) loggedFromSession.getServletContext().getAttribute(LOGGED_USERS_ATTR);
            LOGGER.debug(USER_LOGOUT_MSG, user.getEmail(), loggedFromSession.getId());
            if (contextSessionCounter.get(user).getId().equals(loggedFromSession.getId()))
                contextSessionCounter.remove(user);
            NewOrderNotifyCommand.checkManagersOnline(contextSessionCounter);
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        attributeAdded(se);
    }
}
