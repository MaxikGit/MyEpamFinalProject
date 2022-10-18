package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.OrderData;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.CustomHasDish;
import com.max.restaurant.model.entity.Dish;
import com.max.restaurant.model.services.CustomHasDishService;
import com.max.restaurant.model.services.CustomService;
import com.max.restaurant.utils.UtilsPaginationHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import static com.max.restaurant.utils.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsFileNames.EDIT_ORDER_MANAGEMENT_PAGE;
import static com.max.restaurant.utils.UtilsFileNames.ORDER_MANAGEMENT_PAGE;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

public class ManagerOrderEditingCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerOrderEditingCommand.class);
    private int recordsPerPage = 3;

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        String customId = request.getParameter(VALUE_ATTR);
//        String dishIdToDel = request.getParameter(DEL_FROM_ORDER_ATTR);
        String page = request.getContextPath() + EDIT_ORDER_MANAGEMENT_PAGE;
        HttpSession session = request.getSession();

//        if (dishIdToDel != null && customId == null) {
//            OrderData orderData = (OrderData) session.getAttribute(MANAGEMENT_ORDERDATA_ATTR);
//            CustomHasDishService hasDishService = new CustomHasDishService();
//            CustomHasDish customHasDish = hasDishService.findByCustomIdDishId(
//                    orderData.getCustom().getId(), Integer.parseInt(dishIdToDel));
//            boolean deleted = hasDishService.deleteTransactional(customHasDish);
//            if (!deleted) {
//                LOGGER.info(REDIRECT, page);
//                response.sendRedirect(page);
//                return;
//            }
//            customId = String.valueOf(orderData.getCustom().getId());
//        }
        CustomService customService = new CustomService();
        Custom custom = customService.findCustomById(Integer.parseInt(customId));
        OrderData orderData = new OrderData(custom);
        UtilsPaginationHelper.paginationCounter(request, orderData.getDishes().size(), recordsPerPage);

        session.setAttribute(MANAGEMENT_ORDERDATA_ATTR, orderData);
        LOGGER.debug(TWO_PARAMS_MSG, MANAGEMENT_ORDERDATA_ATTR, orderData);

        LOGGER.info(REDIRECT, page);
        response.sendRedirect(page);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        HttpSession session = request.getSession();
        String page;
        String customId = request.getParameter(VALUE_ATTR);
        String dishIdToDel = request.getParameter(DEL_FROM_ORDER_ATTR);
        OrderData orderData = (OrderData) session.getAttribute(MANAGEMENT_ORDERDATA_ATTR);
        //update quantities
        boolean updated = false;
        for (Map.Entry<Dish, Integer> entry : orderData.getDishes().entrySet()) {
            String newQuantity = request.getParameter(QUANTITY_ATTR + entry.getKey().getId());
            if (newQuantity == null)
                continue;
            int dishCount = Integer.parseInt(newQuantity);
            updated = dishCount == entry.setValue(dishCount) || updated;
        }
        //not accepted
        if (request.getParameter(ORDER_ACCEPT_ATTR) == null) {
            //delete dishes
            if (dishIdToDel != null && customId == null) {
                CustomHasDishService hasDishService = new CustomHasDishService();
                CustomHasDish customHasDish = hasDishService.findByCustomIdDishId(
                        orderData.getCustom().getId(), Integer.parseInt(dishIdToDel));
                hasDishService.deleteTransactional(customHasDish);
                updated = true;
            }
            page = request.getContextPath() + EDIT_ORDER_MANAGEMENT_PAGE;
        }
        //accepted
        else {
            page = request.getContextPath() + ORDER_MANAGEMENT_PAGE;
            LOGGER.info(REDIRECT, page);
            response.sendRedirect(page);
            return;
        }
        if (updated){
            CustomService customService = new CustomService();
            Custom custom = orderData.getCustom();
            customService.updateTransaction(custom, orderData.getDishes());
            custom = customService.findCustomById(custom.getId());
            orderData = new OrderData(custom);
        }
        UtilsPaginationHelper.paginationCounter(request, orderData.getDishes().size(), recordsPerPage);
        session.setAttribute(MANAGEMENT_ORDERDATA_ATTR, orderData);
        LOGGER.info(REDIRECT, page);
        response.sendRedirect(page);
    }
}
