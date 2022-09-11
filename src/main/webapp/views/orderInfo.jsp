<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Order list</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
</head>

<body class="w3-light-grey w3-opacity-min">
<jsp:include page="header.jsp"/>
<div class="w3-container w3-light-blue w3-border-light-blue w3-center">
    <h3>Order details</h3>
</div>
<div class="w3-container w3-center w3-margin-bottom">
    <c:if test="${orderNames != null}">
        <form method="post" action="${pageContext.request.contextPath}/AuthorisationServlet">
            <div class="w3-container w3-margin">
                <table class="w3-table-all w3-hoverable w3-center w3-light-blue w3-margin-bottom">
                    <tr class="w3-card-2 w3-cell-row w3-hover-none w3-light-blue">
                        <th class="w3-cell-middle w3-col s2 w3-center">id#</th>
                        <th class="w3-cell-middle w3-quarter">Name</th>
                        <th class="w3-cell-middle w3-col s2">Price</th>
                        <th class="w3-cell-middle w3-col s2 w3-center">Image</th>
                        <th class="w3-cell-middle w3-col s1 w3-center">Quantity</th>
                        <th class="w3-cell-middle w3-col s2 w3-center"></th>
                    </tr>
                    <c:forEach var="dish" items="${orderNames}" varStatus="num">
                        <tr class="w3-card-2 w3-cell-row">
                            <td class="w3-cell-middle w3-col s2 w3-center">${num.count}</td>
                            <td class="w3-cell-middle w3-quarter">${dish.name}</td>
                            <td class="w3-cell-bottom w3-col s2">${dish.price}</td>
                            <td class="w3-cell-bottom w3-col s2 w3-center">
                                <img src="${pageContext.request.contextPath}${dish.imagePath}"
                                     class="w3-circle" style="height:65px" alt="food picture">
                            </td>
                            <td class="w3-cell-bottom w3-col s1 w3-center">
                                <input name="quantity" type="number" max="10" min="1" value="1"
                                       class="w3-input w3-border-0"/>
                            </td>
                            <td class="w3-cell-bottom w3-col s2 w3-center">
                                <a href="${pageContext.request.contextPath}/AuthorisationServlet?action=orderEdit&deleteId=${dish.id}">
                                    <i class="material-icons w3-xxxlarge w3-text-grey w3-circle">&#xe92b;</i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <div class="w3-cell-row w3-half w3-right">
                <h5>
                    <button name="action" value="orderEdit" type="submit"
                            class="w3-button w3-card-4 w3-third w3-pale-red w3-hover-red w3-round-large">Reject
                    </button>
                    <a class="w3-third w3-hover-none">Total: ${totalCost}</a>
                        <%--                    <button class="w3-button w3-third w3-hover-none">Total: ${totalCost}</button>--%>
                    <button name="action" value="orderEdit" type="submit"
                            class="w3-button w3-card-4 w3-third w3-pale-green w3-hover-green w3-round-large">Accept
                    </button>
                </h5>
            </div>
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
<div class="w3-container w3-grey w3-opacity w3-right-align w3-padding w3-display-container">
    <button class="w3-btn w3-round-large" onclick="location.href='${pageContext.request.contextPath}/'">Back to main
    </button>
</div>
<div class="w3-container" style="height: 65px">
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
