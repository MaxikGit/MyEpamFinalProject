package com.max.restaurant.controller;

import com.max.restaurant.controller.command.CommandExecutor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_FAILED;

@WebServlet(name = "ServletController", value = "/ServletController")
public class ServletController extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletController.class);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("doGet");
        try {
            CommandExecutor.execute(request, response);
        } catch (Throwable e) {
            LOGGER.error(METHOD_FAILED, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("doPost");
        try {
            CommandExecutor.execute(request, response);
        }
         catch (Throwable e) {
            LOGGER.error(METHOD_FAILED,e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
