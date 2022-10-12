<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="utf-8" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<html lang="${lang}">
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/font-awesome.min.css">

</head>
<body class="w3-light-grey bgimg">
<jsp:include page="header.jsp"/>

<div class="w3-display-container w3-center w3-padding ">
    <div class="w3-auto w3-display-middle w3-third " style="margin-top: 300px">
        <div class="w3-card-4 w3-centered w3-round-large w3-light-grey w3-animate-opacity">
            <div class="w3-container w3-round-large-up w3-black w3-border-gray w3-bottombar">
                <h2 class="font-login"><fmt:message key="login.page"/></h2>
            </div>
            <%--        input form--%>
            <form class="w3-container w3-margin" action="${pageContext.request.contextPath}/ServletController"
                  method="post">
                <c:set var="emailPlace"><fmt:message key="login.email"/></c:set>
                <c:set var="passPlace"><fmt:message key="login.pass"/></c:set>
                <label class="w3-left w3-margin-top">${emailPlace}</label>
                <input class="w3-input w3-border w3-hover-border-light-gray " type="email" name="email"
                       value="<c:out value="${sessionScope.email}" default="klimenko@gmail.com"/>"
                       placeholder="${emailPlace}" required>
                <label class="w3-left w3-margin-top">${passPlace}</label>
                <input class="w3-input w3-border w3-hover-border-light-gray w3-round-medium" type="password"
                       name="password" value="Klimenko" placeholder="${passPlace}" required>
                <div class="w3-container">
                    <button class="w3-btn w3-block w3-margin-top w3-margin-bottom w3-round-large w3-gray w3-opacity-min"
                            type="submit" name="action" value="login">
                        <fmt:message key="login.enter"/>
                    </button>
                    <div class="container">
                        <fmt:message key="login.haveacc"/>?
                        <a href="${pageContext.request.contextPath}/ServletController?action=sign_up">
                            <fmt:message key="signup.enter"/>
                        </a>
                    </div>
                </div>
            </form>

            <div class="w3-container w3-border-0"></div>

        </div>
    </div>

</div>

<%--Wrong input line--%>
<div class="w3-margin">
    <c:if test="${!empty sessionScope.unsuccess}">
        <div class="w3-round-large w3-padding-large w3-pale-yellow w3-button w3-hover-none">
            <fmt:message key="${sessionScope.unsuccess}"/>
        </div>
    </c:if>

    <myTag:BackHomeButton/>
</div>
<jsp:include page="footer.jsp"/>

</body>
</html>