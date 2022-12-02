package com.max.restaurant.web.filters;

import com.max.restaurant.model.entity.Category;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.max.restaurant.utils.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * filter that loads data to be displayed on the home page, if it`s the first call to app. Additionally, this filter changes
 * this app default language if it does not match the browser language
 */
@WebFilter(urlPatterns = {"/index.jsp"})
public class StartFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info(METHOD_STARTS_MSG, "doFilter", "true");
        HttpSession session = ((HttpServletRequest) request).getSession();
        Locale locale = request.getLocale();
        if (locale.getLanguage().equalsIgnoreCase(ENG_ATTR)) {
            request.getServletContext().setAttribute(LANG_ATTR, ENG_ATTR);
        }
        @SuppressWarnings("unchecked")
        List<Category> category = (List<Category>) session.getAttribute(CATEGORY_LIST_ATTR);
        if (category == null) {// || user == null){
            LOGGER.debug(TWO_PARAMS_MSG, ("session is new=" + session.isNew()), "category is null=true");
            LOGGER.info(FORWARD, FIRST_START);
            request.getRequestDispatcher(FIRST_START).forward(request, response);
        } else {
            LOGGER.debug(TWO_PARAMS_MSG, ("session is new=" + session.isNew()), "category is null=false");
            chain.doFilter(request, response);
        }
    }
}
