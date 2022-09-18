package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.DAOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.max.restaurant.controller.command.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;


public class CommandExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutor.class);

    private static final Map<String, Command> allKnownCommands = new HashMap<>();
    private static final Map<String, Command> commandPostMeth = new HashMap<>();

    private CommandExecutor() {
    }


    static {
        LOGGER.info(STATIC_INI);
        allKnownCommands.put(LOGIN, new LoginCommand());
        allKnownCommands.put(SIGN_UP, new SignUpCommand());
        allKnownCommands.put(null, new NullCommand());
        allKnownCommands.put(CATEGORY, new CategoryCommand());
        allKnownCommands.put(ORDER, new OrderCommand());
        allKnownCommands.put(ORDER_EDIT, new OrderEditingCommand());
        allKnownCommands.put(SORT_DISHES, new SortCommand());
        allKnownCommands.put(MANAGEMENT, new ManageOrdersCommand());
        //commandGetMeth.put(CATEGORY_ID, new DishByCategoryCommand());
//        allKnownCommandsMap.put(DEPOSIT, new DepositCommand());
//        allKnownCommandsMap.put(WITHDRAW, new WithdrawCommand());
//        allKnownCommandsMap.put(EXIT, new ExitCommand());
//        allKnownCommandsMap.put(LOGIN, new LoginCommand());
    }

    public static final void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
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
