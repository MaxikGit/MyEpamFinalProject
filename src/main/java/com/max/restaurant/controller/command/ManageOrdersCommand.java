package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.CommandException;
import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.OrderData;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.Status;
import com.max.restaurant.model.services.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.max.restaurant.controller.command.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsFileNames.HOME_PAGE;
import static com.max.restaurant.utils.UtilsFileNames.ORDER_MANAGEMENT_PAGE;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

public class ManageOrdersCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(SortCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        CustomService customService = new CustomService();
        StatusService statusService = new StatusService();
        HttpSession session = request.getSession();

        String value = request.getParameter(DEL_FROM_ORDER_ATTR);
        if (value != null)
            customService.deleteCustom(Integer.parseInt(value));

        List<Custom> customList;
        String inProgress = request.getParameter(INPROGRESS_ATTR);
        if (inProgress == null ){
            customList = customService.getCustomsInProgress();
            session.removeAttribute(INPROGRESS_ATTR);
        }else if (inProgress.equals("false")){
            customList = customService.getCustomsCompleted();
            session.setAttribute(INPROGRESS_ATTR, "false");
        }else throw new CommandException("Wrong inProgress attr");


        List<OrderData> orderDataList = new ArrayList<>();
        double totalCost = 0;
        for (Custom cu : customList) {
            OrderData orderData = new OrderData(cu);
            orderDataList.add(orderData);
            totalCost += cu.getCost();
        }
        List<Status> statuses = statusService.findAll();
        session.setAttribute(ORDER_TOTAL_INPROGRESS_ATTR, totalCost);
        session.setAttribute(STATUS_LIST_ATTR, statuses);
        session.setAttribute(MANAGEMENT_ORDERDATA_LIST_ATTR, orderDataList);
        session.setAttribute(MANAGEMENT_ORDERDATA_LIST_ATTR, orderDataList);
        request.getRequestDispatcher(ORDER_MANAGEMENT_PAGE).forward(request, response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        String[] statusesSelected = request.getParameterValues(STATUS_ATTR);
        String page;
        if (statusesSelected == null || statusesSelected.length == 0) {
            page = request.getContextPath() + HOME_PAGE;
            LOGGER.info(REDIRECT, page);
            response.sendRedirect(page);
            return;
        } else {
            LOGGER.debug(TWO_PARAMS_MSG, "statusID & customID", statusesSelected);
            CustomService customService = new CustomService();
            for (String params : statusesSelected) {
                String[] statusCustomParams = params.split(" ");
                int statusId = Integer.parseInt(statusCustomParams[0]);
                int customId = Integer.parseInt(statusCustomParams[1]);
                Custom custom = customService.findCustomById(customId);
                custom.setStatusId(statusId);
                customService.update(custom);
            }
        }
        executeGet(request, response);
    }
}
