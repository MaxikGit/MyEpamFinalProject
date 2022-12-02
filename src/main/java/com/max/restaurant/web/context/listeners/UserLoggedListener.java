package com.max.restaurant.web.context.listeners;

import com.max.restaurant.model.entity.User;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.max.restaurant.utils.UtilsCommandNames.*;

/**
 * Listener that controls the uniqueness of logged user in the app. If the same user will log in from another device,
 * the old session invalidates. Shopping cart (if exist) will be moved to new device.
 */
@WebListener
public class UserLoggedListener implements HttpSessionAttributeListener {

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is added to a session. */
        if (sbe.getName().equals(LOGGED_USER_ATTR)) {
            User user = (User) sbe.getValue();
            HttpSession newSession = sbe.getSession();
            ServletContext servletContext = newSession.getServletContext();
            @SuppressWarnings("unchecked")
            ConcurrentHashMap<Integer, HttpSession> contextSessionCounter =
                    (ConcurrentHashMap<Integer, HttpSession>) servletContext.getAttribute(LOGGED_USERS_ATTR);
            HttpSession oldSession = contextSessionCounter.put(user.getId(), newSession);

            if (oldSession != null) {
                @SuppressWarnings("unchecked")
                Map<Integer, Integer> dishIds = ((Map<Integer, Integer>) oldSession.getAttribute(DISH_IDS_LIST_ATTR));
                String lang = (String) oldSession.getAttribute(LANG_ATTR);
                if ( dishIds != null && dishIds.size() > 0)
                    newSession.setAttribute(DISH_IDS_LIST_ATTR, dishIds);
                if (lang != null)
                    newSession.setAttribute(LANG_ATTR, lang);
                oldSession.invalidate();
            }
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is removed from a session. */

        if (sbe.getName().equals(LOGGED_USER_ATTR)) {
            User user = (User) sbe.getValue();
            HttpSession newSession = sbe.getSession();
            @SuppressWarnings("unchecked")
            ConcurrentHashMap<Integer, HttpSession> contextSessionCounter =
                    (ConcurrentHashMap<Integer, HttpSession>) newSession.getServletContext().getAttribute(LOGGED_USERS_ATTR);
            contextSessionCounter.remove(user.getId());

        }
    }
}
