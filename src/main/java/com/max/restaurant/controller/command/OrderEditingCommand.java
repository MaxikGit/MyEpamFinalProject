package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.dao.services.CustomService;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.Dish;
import com.max.restaurant.model.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.max.restaurant.controller.command.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsFileNames.HOME_PAGE;
import static com.max.restaurant.utils.UtilsFileNames.ORDER_PAGE;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

public class OrderEditingCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEditingCommand.class);


    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        String value = request.getParameter(DEL_FROM_ORDER_ATTR);
        LOGGER.debug(TWO_PARAMS_MSG, DEL_FROM_ORDER_ATTR, value);
        String page;
        HttpSession session = request.getSession();
        Map<Dish, Integer> orderedDishes = (Map<Dish, Integer>) session.getAttribute(ORDER_MAP_ATTR);
        List<Integer> dishIds = (List<Integer>) session.getAttribute(DISH_IDS_LIST_ATTR);

        Dish dishToRemove = getDishById(orderedDishes, Integer.parseInt(value));
        dishIds.remove(dishIds.lastIndexOf(Integer.parseInt(value)));
        orderedDishes.remove(dishToRemove);
        if (orderedDishes.size() == 0) {
            page = request.getContextPath() + HOME_PAGE;
            removeAttributes(session);
        } else {
            double totalCost = orderedDishes.entrySet().stream()
                    .mapToDouble(x -> (x.getKey().getPrice() * x.getValue())).sum();
            session.setAttribute(ORDER_TOTAL_COST_ATTR, totalCost);
            session.setAttribute(DISH_IDS_LIST_ATTR, dishIds);
            session.setAttribute(ORDER_MAP_ATTR, orderedDishes);
            LOGGER.debug(TWO_PARAMS_MSG, ORDER_TOTAL_COST_ATTR, totalCost);
            LOGGER.debug(TWO_PARAMS_MSG, DISH_IDS_LIST_ATTR, dishIds);
            LOGGER.debug(TWO_PARAMS_MSG, ORDER_MAP_ATTR, orderedDishes);
            page = request.getContextPath() + ORDER_PAGE;
        }
        LOGGER.info(REDIRECT, page);
        response.sendRedirect(page);
    }


    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        String page = request.getContextPath() + HOME_PAGE;
        HttpSession session = request.getSession();
        //receiving custom with id
        User currentUser = (User) session.getAttribute(LOGGED_USER_ATTR);
        Map<Dish, Integer> orderedDishes = (Map<Dish, Integer>) session.getAttribute(ORDER_MAP_ATTR);
        for (Map.Entry<Dish, Integer> entry : orderedDishes.entrySet()) {
            int dishCount = Integer.parseInt(request.getParameter(QUANTITY_ATTR + entry.getKey().getId()));
            entry.setValue(dishCount);
        }
        CustomService customService = new CustomService();
        Custom newCustom = customService.getNewCustom(currentUser.getId(), orderedDishes);

        /*
        this part I want to use to show progress bar of Customers orders "in work"
        this still have no implementation on pages
        */
        List<Custom> customList = (List<Custom>) session.getAttribute(CUSTOM_LIST_ATTR);
        if (customList == null)
            customList = new ArrayList<>();
        customList.add(newCustom);
        session.setAttribute(CUSTOM_LIST_ATTR, customList);

        removeAttributes(session);
        response.sendRedirect(page);
    }

    private static void removeAttributes(HttpSession session) {
        session.removeAttribute(ORDER_TOTAL_COST_ATTR);
        session.removeAttribute(ORDER_MAP_ATTR);
        session.removeAttribute(DISH_IDS_LIST_ATTR);
        LOGGER.debug(DELETE_MSG, ORDER_TOTAL_COST_ATTR);
        LOGGER.debug(DELETE_MSG, ORDER_MAP_ATTR);
        LOGGER.debug(DELETE_MSG, DISH_IDS_LIST_ATTR);
    }

    private Dish getDishById(Map<Dish, Integer> map, int id) {
        return map.entrySet().stream()
                .filter(dishEntry -> dishEntry.getKey().getId() == id)
                .map(x -> x.getKey()).findFirst().orElse(null);
    }
}
