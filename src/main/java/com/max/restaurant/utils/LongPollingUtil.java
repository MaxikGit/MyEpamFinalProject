package com.max.restaurant.utils;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static com.max.restaurant.utils.UtilsCommandNames.NEW_ORDERS_ATTR;
import static com.max.restaurant.utils.UtilsCommandNames.NOTIFY_ORDERS_ATTR;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;

/**
 * Realisation of long polling mechanism, based on asynchronous request.
 */

public class LongPollingUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LongPollingUtil.class);
    private static LongPollingUtil longPoll;

    private AsyncContext asyncContext;
    private HttpServletResponse asyncResponse;
    private Thread thread;
    private Map<Integer, Object> newOrdersMap;

    private int notCommittedResponses;
    private int threadCount;
    private boolean exitThread;
    private final long checkTime = 5_000L;
    private int currentNewOrdersSize;

    private LongPollingUtil() {
    }

    public static LongPollingUtil getInstance() {
        LOGGER.debug(CONSTRUCTOR + " new instance = " + (longPoll == null));
        if (longPoll != null)
            return longPoll;
        synchronized (LongPollingUtil.class) {
            longPoll = new LongPollingUtil();
        }
        return longPoll;
    }

    public void startPoll(AsyncContext asyncContext) {
        initNewOrderMapFromContext(asyncContext);
        if (notCommittedResponses != 0) {
            commitResponse(HttpServletResponse.SC_NOT_FOUND);
        }
        initAsyncParams(asyncContext);
        if (thread == null || !thread.isAlive()) {
            startEventCheck();
        }
    }

    public void endPoll() {
        exitThread = true;
        if (thread != null && thread.isAlive()) {
            LOGGER.debug("thread "+ thread.getName() +" is alive trying to stop");
            try {
                Thread.sleep(checkTime);
            } catch (InterruptedException e) {
                throw new RuntimeException("endPoll interrupted", e);
            }
        }
    }

    private void initAsyncParams(AsyncContext asyncContext) {
        notCommittedResponses++;
        this.asyncContext = asyncContext;
        asyncContext.addListener(new MyAsyncContextListener());
        asyncContext.setTimeout(-1L);
        asyncResponse = (HttpServletResponse) asyncContext.getResponse();
    }

    private void startEventCheck() {
        thread = new Thread(new NewOrderChecker());
        thread.setName("LongPollThread-" + notCommittedResponses + "r" + threadCount + "t");
        thread.start();
    }

    private void initNewOrderMapFromContext(AsyncContext asyncContext) {
        if (newOrdersMap == null) {
            HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
            this.newOrdersMap = (Map<Integer, Object>) request.getServletContext().getAttribute(NEW_ORDERS_ATTR);
        }
    }

    private void commitResponse(int responseStatus) {
        if (asyncResponse.isCommitted()) {
            LOGGER.warn(Thread.currentThread().getName() + "finished unplanned");
            return;
        }
        try {
            writeOrderSizeToResponse();
            asyncResponse.setStatus(responseStatus);
            asyncContext.complete();
        } catch (IOException e) {
            asyncResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            asyncContext.complete();
            LOGGER.error("long poll can't write", e);
            throw new RuntimeException("long poll can't write", e);
        }
    }

    private void writeOrderSizeToResponse() throws IOException {
        PrintWriter writer = asyncContext.getResponse().getWriter();
        writer.println(newOrdersMap.size());
        writer.flush();
    }

    private void longPollAction() {
        LOGGER.debug(TWO_PARAMS_MSG,
                "currentNewOrdersSize " + currentNewOrdersSize, "changing to " + newOrdersMap.size());
        currentNewOrdersSize = newOrdersMap.size();
        HttpSession session = ((HttpServletRequest) asyncContext.getRequest()).getSession(false);
        if (session != null)
            session.setAttribute(NOTIFY_ORDERS_ATTR, currentNewOrdersSize);
        commitResponse(HttpServletResponse.SC_OK);
    }

    private class NewOrderChecker implements Runnable {
        private int counter = 1;

        @Override
        public void run() {
            try {
                LOGGER.debug(METHOD_STARTS_MSG, "run", currentNewOrdersSize);
                threadCount++;
                while (!exitThread && newOrdersMap.size() == currentNewOrdersSize) {
                    if (asyncResponse == null || asyncResponse.isCommitted()) {
                        threadCount--;
                        return;
                    }
                    LOGGER.debug("checking {} time, response#{} status {}", counter++, notCommittedResponses, asyncResponse.getStatus());
                    Thread.sleep(checkTime);
                }
                LOGGER.debug(Thread.currentThread().getName() + String
                        .format(" finished. Params are: exit=%b, orderMap changed=%b",
                                exitThread, newOrdersMap.size() != currentNewOrdersSize) );
                exitThread = false;
                threadCount--;
                longPollAction();
            } catch (InterruptedException e) {
                if (!asyncResponse.isCommitted()) {
                    asyncResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    asyncContext.complete();
                }
                LOGGER.error("Event checker was interrupted", e);
                threadCount--;
                throw new RuntimeException("interrupted", e);
            }
        }
    }

    class MyAsyncContextListener implements AsyncListener {
        @Override
        public void onComplete(AsyncEvent event) {
            logMethod("onComplete", event);
            notCommittedResponses--;
        }

        @Override
        public void onTimeout(AsyncEvent event) {
            logMethod("onTimeout", event);
        }

        @Override
        public void onError(AsyncEvent event) {
            logMethod("onError", event);
        }

        @Override
        public void onStartAsync(AsyncEvent event) {
            logMethod("onStartAsync", event);
        }

        private void logMethod(String methodName, AsyncEvent event) {
            HttpServletResponse resp = (HttpServletResponse) event.getSuppliedResponse();
            LOGGER.debug(methodName + ": response commited = {} with status {}", resp.isCommitted(), resp.getStatus());
        }
    }
}

//    private void checkNewOrdersInDB() throws DAOException {
//        HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
//        CustomService customs = new CustomService();
//        List<Custom> customList = customs.getCustomsInProgress();
//        Map<Integer, Object> newOrdersMap = (Map<Integer, Object>) request.getServletContext().getAttribute(NEW_ORDERS_ATTR);
//        for (Custom custom : customList) {
//            if (custom.getStatusId() == 1) {
//                newOrdersMap.put(custom.getId(), "null");
//            }
//        }
//    }
