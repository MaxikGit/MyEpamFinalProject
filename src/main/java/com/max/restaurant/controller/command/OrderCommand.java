package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.OrderData;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.Dish;
import com.max.restaurant.model.entity.User;
import com.max.restaurant.model.services.CustomService;
import com.max.restaurant.model.services.DishService;
import com.max.restaurant.utils.UtilsPaginationHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.max.restaurant.utils.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsFileNames.HOME_PAGE;
import static com.max.restaurant.utils.UtilsFileNames.ORDER_PAGE;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * Command that forms order cart, processes users order & registers it in database.
 */
public class OrderCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCommand.class);
    private int recordsPerPage = 3;

    /**
     * Method forms user cart: collects ids of dishes from request parameter (value)
     * {@link com.max.restaurant.utils.UtilsCommandNames#VALUE_ATTR}, then passes it to order page (if value == null)
     * @param request {@link HttpServletRequest} object
     * @param response {@link HttpServletResponse} object
     * @throws ServletException
     * @throws IOException
     * @throws DAOException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        String value = request.getParameter(VALUE_ATTR);
        LOGGER.debug(TWO_PARAMS_MSG, VALUE_ATTR, value);
        String page;
        HttpSession session = request.getSession(false);
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> dishIds = ((Map<Integer, Integer>) session.getAttribute(DISH_IDS_LIST_ATTR));
        if (value == null) {
            DishService dishService = new DishService();
            Map<Dish, Integer> orderedDishes = dishService.findDishesById(dishIds);

            UtilsPaginationHelper.paginationCounter(request, orderedDishes.size(), recordsPerPage);

            double totalCost = orderedDishes.entrySet().stream()
                    .mapToDouble(x-> (x.getKey().getPrice() * x.getValue()) ).sum();

            session.setAttribute(ORDER_MAP_ATTR, orderedDishes);
            session.setAttribute(ORDER_TOTAL_COST_ATTR, totalCost);
            page = ORDER_PAGE;
            LOGGER.info(FORWARD, page);
            request.getRequestDispatcher(page).forward(request, response);
        }
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
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    /**
     * Method processes changes made by user in his cart. If order is accepted, then register it in
     * database and in servlet context
     * @param request {@link HttpServletRequest} object
     * @param response {@link HttpServletResponse} object
     * @throws ServletException ServletException object
     * @throws IOException IOException object
     * @throws DAOException DAOException object
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        String page;
        HttpSession session = request.getSession();

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> dishIds = (Map<Integer, Integer>) session.getAttribute(DISH_IDS_LIST_ATTR);
        @SuppressWarnings("unchecked")
        Map<Dish, Integer> orderedDishes = (Map<Dish, Integer>) session.getAttribute(ORDER_MAP_ATTR);

        for (Map.Entry<Dish, Integer> entry : orderedDishes.entrySet()) {
            String countParam = request.getParameter(QUANTITY_ATTR + entry.getKey().getId());
            if (countParam == null)
                continue;
            int dishCount = Integer.parseInt(countParam);
            entry.setValue(dishCount);
            dishIds.replace(entry.getKey().getId(), entry.getValue());
        }
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
            placeNewOrder(currentUser.getId(), orderedDishes, request);
            List<OrderData> orders = getOrdersPending(currentUser.getId());
            session.setAttribute(CUSTOM_LIST_ATTR, orders);
            removeAttributes(session);
        }
        LOGGER.debug(REDIRECT, page);
        response.sendRedirect(page);
    }

    private void placeNewOrder(int userId, Map<Dish, Integer> orderedDishes, HttpServletRequest request) throws DAOException {
        CustomService customService = new CustomService();
        Custom newCustom = customService.getNewCustom(userId, orderedDishes);
        @SuppressWarnings("unchecked")
        Map<Integer, Object> map = (Map<Integer, Object>) request.getServletContext().getAttribute(NEW_ORDERS_ATTR);
        map.put(newCustom.getId(), "null");
    }

    private List<OrderData> getOrdersPending(int userId) throws DAOException {
        CustomService service = new CustomService();
        List<Custom> customList = service.getUsersCustomsInProgress(userId);
        List<OrderData> ordersList = OrderData.getOrderDataList(customList);
        return ordersList;
    }

    private static void removeAttributes(HttpSession session) {
        LOGGER.info(METHOD_STARTS_MSG, "removeAttributes", "true");
        session.removeAttribute(ORDER_TOTAL_COST_ATTR);
        session.removeAttribute(ORDER_MAP_ATTR);
        session.removeAttribute(DISH_IDS_LIST_ATTR);
        session.removeAttribute(PAGES_MIN_ATTR);
        session.removeAttribute(PAGES_MAX_ATTR);
        session.removeAttribute(PAGE_ATTR);
        session.removeAttribute(TOTAL_PAGES_ATTR);
    }
}
