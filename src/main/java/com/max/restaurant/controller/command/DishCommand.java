package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.CommandException;
import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.services.DishService;
import com.max.restaurant.model.entity.Dish;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.max.restaurant.utils.UtilsCommandNames.DISH_LIST_ATTR;
import static com.max.restaurant.utils.UtilsFileNames.HOME_PAGE;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;
import static com.max.restaurant.utils.UtilsLoggerMsgs.TWO_PARAMS_MSG;

public class DishCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(DishCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        DishService dishService = new DishService();
        List<Dish> dishes = dishService.findAllDishes();
        LOGGER.info(TWO_PARAMS_MSG, DISH_LIST_ATTR,dishes.size() + " items");
        request.getSession().setAttribute(DISH_LIST_ATTR, dishes);
        request.getRequestDispatcher(HOME_PAGE).forward(request, response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException, CommandException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        throw new CommandException("You might not be here!");
    }
}
