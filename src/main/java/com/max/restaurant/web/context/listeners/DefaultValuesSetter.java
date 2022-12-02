package com.max.restaurant.web.context.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;

import java.util.concurrent.ConcurrentHashMap;

import static com.max.restaurant.utils.UtilsCommandNames.*;

@WebListener
public class DefaultValuesSetter implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(LANG_ATTR, UKR_ATTR);
        sce.getServletContext().setAttribute(LOGGED_USERS_ATTR, new ConcurrentHashMap<Integer, HttpSession>());
    }
}
