<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytag" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Order list</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
</head>

<body class="w3-light-grey bgimg">
<jsp:include page="header.jsp"/>

<div class="w3-container w3-center w3-margin-bottom w3-opacity-min">
    <c:if test="${orderNames != null}">
        <fmt:setLocale value="uk_UA"/>
        <form id="orderTable" name="orderTable" method="post"
              action="${pageContext.request.contextPath}/AuthorisationServlet?action=orderEdit">
            <div class="w3-container w3-margin">
                <div class="w3-container w3-card-2 w3-light-blue w3-border-black w3-center w3-round-large-up">
                    <h3 class="font-login">Order details</h3>
                </div>
                <table class="w3-table-all w3-hoverable w3-center w3-light-blue w3-margin-bottom">
                    <tr class="w3-card-2 w3-cell-row w3-hover-none w3-light-blue">
                        <th class="w3-cell-middle w3-col s2 w3-center">id#</th>
                        <th class="w3-cell-middle w3-quarter">Name</th>
                        <th class="w3-cell-middle w3-col s2">Price</th>
                        <th class="w3-cell-middle w3-col s2 w3-center">Image</th>
                        <th class="w3-cell-middle w3-col s1 w3-center">Quantity</th>
                        <th class="w3-cell-middle w3-col s2 w3-center"></th>
                    </tr>
                    <c:forEach var="dishEntry" items="${orderNames}" varStatus="num"
                               begin="${(pageNum-1) * pagesRecs}" end="${pageNum * pagesRecs - 1}">
<%--                    <c:forEach var="dishEntry" items="${orderNames}" varStatus="num"--%>
<%--                               begin="1" end="3">--%>
                        <tr class="w3-card-2 w3-cell-row">
                            <td class="w3-cell-middle w3-col s2 w3-center">${num.index + 1}</td>
                            <td class="w3-cell-middle w3-quarter">${dishEntry.key.name}</td>
                            <td class="w3-cell-bottom w3-col s2">
                                <fmt:formatNumber type="CURRENCY" value="${dishEntry.key.price}"
                                                  currencySymbol="UAH"/></td>
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

                <%--            Adding two Buttons to react--%>

            <div class="w3-cell-row w3-half w3-right">
                <h5>
                    <button name="accepted" value="no" type="reset"
                            class="w3-button w3-card-4 w3-third w3-pale-red w3-hover-red w3-round-large">Reset
                    </button>

                    <a class="w3-third w3-hover-none">Total:
                        <fmt:formatNumber type="CURRENCY" value="${totalCost}" currencySymbol="UAH"/>
                    </a>
                        <%--                    <button class="w3-button w3-third w3-hover-none">Total: ${totalCost}</button>--%>
                    <button name="accepted" value="yes" type="submit"
                            class="w3-button w3-card-4 w3-third w3-pale-green w3-hover-green w3-round-large">Accept
                    </button>
                </h5>
            </div>

                <%--            End of Adding two Buttons to react--%>

<%-- Pagination begins            --%>

            <mytag:PaginationButtTag pagesMax="${pagesMax}" pagesMin="${pagesMin}"/>

<%--                    Pagination ends                    --%>

        </form>
    </c:if>

    <c:if test="${orderNames == null}">
        <div class="w3-panel w3-pale-yellow w3-display-container w3-card-4 w3-round">
                   <span onclick="this.parentElement.style.display='none'"
                         class="w3-button w3-margin-right w3-display-right w3-round-large w3-hover-red w3-border w3-border-red w3-hover-border-grey">
                   </span>
            <h5>There are no orders yet!</h5>
        </div>
    </c:if>
</div>
<%--<div class="w3-container w3-grey w3-opacity w3-right-align w3-padding w3-display-container">--%>
<%--    <button class="w3-btn w3-round-large" onclick="location.href='${pageContext.request.contextPath}/'">--%>
<%--        Back to main--%>
<%--    </button>--%>
<%--</div>--%>
<mytag:BackHomeButton/>


<jsp:include page="footer.jsp"/>
</body>
</html>
