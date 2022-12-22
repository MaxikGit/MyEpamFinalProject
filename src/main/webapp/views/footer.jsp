<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Restaurant footer</title>
    <c:if test="${loggedUser!= null && loggedUser.roleId == 1}">
        <script src="${pageContext.request.contextPath}/views/js/jquery-1.11.3.min.js"></script>
        <script src="${pageContext.request.contextPath}/views/js/managersNotification.js"></script>
        <script src="${pageContext.request.contextPath}/views/js/sendURLbyGet.js"></script>
    </c:if>
</head>

<%--<div class="w3-container w3-bottom w3-topbar w3-light-grey w3-display-container font-login w3-center w3-xlarge w3-text-blue">--%>
<%--    Powered by <a href="https://github.com/MaxikGit/Restaurant.git" target="_blank" ><u>Maxim Bezzubov</u></a>--%>
<%--</div>--%>

<div class="w3-container w3-bottom w3-topbar w3-light-grey w3-display-container font-login w3-center w3-xlarge w3-text-blue"
     style="height: 50px">
    <div class="w3-display-middle w3-cell-middle">
        Powered by <a href="https://github.com/MaxikGit/Restaurant.git" target="_blank"><u>Maxim Bezzubov</u></a>
    </div>


    <c:if test="${loggedUser!= null && loggedUser.roleId == 1}">
        <c:choose>
            <c:when test="${notifyOrders != null && notifyOrders > 0}">
                <c:set var="hideBell" value=""/>
            </c:when>
            <c:otherwise>
                <c:set var="hideBell" value="w3-hide"/>
            </c:otherwise>
        </c:choose>
        <script>
            let notification_url = "${pageContext.request.contextPath}/ServletController?action=notify"
        </script>
        <div id="manager_notification" class="w3-display-right w3-container ${hideBell}">
            <a href="${pageContext.request.contextPath}/ServletController?action=management">
                <i class="material-icons w3-shake-it w3-hover-text-amber">notifications_active</i>
                <span id="manager_notification_count">${notifyOrders}</span>
            </a>
        </div>
    </c:if>
</div>

</html>
