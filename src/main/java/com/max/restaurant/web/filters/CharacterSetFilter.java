package com.max.restaurant.web.filters;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.max.restaurant.utils.UtilsLoggerMsgs.ENCODE_MSG;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;

//@WebFilter(urlPatterns = {"*.properties"})
//@WebFilter(urlPatterns = {"/*"})
public class CharacterSetFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterSetFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String encoding = "UTF-8";
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        String encoding = StandardCharsets.UTF_8.name();
        String oldEncoding = response.getCharacterEncoding();
        LOGGER.info(METHOD_STARTS_MSG, "doFilter", oldEncoding);
        if (oldEncoding != null && !oldEncoding.equals(encoding)) {
            LOGGER.debug(ENCODE_MSG, oldEncoding, encoding);
            response.setCharacterEncoding(encoding);

            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }

        chain.doFilter(request, response);
    }
}