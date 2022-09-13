package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.Dish;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static com.max.restaurant.controller.command.UtilsCommandNames.DISH_LIST_ATTR;
import static com.max.restaurant.controller.command.UtilsCommandNames.VALUE_ATTR;
import static com.max.restaurant.utils.UtilsFileNames.HOME_PAGE;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD;

public class SortCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(SortCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD, "executeGet", "true");
        String value = request.getParameter(VALUE_ATTR);
        HttpSession session = request.getSession();
        List<Dish> dishes = (List<Dish>) session.getAttribute(DISH_LIST_ATTR);
        if (dishes == null)
            return;
        switch (value) {
            case "name":
                dishes.sort((x, y) -> (CharSequence.compare(x.getName(), y.getName())));
                break;
            case "cost":
                dishes.sort(Comparator.comparingDouble(Dish::getPrice));
                break;
            case "category":
                dishes.sort(Comparator.comparingInt(Dish::getCategoryId));
                break;
        }
        session.setAttribute(DISH_LIST_ATTR, dishes);
        request.getRequestDispatcher(HOME_PAGE).forward(request, response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws DAOException {
        LOGGER.info(METHOD, "executePost", "true");
        throw new DAOException("You might not be here!");
    }
}
