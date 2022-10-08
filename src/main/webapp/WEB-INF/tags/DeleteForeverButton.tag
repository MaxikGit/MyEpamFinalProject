<%@tag import="com.max.restaurant.controller.command.UtilsCommandNames" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="commandName" type="java.lang.String" required="true" %>
<%@attribute name="idToDelete" type="java.lang.Integer" required="true" %>

<c:if test="${!empty commandName}">
<button type="submit" name="${UtilsCommandNames.DEL_FROM_ORDER_ATTR}" value="${idToDelete}" >
    <i class="material-icons w3-xxxlarge w3-text-grey w3-hover-border-red w3-hover-shadow w3-circle">&#xe92b;</i>
</button>
</c:if>
<c:if test="${empty commandName}">
        <i class="material-icons w3-xxxlarge w3-text-grey w3-hover-border-red w3-hover-shadow w3-circle">&#xe92b;</i>
</c:if>