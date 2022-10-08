<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="mytag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://restaurant.max.com" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Manager Order editing list</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
</head>

<body class="w3-light-grey w3-opacity-min">
<s:managersOnlyTag/>
<jsp:include page="header.jsp"/>
<div class="w3-display-container">
    <div class="w3-container w3-light-blue  w3-center">
        <h3>Orders info </h3>
    </div>
    <div class="w3-container w3-center w3-margin-bottom w3-display-container">
        <c:if test="${orderManagement != null}">
            <c:if test="${inProgress == 'false' }">
                <c:set var="Completed" value="w3-light-blue"/>
                <c:set var="InProgress" value=""/>
            </c:if>
            <c:if test="${empty inProgress || inProgress == 'true'}">
                <c:set var="Completed" value=""/>
                <c:set var="InProgress" value="w3-light-blue"/>
            </c:if>
            <form method="post" action="AuthorisationServlet?action=management">
                <div class="w3-container w3-margin">

                    <table class="w3-table-all w3-hoverable w3-center w3-light-blue w3-margin-bottom" style="margin-bottom: 200px">
                        <div class="w3-row w3-card w3-border-light-blue w3-large ">
                            <a id="progress" href="AuthorisationServlet?action=management&inProgress=true"
                               class="w3-col s6 w3-button ${InProgress} tablink w3-hover-light-blue">
                                In progress</a>
                            <a id="finished" href="AuthorisationServlet?action=management&inProgress=false"
                               class="w3-col s6 w3-button tablink w3-hover-light-blue ${Completed} ">
                                Finished</a>
                        </div>
                        <tr class="w3-card-2 w3-cell-row w3-light-blue">
                            <th class="w3-cell-middle w3-col s1 w3-center">Order #</th>
                            <th class="w3-cell-middle w3-col s3">Customer</th>
                            <th class="w3-cell-middle w3-col s2">Price</th>
                            <th class="w3-cell-middle w3-col s2 w3-center">Dishes</th>
                            <th class="w3-cell-middle w3-col s2 w3-center">Status</th>
                            <th class="w3-cell-middle w3-col s1 w3-right-align"></th>
                            <th class="w3-cell-middle w3-col s1 w3-right"></th>
                        </tr>
                        <c:if test="${orderManagement[0].custom != null}">
                            <c:forEach var="orderData" items="${orderManagement}" varStatus="num"
                                       begin="${(pageNum-1) * pagesRecs}" end="${pageNum * pagesRecs - 1}">
                                <tr class="w3-card-2">
                                    <div class="w3-cell-row">
                                    <td class="w3-cell-middle w3-col s1 w3-center">${num.index + 1}</td>
                                    <td class="w3-cell-middle w3-col s3">${orderData.user.name} ${orderData.user.lastName}</td>
                                    <td class="w3-cell-middle w3-col s2">
                                        <fmt:setLocale value="uk_UA"/>
                                        <fmt:formatNumber type="CURRENCY" value="${orderData.custom.cost}"
                                                          currencySymbol="UAH"/>
                                    </td>
                                    <td class="w3-cell-middle w3-col s2 w3-center ">

                                        <mytag:ModalDishInOrder orderData="${orderData}" divNum="${num.count}"/>
                                    </td>
                                    <td class="w3-cell-bottom w3-col s2 w3-center">
                                        <c:if test="${inProgress != 'false' }">
                                            <select name="statusSelect" id="statusSelect"
                                                    class="w3-dropdown-hover w3-light-grey">
                                                <option value="${orderData.status.id}" selected
                                                        disabled>${orderData.status.name}</option>
                                                <c:forEach var="status" varStatus="statNum" items="${statusNames}">
                                                    <c:if test="${status.id != orderData.status.id}">
                                                        <option value="${status.id} ${orderData.custom.id}">${status.name}</option>
                                                    </c:if>
                                                </c:forEach>
                                            </select>
                                        </c:if>
                                        <c:if test="${inProgress == 'false' }">
                                            ${orderData.status.name}
                                        </c:if>
                                    </td>
                                    <c:if test="${inProgress != 'false' }">
                                        <td class="w3-cell-middle w3-col s1 w3-right-align">

                                            <a href="AuthorisationServlet?action=orderEditManagement&value=${orderData.custom.id}">
                                                <i class="material-icons w3-xxlarge w3-text-grey w3-hover-shadow w3-circle">&#xe3c9;</i>
                                            </a>

                                        </td>
                                        <td class="w3-cell-middle w3-col s1 w3-right">
