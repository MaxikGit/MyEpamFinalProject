package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.services.CustomService;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.Dish;
import com.max.restaurant.model.entity.User;
import com.max.restaurant.model.services.DishService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.max.restaurant.controller.command.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsFileNames.HOME_PAGE;
import static com.max.restaurant.utils.UtilsFileNames.ORDER_PAGE;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

public class OrderEditingCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEditingCommand.class);
    private int recordsPerPage = 3;

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        String value = request.getParameter(VALUE_ATTR);
        LOGGER.debug(TWO_PARAMS_MSG, VALUE_ATTR, value);
        String page;
        HttpSession session = request.getSession();
        Map<Integer, Integer> dishIds = ((Map<Integer, Integer>) session.getAttribute(DISH_IDS_LIST_ATTR));
        //value == null -> pressed "shopping cart"
        if (value == null) {
            DishService dishService = new DishService();
            Map<Dish, Integer> orderedDishes = dishService.findDishesById(dishIds);
            session.setAttribute(ORDER_MAP_ATTR, orderedDishes);

            UtilsPaginationHelper.paginationCounter(request, orderedDishes.size(), recordsPerPage);

            double totalCost = orderedDishes.entrySet().stream()
                    .mapToDouble(x-> (x.getKey().getPrice() * x.getValue()) ).sum();

            session.setAttribute(ORDER_TOTAL_COST_ATTR, totalCost);
            page = ORDER_PAGE;
            LOGGER.info(FORWARD, page);
            request.getRequestDispatcher(page).forward(request, response);
        }
        //else -> choosing dishes
        else {
            if (dishIds == null)
                dishIds = new HashMap<>();
            Integer dishKey = Integer.parseInt(value);
            Integer mapValue = dishIds.put(dishKey, 1);
            if (mapValue != null)
                dishIds.put(dishKey, mapValue + 1);
            session.setAttribute(DISH_IDS_LIST_ATTR, dishIds);
            LOGGER.debug(TWO_PARAMS_MSG, DISH_IDS_LIST_ATTR, dishIds);
            page = request.getContextPath() + HOME_PAGE;
            LOGGER.info(REDIRECT, page);
            response.sendRedirect(page);
        }
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        String page;
        HttpSession session = request.getSession();

        Map<Integer, Integer> dishIds = (Map<Integer, Integer>) session.getAttribute(DISH_IDS_LIST_ATTR);
        Map<Dish, Integer> orderedDishes = (Map<Dish, Integer>) session.getAttribute(ORDER_MAP_ATTR);

        //if order not accepted
        if (request.getParameter(ORDER_ACCEPT_ATTR) == null)
        {
            //if removing from order dish
            String value = request.getParameter(DEL_FROM_ORDER_ATTR);
            LOGGER.debug(TWO_PARAMS_MSG, DEL_FROM_ORDER_ATTR, value);
            if (value != null) {
                DishService dishService = new DishService();
                Dish dishToRemove = dishService.getDishByIdFromMap(orderedDishes, Integer.parseInt(value));
                dishIds.remove(dishToRemove.getId());
                orderedDishes.remove(dishToRemove);
            }
            if (orderedDishes.size() == 0) {
                page = request.getContextPath() + HOME_PAGE;
                removeAttributes(session);
            } else {
                for (Map.Entry<Dish, Integer> entry : orderedDishes.entrySet()) {
                    String countParam = request.getParameter(QUANTITY_ATTR + entry.getKey().getId());
                    if (countParam == null)
                        continue;
                    int dishCount = Integer.parseInt(countParam);
                    entry.setValue(dishCount);
                    dishIds.replace(entry.getKey().getId(), entry.getValue());
                }
                UtilsPaginationHelper.paginationCounter(request, orderedDishes.size(), recordsPerPage);
                page = request.getContextPath() + ORDER_PAGE;

                double totalCost = orderedDishes.entrySet().stream()
                        .mapToDouble(x -> (x.getKey().getPrice() * x.getValue())).sum();
                session.setAttribute(ORDER_TOTAL_COST_ATTR, totalCost);
                session.setAttribute(DISH_IDS_LIST_ATTR, dishIds);
                session.setAttribute(ORDER_MAP_ATTR, orderedDishes);
                LOGGER.debug(TWO_PARAMS_MSG, ORDER_TOTAL_COST_ATTR, totalCost);
                LOGGER.debug(TWO_PARAMS_MSG, DISH_IDS_LIST_ATTR, dishIds);
                LOGGER.debug(TWO_PARAMS_MSG, ORDER_MAP_ATTR, orderedDishes);
            }
            //if order accepted - put it to db n clean after
        } else {
            page = request.getContextPath() + HOME_PAGE;
            User currentUser = (User) session.getAttribute(LOGGED_USER_ATTR);
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
        }
        LOGGER.debug(REDIRECT, page);
        response.sendRedirect(page);
    }

    private static void removeAttributes(HttpSession session) {
        LOGGER.info(METHOD_STARTS_MSG, "removeAttributes", "true");
        session.removeAttribute(ORDER_TOTAL_COST_ATTR);
        session.removeAttribute(ORDER_MAP_ATTR);
        session.removeAttribute(DISH_IDS_LIST_ATTR);
        session.removeAttribute(PAGES_MIN_ATTR);
        session.removeAttribute(PAGES_MAX_ATTR);
        session.removeAttribute(PAGE_ATTR);
        session.removeAttribute(RECS_PER_PAGE_ATTR);
    }
}
