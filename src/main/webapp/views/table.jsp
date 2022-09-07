<%@ page import="java.util.List" %>
<%@ page import="com.max.restaurant.model.entity.User" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users list</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
</head>

<body class="w3-light-grey">

<jsp:include page="header.jsp"/>

<div class="w3-container w3-center w3-margin-bottom w3-padding" >
    <div class="w3-card-4">
        <div class="w3-container w3-light-blue w3-border-light-blue" >
            <h2>Users</h2>
        </div>
        <c:if test="${userNames != null}" >
        <div class="w3-container">
            <table class="w3-table-all w3-hoverable w3-center w3-light-blue">
            <tr class="w3-hover-none">
                <th>id#</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>E-mail</th>
                </tr>
            <c:forEach var="user" items="${userNames}">
                <tr><td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.lastName}</td>
                <td>${user.email}</td></tr>
            </c:forEach>
        </c:if>
                <c:if test="${userNames == null}">
                <div class="w3-panel w3-red w3-display-container w3-card-4 w3-round" >

                   <span onclick="this.parentElement.style.display='none'"
                         class="w3-button w3-margin-right w3-display-right w3-round-large w3-hover-red w3-border w3-border-red w3-hover-border-grey">
                   </span>
                   <h5>There are no users yet!</h5>
                </div>
                </c:if>
    </div>
</div>

<div class="w3-container w3-grey w3-opacity w3-right-align w3-padding">
    <button class="w3-btn w3-round-large" onclick="location.href='${pageContext.request.contextPath}/'">Back to main
    </button>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
