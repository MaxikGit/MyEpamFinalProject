package com.max.restaurant.filters;

import com.max.restaurant.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.max.restaurant.controller.command.UtilsCommandNames.LOGGED_USER_ATTR;
import static com.max.restaurant.utils.UtilsFileNames.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

@WebFilter(urlPatterns = {"/views/*"})
public class AuthenticationFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
    private int managerId = 1;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info(METHOD_STARTS_MSG, "doFilter", "true");
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = ((HttpServletRequest) request).getRequestURI();
        boolean isImage = 0 < uri.lastIndexOf("images");
        boolean isCSS = 0 < uri.lastIndexOf("styles");
        boolean isIcon = 0 < uri.lastIndexOf("icons");
        boolean isHome = uri.equals(((HttpServletRequest) request).getContextPath() + HOME_PAGE);

        if (isHome || isImage || isCSS || isIcon) {
            chain.doFilter(request, response);
        } else {
            boolean isNotLoginPage = !uri.endsWith(LOGIN_PAGE);
            boolean isNotSignUpPage = !uri.endsWith(SIGN_UP_PAGE);
            boolean isManagersPage = uri.endsWith(EDIT_ORDER_MANAGEMENT_PAGE) || uri.endsWith(ORDER_MANAGEMENT_PAGE);
            String page = ((HttpServletRequest) request).getContextPath() + HOME_PAGE;
            if ( (session == null || session.getAttribute(LOGGED_USER_ATTR) == null)
                    && (isNotLoginPage || isNotSignUpPage) )
            {
                LOGGER.info(UNAUTH_MSG, session.getAttribute(LOGGED_USER_ATTR));
                LOGGER.debug(REDIRECT, page);
                resp.sendRedirect(page);
            } else if (isManagersPage && (( (User)session.getAttribute(LOGGED_USER_ATTR) ).getRoleId() != managerId) ) {
                resp.sendRedirect(page);
            }
            else chain.doFilter(request, response);
        }
    }
}