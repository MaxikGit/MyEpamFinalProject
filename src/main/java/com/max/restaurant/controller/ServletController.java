package com.max.restaurant.controller;

import com.max.restaurant.controller.command.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.max.restaurant.utils.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * Main enter point, which controls the processing of all requests.
 * It holds all known commands of {@link Command} interface.
 */
@WebServlet(name = "ServletController", value = "/ServletController", asyncSupported = true)
public class ServletController extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletController.class);
    private static final Map<String, Command> allKnownCommands = new HashMap<>();
    private static final String GET = "get";
    private static final String POST = "post";

    /**
     * All commands initialization
     */
    @Override
    public void init() {
        allKnownCommands.put(LOGIN, new LoginCommand());
        allKnownCommands.put(SIGN_UP, new SignUpCommand());
        allKnownCommands.put(null, new UnknownCommand());
        allKnownCommands.put(CATEGORY, new CategoryCommand());
        allKnownCommands.put(EDIT_ORDER, new OrderCommand());
        allKnownCommands.put(SORT_DISHES, new SortCommand());
        allKnownCommands.put(MANAGEMENT, new ManageOrdersCommand());
        allKnownCommands.put(MANAGER_EDIT_ORDER, new ManagerOrderEditingCommand());
        allKnownCommands.put(LANGUAGE, new ChangeLangCommand());
        allKnownCommands.put(PDF, new CreatePDFCommand());
        allKnownCommands.put(PASS_RECOVERY, new ForgotPassCommand());
        allKnownCommands.put(NOTIFY, new NewOrderNotifyCommand());
        LOGGER.info(METHOD_STARTS_MSG, "init", "size " + allKnownCommands.size());
    }

    /**
     * Method which calls an appropriate GET methods of {@link Command} interface object, depending on "action" <br>
     * parameters of request.
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("doGet");
        chooseCommand(GET, request, response);
    }

    /**
     * Method which calls an appropriate POST methods of {@link Command} interface object, depending on "action" <br>
     * parameters of request.
     *
     * @param request  an {@link HttpServletRequest} object that
     *                 contains the request the client has made
     *                 of the servlet
     * @param response an {@link HttpServletResponse} object that
     *                 contains the response the servlet sends
     *                 to the client
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("doPost");
        chooseCommand(POST, request, response);
    }

    private static void chooseCommand(String method, HttpServletRequest request, HttpServletResponse response) {
        String commandName = request.getParameter(ACTION);
        LOGGER.debug(TWO_PARAMS_MSG, method, commandName);
        try {
            Command command = allKnownCommands.get(commandName);
            if (command == null)
                command = allKnownCommands.get(null);
            if (method.equals(POST))
                command.executePost(request, response);
            else
                command.executeGet(request, response);
        } catch (Throwable e) {
            LOGGER.error(METHOD_FAILED, e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }
}
