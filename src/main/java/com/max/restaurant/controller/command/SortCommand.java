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
import java.util.Optional;

import static com.max.restaurant.utils.UtilsCommandNames.DISH_LIST_ATTR;
import static com.max.restaurant.utils.UtilsCommandNames.VALUE_ATTR;
import static com.max.restaurant.utils.UtilsFileNames.HOME_PAGE;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;

public class SortCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(SortCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        String value = Optional.ofNullable(request.getParameter(VALUE_ATTR)).orElse("");
        HttpSession session = request.getSession();
        List<Dish> dishes = (List<Dish>) session.getAttribute(DISH_LIST_ATTR);
        if (dishes == null)
            return;
        switch (value) {
            case "name":
                dishes.sort(Comparator.comparing(x -> x.getName()));
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
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        throw new DAOException("You might not be here!");
    }
}
