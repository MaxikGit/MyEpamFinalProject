package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.OrderData;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.CustomHasDish;
import com.max.restaurant.model.entity.Dish;
import com.max.restaurant.model.services.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import static com.max.restaurant.controller.command.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsFileNames.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

public class ManagerOrderEditingCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerOrderEditingCommand.class);


    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        String customId = request.getParameter(VALUE_ATTR);
        String dishIdToDel = request.getParameter(DEL_FROM_ORDER_ATTR);
        String page = request.getContextPath() + EDIT_ORDER_MANAGEMENT_PAGE;
        HttpSession session = request.getSession();

        if (dishIdToDel != null && customId == null) {
            OrderData orderData = (OrderData)session.getAttribute(MANAGEMENT_ORDERDATA_ATTR);
            CustomHasDishService hasDishService = new CustomHasDishService();
            CustomHasDish customHasDish = hasDishService.findByCustomIdDishId(
                    orderData.getCustom().getId(), Integer.parseInt(dishIdToDel));
            boolean deleted = hasDishService.deleteTransactional(customHasDish);
            if (!deleted){
                LOGGER.info(REDIRECT, page);
                response.sendRedirect(page);
                return;
            }
            customId = String.valueOf(orderData.getCustom().getId());
        }
        CustomService customService = new CustomService();
        Custom custom = customService.findCustomById(Integer.parseInt(customId));
        OrderData orderData = new OrderData(custom);

        session.setAttribute(MANAGEMENT_ORDERDATA_ATTR, orderData);
        LOGGER.debug(TWO_PARAMS_MSG, MANAGEMENT_ORDERDATA_ATTR, orderData);

        LOGGER.info(REDIRECT, page);
        response.sendRedirect(page);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        HttpSession session = request.getSession();
        OrderData orderData = (OrderData) session.getAttribute(MANAGEMENT_ORDERDATA_ATTR);
        for (Map.Entry<Dish, Integer> entry : orderData.getDishes().entrySet()) {
            int dishCount = Integer.parseInt(request.getParameter(QUANTITY_ATTR + entry.getKey().getId()));
            entry.setValue(dishCount);
        }

        CustomService customService = new CustomService();
        Custom custom = orderData.getCustom();
        customService.updateTransaction(custom, orderData.getDishes());
        custom = customService.findCustomById(custom.getId());
        orderData = new OrderData(custom);

        session.setAttribute(MANAGEMENT_ORDERDATA_ATTR, orderData);
        String page = request.getContextPath() + EDIT_ORDER_MANAGEMENT_PAGE;

        response.sendRedirect(page);
    }

}
