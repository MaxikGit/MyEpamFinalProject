package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.CommandException;
import com.max.restaurant.exceptions.DAOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.max.restaurant.utils.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.FORWARD;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;

/**
 * Command to change language of a page.<br>
 * The params of request, to call this command: action=lang
 */
public class ChangeLangCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeLangCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        HttpSession session = request.getSession();
        String param = request.getParameter(LANG_ATTR);
        String referer = request.getHeader("referer");
        String cont = request.getContextPath();
        String pageRedir = referer.substring(referer.lastIndexOf(cont));
        String page = referer.substring(referer.lastIndexOf(cont) + cont.length());
        if (param != null){
            switch (param){
                case UKR_ATTR: session.setAttribute(LANG_ATTR, ENG_ATTR);
                break;
                default:  session.setAttribute(LANG_ATTR, UKR_ATTR);
            }
        }
        LOGGER.debug(FORWARD, page);
        response.sendRedirect(pageRedir);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.error(METHOD_STARTS_MSG, "executePost", "true");
        throw new CommandException("You might not be here!");
    }
}
