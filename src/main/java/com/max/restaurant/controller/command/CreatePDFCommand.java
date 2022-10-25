package com.max.restaurant.controller.command;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.max.restaurant.exceptions.CommandException;
import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.OrderData;
import com.max.restaurant.model.services.CustomService;
import com.max.restaurant.utils.UtilsPDFReportCreator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.max.restaurant.utils.UtilsCommandNames.VALUE_ATTR;
import static com.max.restaurant.utils.UtilsExceptionMsgs.UNUSED_METHOD_WORKS;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_FAILED;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;

public class CreatePDFCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePDFCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.error(METHOD_STARTS_MSG, "executeGet", "true");
        throw new CommandException(UNUSED_METHOD_WORKS);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
        int customId = Integer.parseInt(request.getParameter(VALUE_ATTR));
        CustomService service = new CustomService();
        OrderData orderData = new OrderData(service.findCustomById(customId));
        Document document = new Document();
        try {
            response.setContentType("application/pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            UtilsPDFReportCreator reportCreator = new UtilsPDFReportCreator(document, orderData, request);
            reportCreator.fillPDFReportDocument();
        } catch (Exception e) {
            LOGGER.error(METHOD_FAILED, e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }finally {
            document.close();
        }
    }
}
