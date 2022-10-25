<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://restaurant.max.com" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<html lang="${lang}">
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
        <h3 class="font-login"><fmt:message key="order.management.page"/></h3>
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
            <form method="post" action="ServletController?action=management">
                <div class="w3-container w3-margin">

                    <table class="w3-table-all w3-hoverable w3-center w3-light-blue w3-margin-bottom"
                           style="margin-bottom: 200px">
                        <div class="w3-row w3-card w3-border-light-blue w3-large ">
                            <a id="progress" href="ServletController?action=management&inProgress=true"
                               class="w3-col s6 w3-button ${InProgress} tablink w3-hover-light-blue">
                                <strong class="font-login"><i><fmt:message key="order.inprogress"/></i></strong>
                            </a>
                            <a id="finished" href="ServletController?action=management&inProgress=false"
                               class="w3-col s6 w3-button tablink w3-hover-light-blue ${Completed} ">
                                <strong class="font-login"><i><fmt:message key="order.finished"/></i></strong>
                            </a>
                        </div>
                        <tr class="w3-card-2 w3-cell-row w3-light-blue">
                            <th class="w3-cell-middle w3-col s1 w3-center">
                                <fmt:message key="order.num"/>
                            </th>
                            <th class="w3-cell-middle w3-col s3">
                                <a href="ServletController?action=management&sort=customer"
                                   style="text-decoration: none">
                                    <fmt:message key="order.customer"/>
                                    <i class="material-icons w3-xlarge w3-text-grey w3-circle  w3-cell-middle">&#xe053;</i>
                                </a>
                            </th>
                            <th class="w3-cell-middle w3-col s2">
                                <a href="ServletController?action=management&sort=price"
                                   style="text-decoration: none">
                                    <fmt:message key="order.price"/>
                                    <i class="material-icons w3-xlarge w3-text-grey w3-circle w3-cell-middle">&#xe053;</i>
                                </a>
                            </th>
                            <th class="w3-cell-middle w3-col s2 w3-center"><fmt:message key="order.dish.name"/></th>
                            <th class="w3-cell-middle w3-col s2 w3-center">
                                <a href="ServletController?action=management&sort=status"
                                   style="text-decoration: none">
                                    <fmt:message key="order.status"/>
                                    <i class="material-icons w3-xlarge w3-text-grey w3-circle w3-cell-middle">&#xe053;</i>
                                </a>
                            </th>
                            <th class="w3-cell-middle w3-col s1 w3-right-align"></th>
                            <th class="w3-cell-middle w3-col s1 w3-right"></th>
                        </tr>
                        <c:if test="${orderManagement[0].custom != null}">
                            <c:forEach var="orderData" items="${orderManagement}" varStatus="num"
                                       begin="${(pageNum-1) * pagesRecs}" end="${pageNum * pagesRecs - 1}">
                                <tr class="w3-card-2">
                                    <td class="w3-cell-middle w3-col s1 w3-center">${num.index + 1}</td>
                                    <td class="w3-cell-middle w3-col s3">${orderData.user.name} ${orderData.user.lastName}</td>
                                    <td class="w3-cell-middle w3-col s2">
                                        <c:set var="currSymb"><fmt:message key="currency"/></c:set>
                                        <fmt:formatNumber type="CURRENCY" value="${orderData.custom.cost}"
                                                          currencySymbol="${currSymb}"/>
                                    </td>
                                    <td class="w3-cell-middle w3-col s2 w3-center ">
                                        <div class="w3-button w3-hover-shadow w3-round-large">
                                        <c:set var="orderDetailsText"><fmt:message key="order.details"/></c:set>
                                        <my:ModalDishInOrder orderData="${orderData}" divNum="${num.count}"
                                                             buttonText="${orderDetailsText}..."/>
                                        </div>
                                    </td>
                                    <td class="w3-cell-bottom w3-col s2 w3-center">
                                        <c:if test="${inProgress != 'false' }">
                                            <select id="statusSelect" name="statusSelect"
                                                    class="w3-dropdown-hover w3-light-grey">
                                                <option value="${orderData.status.id}" selected disabled>
                                                    <fmt:message key="${orderData.status.name}"/>
                                                </option>
                                                <c:forEach var="status" varStatus="statNum" items="${statusNames}">
                                                    <c:if test="${status.id != orderData.status.id}">
                                                        <option value="${status.id} ${orderData.custom.id}">
                                                            <fmt:message key="${status.name}"/></option>
                                                    </c:if>
                                                </c:forEach>
                                            </select>
                                        </c:if>
                                        <c:if test="${inProgress == 'false' }">
                                            <fmt:message key="${orderData.status.name}"/>
                                        </c:if>
                                    </td>
                                    <c:if test="${inProgress != 'false' }">
                                        <td class="w3-cell-middle w3-col s1 w3-right-align">
                                            <a href="ServletController?action=orderEditManagement&value=${orderData.custom.id}">
                                                <i class="material-icons w3-xxlarge w3-text-grey w3-hover-shadow w3-circle">edit</i>
                                            </a>
                                        </td>
                                        <td class="w3-cell-middle w3-col s1 w3-right">
                                            <my:DeleteForeverButton commandName="management"
                                                                    idToDelete="${orderData.custom.id}"/>
                                        </td>
                                    </c:if>
                                    <c:if test="${inProgress == 'false'}">
                                        <td class="w3-cell-middle w3-col s1 w3-right-align">
                                            <form name="PDFForm" id="PDFForm" method="post"
                                                  action="ServletController?action=pdf&value=${orderData.custom.id}">
                                                <button type="submit" name="pdfButton" form="PDFForm"
                                                        class="w3-hover-none w3-border-0" style="background: transparent">
                                                    <i class="material-icons w3-xxlarge w3-text-grey w3-hover-shadow">
                                                        picture_as_pdf
                                                    </i>
                                                </button>
                                            </form>
                                        </td>
                                        <td class="w3-cell-middle w3-col s1 w3-right w3-hover-none">
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </c:if>
                        <c:if test="${orderManagement[0].custom == null}">
                            <tr class="w3-card-2 w3-cell-row w3-pale-yellow">
                                <th class="w3-cell-middle w3-center">
                                    <h3><fmt:message key="order.fail"/></h3>
                                </th>
                            </tr>
                        </c:if>
                    </table>
                </div>

                <c:if test="${inProgress != 'false' }">
                    <my:ResetAccessButton lang="${lang}" totalCost="${totalOrdersCost}"/>
                </c:if>
                <my:PaginationButtTag pagesMax="${pagesMax}" pagesMin="${pagesMin}"/>

            </form>
        </c:if>
        <c:if test="${orderManagement == null}">
            <div class="w3-panel w3-pale-yellow w3-display-bottommiddle w3-display-container w3-card-4 w3-round">
                   <span onclick="this.parentElement.style.display='none'"
                         class="w3-button w3-margin-right w3-display-right w3-round-large w3-hover-red w3-border w3-border-red w3-hover-border-grey">
                   </span>
                <h5><fmt:message key="order.fail"/></h5>
            </div>
        </c:if>
    </div>
    <my:BackHomeButton/>
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

