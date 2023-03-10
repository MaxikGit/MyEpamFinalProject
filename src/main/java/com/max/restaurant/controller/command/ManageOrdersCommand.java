package com.max.restaurant.controller.command;

import com.itextpdf.text.DocumentException;
import com.max.restaurant.exceptions.CommandException;
import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.OrderData;
import com.max.restaurant.model.entity.Custom;
import com.max.restaurant.model.entity.Status;
import com.max.restaurant.model.services.CustomService;
import com.max.restaurant.model.services.StatusService;
import com.max.restaurant.utils.UtilsEmailNotificator;
import com.max.restaurant.utils.UtilsPDFReportCreator;
import com.max.restaurant.utils.UtilsPaginationHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static com.max.restaurant.utils.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsExceptionMsgs.EMAIL_EXC;
import static com.max.restaurant.utils.UtilsExceptionMsgs.PDF_EXC;
import static com.max.restaurant.utils.UtilsFileNames.HOME_PAGE;
import static com.max.restaurant.utils.UtilsFileNames.ORDER_MANAGEMENT_PAGE;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * This command is used to view lists of orders and to change the order's status.<br>
 * Orders can be deleted or sent to edit<br>
 * It can be used only by registered managers<br>
 * The params of request, to call this command: action=management
 */
public class ManageOrdersCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(SortCommand.class);
    private int recordsPerPage = 4;
    private final static int newStatus = 1;
    private final static String bundleFileName = "messages";

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executeGet", "true");
        StatusService statusService = new StatusService();
        HttpSession session = request.getSession();

        List<Custom> customList = getCustomsByLabel(request);
        UtilsPaginationHelper.paginationCounter(request, customList.size(), recordsPerPage);

        List<OrderData> orderDataList = new ArrayList<>();
        double totalCost = 0;
        for (Custom cu : customList) {
            OrderData orderData = new OrderData(cu);
            orderDataList.add(orderData);
            totalCost += cu.getCost();
        }
        String sortingParam = getSortingParam(request);
        sortOrder(orderDataList, sortingParam);

        List<Status> statuses = statusService.findAll();
        session.setAttribute(SORT_ATTR, sortingParam);
        session.setAttribute(ORDER_TOTAL_INPROGRESS_ATTR, totalCost);
        session.setAttribute(STATUS_LIST_ATTR, statuses);
        session.setAttribute(MANAGEMENT_ORDERDATA_LIST_ATTR, orderDataList);
        session.setAttribute(MANAGEMENT_ORDERDATA_LIST_ATTR, orderDataList);
        request.getRequestDispatcher(ORDER_MANAGEMENT_PAGE).forward(request, response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        String page;
        String[] statusesSelected = request.getParameterValues(STATUS_ATTR);
        boolean updated = (statusesSelected != null && statusesSelected.length > 0);

        if (updated) {
            CustomService customService = new CustomService();
            LOGGER.debug(TWO_PARAMS_MSG, "statusID & customID", statusesSelected);
            updateOrderStatuses(customService, statusesSelected, request);
        }
        //if orders not accepted
        if (request.getParameter(ORDER_ACCEPT_ATTR) == null) {
            String value = request.getParameter(DEL_FROM_ORDER_ATTR);
            //del button pressed
            if (value != null) {
                CustomService customService = new CustomService();
                Custom custom = customService.findCustomById(Integer.parseInt(value));
                if (custom.getStatusId() == newStatus)
                    removeNewOrderFromContext(request, custom.getId());
                customService.deleteCustom(custom.getId());
                page = MANAGEMENT_COMM;
            }
            //page button pressed
            else {
//                if (updated) {
//                    page = MANAGEMENT_COMM;
//                } else {
                    List<Custom> customList = getCustomsByLabel(request);
                    UtilsPaginationHelper.paginationCounter(request, customList.size(), recordsPerPage);
                    int currPageNum = Integer.parseInt(Optional.ofNullable(request.getParameter(PAGE_ATTR)).orElse("1"));
//                    return;
                    page = MANAGEMENT_COMM + "&" + PAGE_ATTR + "=" + currPageNum;
//                }
            }
        }
        //if orders accepted
        else {
            if (updated) {
                page = MANAGEMENT_COMM;
            } else {
                page = HOME_PAGE;
                LOGGER.info(FORWARD, page);
                request.getRequestDispatcher(page).forward(request, response);
                return;
            }
        }
        response.sendRedirect(page);
    }

    private static void updateOrderStatuses(CustomService customService, String[] statusesSelected,
                                            HttpServletRequest request) throws DAOException {
        int completedStatus = 4;
        for (String params : statusesSelected) {
            if (params == null)
                continue;
            String[] statusCustomParams = params.split(" ");
            int statusId = Integer.parseInt(statusCustomParams[0]);
            int customId = Integer.parseInt(statusCustomParams[1]);
            Custom custom = customService.findCustomById(customId);
            int oldStatus = custom.getStatusId();
            custom.setStatusId(statusId);
            customService.update(custom);

            if (statusId == completedStatus) {
                OrderData orderData = new OrderData(custom);
                sendPDFReportToUser(orderData, request);
            }
            else if (oldStatus == newStatus){
                removeNewOrderFromContext(request, custom.getId());
            }
        }
    }

    private static void removeNewOrderFromContext(HttpServletRequest request, int customId) {
        @SuppressWarnings("unchecked")
        Map<Integer, Object> newOrdersMap = (Map<Integer, Object>) request.getServletContext().getAttribute(NEW_ORDERS_ATTR);
        newOrdersMap.remove(customId);
    }

    private static String getSortingParam(HttpServletRequest request) {
        String result = request.getParameter(SORT_ATTR);
        if (result == null) {
            HttpSession session = request.getSession();
            result = (String) session.getAttribute(SORT_ATTR);
        }
        return Optional.ofNullable(result).orElse("");
    }

    private void sortOrder(List<OrderData> orderDataList, String sortingParam) {
        Comparator<OrderData> comp;
        switch (sortingParam) {
            case "price":
                comp = Comparator.comparingDouble(x -> x.getCustom().getCost());
                break;
            case "customer":
                comp = Comparator.comparing(x -> x.getUser().getLastName());
                break;
            case "status":
                comp = Comparator.comparing(x -> x.getStatus().getId());
                break;
            default:
                return;
        }
        orderDataList.sort(comp);
    }

    private static List<Custom> getCustomsByLabel(HttpServletRequest request) throws DAOException {
        String inProgress = request.getParameter(INPROGRESS_ATTR);
        HttpSession session = request.getSession();
        CustomService customService = new CustomService();
        List<Custom> customList;
        if (inProgress == null) {
            inProgress = (String) session.getAttribute(INPROGRESS_ATTR);
        }
        if (inProgress == null || inProgress.equals("true")) {
            customList = customService.getCustomsInProgress();
            customList.sort(Comparator.comparingInt(Custom::getStatusId));
            session.setAttribute(INPROGRESS_ATTR, "true");
        } else if (inProgress.equals("false")) {
            customList = customService.getCustomsCompleted();
            customList.sort(Comparator.comparing(Custom::getCreateTime));
            session.setAttribute(INPROGRESS_ATTR, "false");
        } else throw new CommandException("Wrong inProgress attr");
        return customList;
    }

    private static void sendPDFReportToUser(OrderData orderData, HttpServletRequest request) throws CommandException {
        Locale locale = new Locale(Optional
                .ofNullable((String) request.getSession().getAttribute(LANG_ATTR))
                .orElse((String) request.getServletContext().getAttribute(LANG_ATTR)));
        String email = orderData.getUser().getEmail();
        ResourceBundle bundle = ResourceBundle.getBundle(bundleFileName, locale);
        String subject = bundle.getString("report.email.subject") + orderData.getCustom().getId();
        String text = String.format("%s %s %s", bundle.getString("report.email.text"),
                orderData.getUser().getName(), orderData.getUser().getLastName());
        String fileName = "your_pdf_report.pdf";
        String mimeType = UtilsEmailNotificator.mimeTypePdf;

        UtilsPDFReportCreator reportCreator = new UtilsPDFReportCreator(orderData, request);
        try {
            ByteArrayOutputStream baos = reportCreator.getPDFStream();
            UtilsEmailNotificator.send(List.of(email), subject, text,
                    List.of(new ByteArrayInputStream(baos.toByteArray())),
                    List.of(fileName), List.of(mimeType));
        } catch (DocumentException e) {
            LOGGER.error(METHOD_FAILED, e.getLocalizedMessage(), e);
            throw new CommandException(PDF_EXC, e);
        } catch (EmailException | IOException e) {
            LOGGER.error(METHOD_FAILED, e.getLocalizedMessage(), e);
            throw new CommandException(EMAIL_EXC, e);
        }
    }
}
