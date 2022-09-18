package com.max.restaurant.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.max.restaurant.utils.UtilsFileNames.FIRST_START;
import static com.max.restaurant.utils.UtilsLoggerMsgs.FORWARD;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;

@WebFilter(urlPatterns = {"/index.jsp"})
public class StartFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info(METHOD_STARTS_MSG, "doFilter", "true");
        HttpSession session = ((HttpServletRequest)request).getSession();
        if (session.isNew()){
            LOGGER.info(FORWARD, FIRST_START);
            request.getRequestDispatcher(FIRST_START).forward(request, response);
        }
        else chain.doFilter(request, response);
    }
}
