package com.max.restaurant.web.context.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import static com.max.restaurant.controller.command.UtilsCommandNames.LANG_ATTR;
import static com.max.restaurant.controller.command.UtilsCommandNames.UKR_ATTR;

@WebListener
public class DefaultLanguageSetter implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(LANG_ATTR, UKR_ATTR);
    }
}