<%--                                            <mytag:DeleteForeverButton commandName="management"--%>
<%--                                                                       idToDelete="${orderData.custom.id}"/>--%>
                                            <mytag:DeleteForeverButton commandName="management" idToDelete="${orderData.custom.id}"/>
                                        </td>
                                    </c:if>
                                    <c:if test="${inProgress == 'false'}">
                                        <td class="w3-cell-middle w3-col s1 w3-right-align">
                                            <i class="material-icons w3-xxlarge w3-text-grey w3-hover-shadow w3-circle">&#xe415;</i>
                                        </td>
                                        <td class="w3-cell-middle w3-col s1 w3-right w3-hover-none">
<%--                                            <mytag:DeleteForeverButton commandName=""--%>
<%--                                                                       idToDelete="${0}"/>--%>
                                        </td>
                                    </c:if>
                                    </div>
                                </tr>
                            </c:forEach>
                        </c:if>
                        <c:if test="${orderManagement[0].custom == null}">
                            <tr class="w3-card-2 w3-cell-row w3-pale-yellow">
                                <th class="w3-cell-middle w3-center">
                                    <h3>There are no orders yet!</h3>
                                </th>
                            </tr>
                        </c:if>
                    </table>
                </div>

                    <%--            Adding two Buttons to react--%>
                <c:if test="${inProgress != 'false' }">
                    <div class="w3-cell-row w3-half w3-right">
                        <h5>
                            <button name="action" value="management" type="reset"
                                    class="w3-button w3-card-4 w3-third w3-pale-red w3-hover-red w3-round-large">Reset
                            </button>
                            <a class="w3-third w3-hover-none">
                                Total: <fmt:formatNumber type="CURRENCY" value="${totalOrdersCost}"
                                                         currencySymbol="UAH"/>
                            </a>
                            <button name="accepted" value="yes" type="submit"
                                    class="w3-button w3-card-4 w3-third w3-pale-green w3-hover-green w3-round-large">
                                Accept
                            </button>
                        </h5>
                    </div>
                </c:if>
                    <%--            End of Adding two Buttons to react--%>

                    <%--Pagination begins            --%>

                <mytag:PaginationButtTag pagesMax="${pagesMax}" pagesMin="${pagesMin}"/>

                    <%--                    Pagination ends                    --%>

            </form>
        </c:if>
        <c:if test="${orderManagement == null}">
            <div class="w3-panel w3-pale-yellow w3-display-bottommiddle w3-display-container w3-card-4 w3-round">
                   <span onclick="this.parentElement.style.display='none'"
                         class="w3-button w3-margin-right w3-display-right w3-round-large w3-hover-red w3-border w3-border-red w3-hover-border-grey">
                   </span>
                <h5>There are no orders yet!</h5>
            </div>
        </c:if>
    </div>
<%--    <div class="w3-container w3-grey w3-opacity w3-right-align w3-padding">--%>
<%--        <button class="w3-btn w3-round-large " onclick="location.href='${pageContext.request.contextPath}/'">Back to--%>
<%--            main--%>
<%--        </button>--%>
<%--    </div>--%>
<%--    <div class="w3-container" style="height: 65px">--%>
<%--    </div>--%>
    <mytag:BackHomeButton/>
</div>

<jsp:include page="footer.jsp"/>

</body>
</html>
<%--<script>--%>
<%--    // Tabbed Menu--%>
<%--    function openMenu(evt) {--%>
<%--        var i, tablinks;--%>

<%--        tablinks = document.getElementsByClassName("tablink");--%>
<%--        for (i = 0; i < tablinks.length; i++) {--%>

<%--            tablinks[i].className = tablinks[i].className.replace(" w3-light-blue", "");--%>
<%--        }--%>
<%--        evt.currentTarget.className += " w3-light-blue";--%>
<%--    }--%>

<%--</script>--%>

<%--                            <td class="w3-cell-bottom w3-col s2 w3-center">--%>
<%--                                <img src="${pageContext.request.contextPath}${order.dish.imagePath}"--%>
<%--                                     class="w3-circle" style="height:65px" alt="food picture">--%>
<%--                            </td>--%>
<%--                            <td class="w3-cell-bottom w3-col s1 w3-center">--%>
<%--                                <input name="quantity" type="number" max="10" min="1" value="1"--%>
<%--                                       class="w3-input w3-border-2"/>--%>
<%--                            </td>--%>
<%--                            <td class="w3-cell-bottom w3-col s2 w3-center">--%>
<%--                                <a href="${pageContext.request.contextPath}/AuthorisationServlet?action=orderEdit&deleteId=${dish.id}">--%>
<%--                                    <i class="material-icons w3-xxxlarge w3-text-grey w3-circle">&#xe92b;</i>--%>
<%--                                </a>--%>
<%--                            </td>--%>
