package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.dao.services.CategoryService;
import com.max.restaurant.model.dao.services.DishService;
import com.max.restaurant.model.entity.Category;
import com.max.restaurant.model.entity.Dish;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.max.restaurant.controller.command.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsFileNames.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

public class CategoryCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD, "executeGet", "true");
        String value = request.getParameter(VALUE_ATTR);
        HttpSession session = request.getSession();
        List<Dish> dishes;
        if (value == null) {
            CategoryService categoryService = new CategoryService();
            List<Category> category = categoryService.findAllCategories();
            Collections.sort(category, Comparator.comparingInt(Category::getId));
            session.setAttribute(CATEGORY_LIST_ATTR, category);
            LOGGER.debug(TWO_PARAMS_MSG, CATEGORY_LIST_ATTR, category);
            DishService dishService = new DishService();
            dishes = dishService.findAllDishes();
        }else {
            DishService dishService = new DishService();
            dishes = dishService.findDishByCategoryId(value);
        }
        Collections.sort(dishes, Comparator.comparingInt(Dish::getCategoryId));
        session.setAttribute(DISH_LIST_ATTR, dishes);
        LOGGER.debug(TWO_PARAMS_MSG, VALUE_ATTR + request.getParameter(VALUE_ATTR), dishes.size() + " items");
        LOGGER.info(FORWARD, HOME_PAGE);
        request.getRequestDispatcher(HOME_PAGE).forward(request, response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws DAOException {
        LOGGER.info(METHOD, "executePost", "true");
        throw new DAOException("You might not be here!");
    }
}
