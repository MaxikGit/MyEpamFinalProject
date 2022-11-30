<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>

<!DOCTYPE html>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <meta charset="UTF-8">
    <title>Exception page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3my.css">
    <style>
        .excimg {
            background-image: url('${pageContext.request.contextPath}/views/images/for_exception_pic.webp');
            height: 100%;
            background-position: center 50px;
            background-size: cover;
            background-repeat: no-repeat;
        }
    </style>
</head>
<c:if test="${loggedUser.roleId != 1}">
<body class="excimg w3-light-grey">
</c:if>
<c:if test="${loggedUser.roleId == 1}">
<body class="w3-light-grey">
</c:if>
<jsp:include page="header.jsp"/>

<div class="w3-container w3-center w3-margin-bottom w3-padding">

    <c:if test="${loggedUser.roleId == 1}">
        <div class="w3-panel w3-red w3-display-container w3-card-4 w3-round w3-animate-fading">
            <h2><fmt:message key="exception.page"/></h2>
        </div>
        <div class="w3-card-4 w3-round-large">
            <div class="w3-container  w3-light-blue w3-round-large">
                <h4 class="w3-border-red w3-text-black ">
                    <fmt:message key="exception.code"/> : ${pageContext.errorData.statusCode}  </h4>
                <hr>
                <div class="w3-card-4 w3-margin w3-border-light-green">
<%--                                        <c:forEach var="stackStrings" items="${pageContext.errorData.throwable.stackTrace}">--%>
<%--                                        <c:forEach var="stackStrings" items="${pageContext.errorData.throwable.cause.stackTrace}">--%>
<%--                                            <p> Exception in : ${stackStrings}</p>--%>
<%--                                        </c:forEach>--%>
                    <p>Throwable: ${pageContext.errorData.throwable}</p>
                    <p><fmt:message key="exception.localized"/>: ${pageContext.errorData.throwable.cause.localizedMessage}</p>
                </div>
                <hr>
                <div class="w3-card-4 w3-margin w3-border-light-green">
                    Stack Trace [0]: ${pageContext.errorData.throwable.cause.stackTrace[0]}
                </div>
                <hr>
                <div class="w3-card-4 w3-margin w3-border-light-green">
                    <fmt:message key="exception.message"/>: ${pageContext.exception.message}
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${loggedUser.roleId != 1}">
    <div class="w3-display-container" >
        <div class="w3-half w3-display-topleft w3-margin w3-animate-opacity">
                <span class="w3-padding-large w3-jumbo w3-text-gray font-login">
                    <fmt:message key="exception.oops"/> ${pageContext.errorData.statusCode}!
                </span>
        </div>
        <div class="w3-third w3-display-topright w3-animate-opacity w3-xlarge font-login">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 404}">
                    <p class="w3-text-dark-gray "><fmt:message key="exception.smthwrong404"/></p>
                    <p class="w3-text-dark-gray"><fmt:message key="exception.calm404"/>!</p>
                </c:when>
                <c:otherwise>
                    <p class="w3-text-dark-gray "><fmt:message key="exception.smthwrong"/></p>
                    <p class="w3-text-dark-gray"><fmt:message key="exception.calm"/>!</p>
                </c:otherwise>
            </c:choose>

        </div>
    </div>
    </c:if>
</div>
<div class="w3-margin w3-center">

    <myTag:BackHomeButton/>

    <jsp:include page="footer.jsp"/>
</div>
</body>
</html>