package com.max.restaurant.filters;

import com.max.restaurant.model.entity.Category;
import com.max.restaurant.model.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.max.restaurant.controller.command.UtilsCommandNames.CATEGORY_LIST_ATTR;
import static com.max.restaurant.utils.UtilsFileNames.FIRST_START;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

@WebFilter(urlPatterns = {"/*"})
public class StartFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info(METHOD_STARTS_MSG, "doFilter", "true");
        HttpSession session = ((HttpServletRequest)request).getSession();
        List<Category> category = (List<Category>) session.getAttribute(CATEGORY_LIST_ATTR);
//        User user = (User) session.getAttribute(LOGGED_USER_ATTR)
        if (session.isNew() || category == null){// || user == null){
            LOGGER.debug(TWO_PARAMS_MSG, ("session is new=" + session.isNew()), "category is null=" + (category==null) );
            LOGGER.info(FORWARD, FIRST_START);
            request.getRequestDispatcher(FIRST_START).forward(request, response);
        }
        else {
            LOGGER.debug(TWO_PARAMS_MSG, ("session is new=" + session.isNew()), "category is null=" + false);
            chain.doFilter(request, response);
        }
    }
}
