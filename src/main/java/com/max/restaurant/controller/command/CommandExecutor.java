package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.CommandException;
import com.max.restaurant.exceptions.DAOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.max.restaurant.utils.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * This is a helper class that is used to work with servlet commands.
 * It holds all known commands of {@link Command} interface.
 */
public class CommandExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutor.class);
    private static final Map<String, Command> allKnownCommands = new HashMap<>();

    private CommandExecutor() {
    }

    static {
        LOGGER.info(STATIC_INI);
        allKnownCommands.put(LOGIN, new LoginCommand());
        allKnownCommands.put(SIGN_UP, new SignUpCommand());
        allKnownCommands.put(null, new NullCommand());
        allKnownCommands.put(CATEGORY, new CategoryCommand());
        allKnownCommands.put(EDIT_ORDER, new OrderCommand());
        allKnownCommands.put(SORT_DISHES, new SortCommand());
        allKnownCommands.put(MANAGEMENT, new ManageOrdersCommand());
        allKnownCommands.put(MANAGER_EDIT_ORDER, new ManagerOrderEditingCommand());
        allKnownCommands.put(LANGUAGE, new ChangeLangCommand());
        allKnownCommands.put(PDF, new CreatePDFCommand());
    }

    /**
     * Method which calls an appropriate {@link Command} interface object, depending on "action" parameters of
     * request. It also parses servlet requests to POST or GET processing methods of {@link Command} interface object.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws DAOException
     * @throws CommandException
     */
    public static final void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException, CommandException {
        LOGGER.info(METHOD_STARTS_MSG, "execute", "true");
        String commandName = request.getParameter(ACTION);
        String method = request.getMethod();
        LOGGER.debug(TWO_PARAMS_MSG, method, commandName);
        Command command;
        if (method.equalsIgnoreCase("GET")) {
            command = allKnownCommands.get(commandName);
            if (command == null)
                command = allKnownCommands.get(null);
            command.executeGet(request, response);
        } else if (method.equalsIgnoreCase("POST")) {
            command = allKnownCommands.get(commandName);
            if (command == null)
                command = allKnownCommands.get(null);
            command.executePost(request, response);
        }
    }
}
