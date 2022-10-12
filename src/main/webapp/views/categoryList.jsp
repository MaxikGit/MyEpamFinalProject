<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%--<html>--%>
<%--<head>--%>
<%--    <title>Categories list</title>--%>
<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">--%>
<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}views/styles/font-awesome.min.css">--%>
<%--</head>--%>

<%--<body>--%>
<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>
<div>
    <a href="ServletController?action=category" class="w3-bar-item w3-button w3-padding w3-blue w3-cell-row">
        <i class="material-icons ">&#xe896;</i>&nbsp;
        <span class="w3-large"><fmt:message key="category.overview"/></span></a>
    <div class="w3-bar-block">
        <c:if test="${categoryNames != null}">
            <c:forEach var="category" items="${categoryNames}">
                <c:set var="category_prop"><fmt:message key="${category.name}"/></c:set>
                <a href="ServletController?action=category&value=${category.id}"
                   class="w3-bar-item w3-button w3-padding">
                    <i class="fa fa-math fa-fw"></i>&nbsp;&nbsp; ${category_prop}</a>
            </c:forEach>
        </c:if>
        <c:if test="${loggedUser != null}">

            <%--        Managers only--%>

            <c:if test="${loggedUser.roleId == 1}">
                <hr class="w3-border-gray" style="width: 80%; margin: auto">
                <a href="ServletController?action=management"
                   class="w3-bar-item w3-button w3-padding">
                    <i class="fa fa-math fa-fw"></i>&nbsp; <fmt:message key="category.management"/></a>
            </c:if>
        </c:if>
    </div>
</div>
<%--</body>--%>
<%--</html>--%>
