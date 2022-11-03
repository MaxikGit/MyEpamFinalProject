<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.max.restaurant.utils.UtilsCommandNames" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytag" %>
<%@ taglib prefix="s" uri="http://restaurant.max.com" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<html>
<head>
    <title>Order edit page for managers</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3my.css">
    <c:set value="${UtilsCommandNames.MANAGER_EDIT_ORDER}" var="commandName"/>
    <c:set value="${UtilsCommandNames.VALUE_ATTR}" var="value"/>
</head>

<body class="w3-light-grey w3-opacity-min w3-cursive">
<s:managersOnlyTag/>
<jsp:include page="header.jsp"/>
<div class="w3-container w3-light-blue w3-border-light-blue w3-center">
    <h3 class="font-login">${orderDataManagement.user.lastName} ${orderDataManagement.user.name}: <mytag:OrderCreatedDate
            custom="${orderDataManagement.custom}"/>
    </h3>
</div>
<div class="w3-container w3-center w3-margin-bottom">

    <form id="orderTable" name="orderTable" method="post"
          action="${pageContext.request.contextPath}/ServletController?action=orderEditManagement">
        <div class="w3-container w3-margin">
            <table class="w3-table-all w3-hoverable w3-center w3-light-blue w3-margin-bottom">
                <tr class="w3-card-2 w3-cell-row w3-hover-none w3-light-blue">
                    <th class="w3-cell-middle w3-col s2 w3-center">
                        <fmt:message key="order.num"/>
                    </th>
                    <th class="w3-cell-middle w3-quarter">
                        <fmt:message key="order.dish.name"/>
                    </th>
                    <th class="w3-cell-middle w3-col s2">
                        <fmt:message key="order.price"/>
                    </th>
                    <th class="w3-cell-middle w3-col s2 w3-center">
                        <fmt:message key="order.image"/>
                    </th>
                    <th class="w3-cell-middle w3-col s1 w3-center">
                        <fmt:message key="order.quantity"/>
                    </th>
                    <th class="w3-cell-middle w3-col s2 w3-center"></th>
                </tr>
                <c:forEach var="dishEntry" items="${orderDataManagement.dishes}" varStatus="num"
                           begin="${(pageNum-1) * pagesRecs}" end="${pageNum * pagesRecs - 1}">
                    <tr class="w3-card-2 w3-cell-row">
                        <td class="w3-cell-middle w3-col s2 w3-center">
                                ${num.index + 1}
                        </td>
                        <td class="w3-cell-middle w3-quarter">
                                ${dishEntry.key.name}
                        </td>
                        <td class="w3-cell-bottom w3-col s2">
                            <c:set var="currSymb"><fmt:message key="currency"/></c:set>
                            <fmt:formatNumber type="CURRENCY" value="${dishEntry.key.price}"
                                              currencySymbol="${currSymb}"/>
                        </td>
                        <td class="w3-cell-bottom w3-col s2 w3-center">
                            <img src="${pageContext.request.contextPath}${dishEntry.key.imagePath}"
                                 class="w3-circle" style="height:65px" alt="food picture">
                        </td>
                        <td class="w3-cell-bottom w3-col s1 w3-center">
                            <input name="quantity${dishEntry.key.id}" type="number" max="10" min="1"
                                   value="${dishEntry.value}" class="w3-input w3-border-2"/>
                        </td>
                        <td class="w3-cell-bottom w3-col s2 w3-center">
                            <mytag:DeleteForeverButton commandName="${commandName}" idToDelete="${dishEntry.key.id}"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <%--            Adding two Buttons to react--%>

        <mytag:ResetAccessButton lang="${lang}" totalCost="${orderDataManagement.custom.cost}"/>

        <%--            End of Adding two Buttons to react--%>

        <mytag:PaginationButtTag pagesMax="${pagesMax}" pagesMin="${pagesMin}" pageNum="${pageNum}" pagesTotal="${pagesTotal}"/>
    </form>
        <c:set var="buttName"><fmt:message key="management.back"/></c:set>
    <mytag:BackHomeButton altPath="/ServletController?action=management" altName="${buttName}"/>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
