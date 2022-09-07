<%@ page import="java.util.List" %>
<%@ page import="com.max.restaurant.model.entity.User" %><%--
  Created by IntelliJ IDEA.
  User: maxim
  Date: 18.08.22
  Time: 12:30
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users list</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
</head>

<body class="w3-light-grey">
<div class="w3-container w3-blue-grey w3-opacity w3-right-align">
    <h1>Super app!</h1>
</div>

<div class="w3-container w3-center w3-margin-bottom w3-padding">
    <div class="w3-card-4">
        <div class="w3-container w3-light-blue">
            <h2>Users</h2>
        </div>
        <%
            List<User> names = (List<User>) request.getAttribute("userNames");

            if (names != null && !names.isEmpty()) {
                out.println("<ul class=\"w3-ul\">");
                for (User s : names) {
                    out.println("<li class=\"w3-hover-sand\">" + s + "</li>");
                }
                out.println("</ul>");

            } else out.println("<div class=\"w3-panel w3-red w3-display-container w3-card-4 w3-round\">\n"
                    +
                    "   <span onclick=\"this.parentElement.style.display='none'\"\n" +
                    "   class=\"w3-button w3-margin-right w3-display-right w3-round-large w3-hover-red w3-border w3-border-red w3-hover-border-grey\">×</span>\n" +
                    "   <h5>There are no users yet!</h5>\n" +
                    "</div>");
        %>
    </div>
</div>

<div class="w3-container w3-grey w3-opacity w3-right-align w3-padding">
    <button class="w3-btn w3-round-large" onclick="location.href='${pageContext.request.contextPath}/'">Back to main</button>
    <a href="/views/table.jsp" id="AuthorizationManager">To table</a>
<%--    <form action="${pageContext.request.contextPath}views/table.jsp" class="w3-animate-right w3-left">--%>
<%--    <button class="w3-btn w3-round-large" onclick="location.href='${pageContext.request.contextPath}/views/table.jsp'">To table</button>--%>
<%--    </form>--%>
</div>
</body>
</html>
