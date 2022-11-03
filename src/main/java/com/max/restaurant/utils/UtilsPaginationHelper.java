package com.max.restaurant.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.max.restaurant.utils.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;

/**
 * Helper class, calculates the number of pages to display. Results are set in {@code HttpSession} object as attributes.
 * Attribute names are:<br>
 * {@link UtilsCommandNames#PAGES_MIN_ATTR},<br>
 * {@link UtilsCommandNames#PAGES_MAX_ATTR},<br>
 * {@link UtilsCommandNames#PAGE_ATTR},<br>
 * {@link UtilsCommandNames#TOTAL_PAGES_ATTR};
 */
public class UtilsPaginationHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UtilsPaginationHelper.class);
    private static int numOfPageRecords;
    private static int totalOfPages;
    private static final int NUM_OF_PAGE_RECORDS = 3;
    private static final int NUM_OF_PAGE_BUTTONS = 3;

    /**
     * Calculates minimal and maximal number of pages, than writes them as attributes to session object.<br>
     * Number of pagination buttons {@link #NUM_OF_PAGE_BUTTONS} have default value, while number of records per page
     * {@link #numOfPageRecords} can be pointed as <strong>recsPerPage</strong> parameter     *
     * @param request HttpServletRequest object
     * @param containerSize the size of the whole collection to be displayed on a page
     */
    public static synchronized void paginationCounter(HttpServletRequest request, int containerSize) {
        numOfPageRecords = NUM_OF_PAGE_RECORDS;
        paginationCounter(request, containerSize, numOfPageRecords);
    }

    /**
     * Is the copy of {@link #paginationCounter(HttpServletRequest, int)} with the only difference, that
     * the number of pagination buttons {@link #NUM_OF_PAGE_BUTTONS} is user-defined
     * @param request HttpServletRequest object
     * @param containerSize the size of the whole collection to be displayed on a page
     * @param recsPerPage user defined value of {@link #numOfPageRecords}
     */
    public static void paginationCounter(HttpServletRequest request, int containerSize,
                                         int recsPerPage) {
        LOGGER.info(METHOD_STARTS_MSG, "paginationCounter", "true");
        UtilsPaginationHelper.numOfPageRecords = recsPerPage;
        UtilsPaginationHelper.totalOfPages = (int) Math.ceil(containerSize * 1.0 / numOfPageRecords);
        HttpSession session = request.getSession(false);
        int currPage = Integer.parseInt(Optional.ofNullable(request.getParameter(PAGE_ATTR)).orElse("1"));
        int pageNumMin;
        int pageNumMax;

        if (totalOfPages == 1) {
            pageNumMax = 1;
            pageNumMin = 1;
        } else {
            pageNumMin = getMinPageNum(currPage);
            pageNumMax = getMaxPageNum(pageNumMin);
        }
        session.setAttribute(PAGES_MIN_ATTR, pageNumMin);
        session.setAttribute(PAGES_MAX_ATTR, pageNumMax);
        session.setAttribute(PAGE_ATTR, currPage);
        session.setAttribute(TOTAL_PAGES_ATTR, totalOfPages);
        session.setAttribute(RECS_PER_PAGE_ATTR, numOfPageRecords);
    }

    private static int getMinPageNum(int currPageNum) {
        int tempMin = Math.max((currPageNum - (NUM_OF_PAGE_BUTTONS / 2)), 1);
        if (tempMin <= (totalOfPages - NUM_OF_PAGE_BUTTONS))
            return tempMin;
        return Math.max(totalOfPages - NUM_OF_PAGE_BUTTONS + 1, 1);
    }

    private static int getMaxPageNum(int pageNumMin) {
        return Math.min((pageNumMin + (NUM_OF_PAGE_BUTTONS - 1)), totalOfPages);
    }

}
