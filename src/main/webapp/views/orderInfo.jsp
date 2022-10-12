<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytag" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<html lang="${lang}">
<head>
    <title>Order list</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
</head>

<body class="w3-light-grey bgimg">
<jsp:include page="header.jsp"/>

<div class="w3-container w3-center w3-margin-bottom w3-opacity-min">
    <c:if test="${orderNames != null}">
        <form id="orderTable" name="orderTable" method="post"
              action="${pageContext.request.contextPath}/ServletController?action=orderEdit">
            <div class="w3-container w3-margin">
                <div class="w3-container w3-card-2 w3-light-blue w3-border-black w3-center w3-round-large-up">
                    <h3 class="font-login"><fmt:message key="order.info.page"/></h3>
                </div>
                <table class="w3-table-all w3-hoverable w3-center w3-light-blue w3-margin-bottom">
                    <tr class="w3-card-2 w3-cell-row w3-hover-none w3-light-blue">
                        <th class="w3-cell-middle w3-col s2 w3-center"><fmt:message key="order.num"/></th>
                        <th class="w3-cell-middle w3-quarter"><fmt:message key="order.dish.name"/></th>
                        <th class="w3-cell-middle w3-col s2"><fmt:message key="order.price"/></th>
                        <th class="w3-cell-middle w3-col s2 w3-center"><fmt:message key="order.image"/></th>
                        <th class="w3-cell-middle w3-col s1 w3-center"><fmt:message key="order.quantity"/></th>
                        <th class="w3-cell-middle w3-col s2 w3-center"></th>
                    </tr>
                    <c:forEach var="dishEntry" items="${orderNames}" varStatus="num"
                               begin="${(pageNum-1) * pagesRecs}" end="${pageNum * pagesRecs - 1}">
                        <tr class="w3-card-2 w3-cell-row">
                            <td class="w3-cell-middle w3-col s2 w3-center">${num.index + 1}</td>
                            <td class="w3-cell-middle w3-quarter">${dishEntry.key.name}</td>
                            <td class="w3-cell-bottom w3-col s2">
                                <c:set var="currSymb"><fmt:message key="currency"/></c:set>
                                <fmt:formatNumber type="CURRENCY" value="${dishEntry.key.price}"
                                                  currencySymbol="${currSymb}"/></td>
                            <td class="w3-cell-bottom w3-col s2 w3-center">
                                <img src="${pageContext.request.contextPath}${dishEntry.key.imagePath}"
                                     class="w3-circle" style="height:65px" alt="food picture">
                            </td>
                            <td class="w3-cell-bottom w3-col s1 w3-center">
                                <input name="quantity${dishEntry.key.id}" type="number" max="10" min="1"
                                       value="${dishEntry.value}" class="w3-input w3-border-2"/>
                            </td>
                            <td class="w3-cell-bottom w3-col s2 w3-center">
                                <mytag:DeleteForeverButton commandName="orderEdit" idToDelete="${dishEntry.key.id}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>

            <mytag:ResetAccessButton lang="${lang}" totalCost="${totalCost}"/>

            <mytag:PaginationButtTag pagesMax="${pagesMax}" pagesMin="${pagesMin}"/>

        </form>
    </c:if>

    <c:if test="${orderNames == null}">
        <div class="w3-panel w3-pale-yellow w3-display-container w3-card-4 w3-round">
                   <span onclick="this.parentElement.style.display='none'"
                         class="w3-button w3-margin-right w3-display-right w3-round-large w3-hover-red w3-border w3-border-red w3-hover-border-grey">
                   </span>
            <h5><fmt:message key="order.fail"/></h5>
        </div>
    </c:if>
</div>
<mytag:BackHomeButton/>


<jsp:include page="footer.jsp"/>
</body>
</html>
