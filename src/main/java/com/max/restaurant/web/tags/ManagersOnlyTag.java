package com.max.restaurant.web.tags;


import com.max.restaurant.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.max.restaurant.utils.UtilsCommandNames.FIRST_START;
import static com.max.restaurant.utils.UtilsCommandNames.LOGGED_USER_ATTR;
import static com.max.restaurant.utils.UtilsLoggerMsgs.METHOD_STARTS_MSG;
import static com.max.restaurant.utils.UtilsLoggerMsgs.UNAUTH_MSG;

public class ManagersOnlyTag extends SimpleTagSupport {
    private int managerId = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagersOnlyTag.class);

    @Override
    public void doTag() throws IOException {
        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        LOGGER.info(METHOD_STARTS_MSG, "doTag", request.getRequestURI());
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute(LOGGED_USER_ATTR);
        if (user == null || user.getRoleId() != managerId){
            LOGGER.info(UNAUTH_MSG, user);
            ((HttpServletResponse)pageContext.getResponse()).sendRedirect(FIRST_START);
        }
    }
}
