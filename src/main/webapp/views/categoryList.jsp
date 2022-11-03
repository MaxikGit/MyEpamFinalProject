<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<c:choose>
    <c:when test="${empty categoryId}"><c:set var="hoverClass" value="w3-blue"/></c:when>
    <c:otherwise><c:set var="hoverClass" value=""/></c:otherwise>
</c:choose>

<%--<div id="category">--%>
<div id="category" class="w3-bar-block">
    <a href="ServletController?action=category" class="w3-bar-item w3-button w3-padding ${hoverClass} w3-cell-row">
        <%--    <a href="#" class="w3-bar-item w3-button w3-padding w3-blue w3-cell-row">--%>
        <i class="material-icons ">&#xe896;</i>&nbsp;
        <span class="w3-xlarge font-login">
            <fmt:message key="category.overview"/>
        </span>
    </a>
    <c:if test="${categoryNames != null}">
        <c:forEach var="category" items="${categoryNames}">
            <c:set var="category_prop">
                <fmt:message key="${category.name}"/>
            </c:set>
<%--            highlighting category menu--%>
            <c:choose>
                <c:when test="${categoryId == category.id}"><c:set var="hoverClass" value="w3-blue"/></c:when>
                <c:otherwise><c:set var="hoverClass" value=""/></c:otherwise>
            </c:choose>
            <a href="ServletController?action=category&value=${category.id}"
               class="w3-bar-item w3-button w3-padding font-login w3-xlarge w3-text-dark-grey w3-hover-light-blue ${hoverClass}">
                &emsp; &nbsp;${category_prop}
            </a>
        </c:forEach>
    </c:if>
    <c:if test="${loggedUser != null}">

        <%--        Managers only--%>

        <c:if test="${loggedUser.roleId == 1}">
            <hr class="w3-border-gray" style="width: 80%; margin: auto">
            <a href="ServletController?action=management"
               class="w3-bar-item w3-button w3-padding font-login w3-xlarge">
                <fmt:message key="category.management"/>
            </a>
        </c:if>
    </c:if>
</div>