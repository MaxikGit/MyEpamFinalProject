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
 * Attribute names are:
 * {@link UtilsCommandNames#PAGES_MIN_ATTR},
 * {@link UtilsCommandNames#PAGES_MAX_ATTR},
 * {@link UtilsCommandNames#PAGE_ATTR},
 * {@link UtilsCommandNames#RECS_PER_PAGE_ATTR};
 */

public class UtilsPaginationHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UtilsPaginationHelper.class);
    private static int NUM_OF_RECS = 3;
    private static int PAGINATION_SIZE = 3;

    public static void paginationCounter(HttpServletRequest request, int containerSize) {
        LOGGER.info(METHOD_STARTS_MSG, "paginationCounter", "true");
        int totalOfpages = (int) Math.ceil(containerSize * 1.0 / NUM_OF_RECS);
        HttpSession session  = request.getSession(false);
        int currPageNum = Integer.parseInt(Optional.ofNullable(request.getParameter(PAGE_ATTR)).orElse("1"));
        int pageNumMin = Optional.ofNullable((Integer) session.getAttribute(PAGES_MIN_ATTR)).orElse(1);
        int pageNumMax;
        int currNumOfPages = Math.min(totalOfpages, PAGINATION_SIZE);

        if (totalOfpages == 1) {
            pageNumMax = 1;
        } else
        {
            pageNumMax = Integer.parseInt(Optional.ofNullable(request.getParameter(PAGES_MAX_ATTR))
                    .orElse("" + (Math.min(totalOfpages, currNumOfPages))));
            if (currPageNum < pageNumMin) {
                if (pageNumMin - currNumOfPages > 1) {
                    pageNumMin = pageNumMin - currNumOfPages;
                    pageNumMax = pageNumMin + currNumOfPages;
                } else {
                    pageNumMin = 1;
                    pageNumMax = currNumOfPages;
                    currPageNum = 1;
                }
            } else if (currPageNum > pageNumMax) {
                if (pageNumMax + currNumOfPages <= totalOfpages) {
                    pageNumMax = pageNumMax + currNumOfPages;
                    pageNumMin = pageNumMax - currNumOfPages + 1;
                    currPageNum =pageNumMin;
                } else {
                    pageNumMax = totalOfpages;
                    pageNumMin = pageNumMax - currNumOfPages + 1;
                    currPageNum = pageNumMax;
                }
            }
        }
        session.setAttribute(PAGES_MIN_ATTR, pageNumMin);
        session.setAttribute(PAGES_MAX_ATTR, pageNumMax);
        session.setAttribute(PAGE_ATTR, currPageNum);
        session.setAttribute(RECS_PER_PAGE_ATTR, NUM_OF_RECS);
    }
    public static void paginationCounter(HttpServletRequest request, int containerSize,
                                         int recsPerPage) {
        NUM_OF_RECS = recsPerPage;
        paginationCounter(request, containerSize);
    }

}
