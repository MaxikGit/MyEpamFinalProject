package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.dao.services.DishService;
import com.max.restaurant.model.entity.Dish;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.max.restaurant.controller.command.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsFileNames.HOME_PAGE;
import static com.max.restaurant.utils.UtilsFileNames.ORDER_PAGE;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

public class OrderCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCommand.class);


    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD, "executeGet", "true");
        String value = request.getParameter(VALUE_ATTR);
        LOGGER.debug(TWO_PARAMS_MSG, VALUE_ATTR, value);
        String page;
        HttpSession session = request.getSession();
        List<Integer> dishIds = ((List<Integer>) session.getAttribute(DISH_IDS_LIST_ATTR));
        //value == null -> pressed "shopping cart"
        if (value == null) {
            DishService dishService = new DishService();
            List<Dish> orderedDishes = new ArrayList<>();
            for (int dishId : dishIds){
                orderedDishes.add(dishService.findDishById(dishId));
            }
            Collections.sort(orderedDishes, Comparator.comparingInt(Dish::getId));
            session.setAttribute(ORDER_LIST_ATTR, orderedDishes);
            double totalCost = orderedDishes.stream().mapToDouble(Dish::getPrice).sum();
            session.setAttribute(ORDER_TOTAL_COST_ATTR, totalCost);
            page = request.getContextPath() + ORDER_PAGE;
        }
        //else -> choosing dishes
        else {
            if (dishIds == null)
                dishIds = new ArrayList<>();
            dishIds.add(Integer.parseInt(value));
            session.setAttribute(DISH_IDS_LIST_ATTR, dishIds);
            LOGGER.debug(TWO_PARAMS_MSG, DISH_IDS_LIST_ATTR, dishIds);
            page = request.getContextPath() + HOME_PAGE;
        }
        LOGGER.info(REDIRECT, page);
        response.sendRedirect(page);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD, "executePost", "true");

//        CustomService customService = new CustomService();
//        HttpSession session = request.getSession();
//        List<Custom> customs = (List<Custom>) session.getAttribute(ORDER_LIST_ATTR);
//        if (customs == null) {
//            User currentUser = (User) session.getAttribute(LOGGED_USER_ATTR);
//            customs = customService.findCustomsByUserId(currentUser.getId());
//            if (customs == null)
//                customs = new ArrayList<>();
//        }
//        Custom custom = new Custom();
//        CustomHasDish customHasDish = new CustomHasDish();
//        LOGGER.info(TWO_PARAMS, ORDER_LIST_ATTR, customs.size() + " items");
//        session.setAttribute(ORDER_LIST_ATTR, customs);
//        request.getRequestDispatcher(ORDER_PAGE).forward(request, response);
    }
}
