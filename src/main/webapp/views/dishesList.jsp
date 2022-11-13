<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<head>
    <title>Dishes list</title>
    <script src="views/js/SendURLbyGet.js"></script>
</head>

<body>
<c:if test="${dishesNames != null}">
    <c:forEach var="dishEntry" items="${dishesNames}" varStatus="iterations">
        <c:if test="${iterations.count % 4 == 1}">
            <div class="w3-row-padding w3-padding-16 w3-center">
        </c:if>
        <div class="dish w3-quarter w3-padding w3-animate-opacity" id="dish#${dishEntry.id}"
             onclick="sendURLbyGet('ServletController?action=orderEdit&value=${dishEntry.id}')">
            <i>
                <c:if test="${loggedUser!=null}">
                    <i class="is-user"></i>
                    <%--                <a href="ServletController?action=orderEdit&value=${dishEntry.id}">--%>
                    <%--                <a href="#" onclick="loadDoc('ServletController?action=orderEdit&value=${dishEntry.id}')">--%>
                </c:if>

                <img src="${pageContext.request.contextPath}${dishEntry.imagePath}"
                     style="width:95%" alt="food picture" class="w3-round-large">
                <h3 class="w3-left-align" style="height: 65px">${dishEntry.name}</h3>

                <div>
                    <hr class="w3-border w3-border-gray w3-gray w3-center" style="margin: auto;">
                    <h5 class="w3-right-align w3-margin-right">
                        <c:set var="currSymb">
                            <fmt:message key="currency"/>
                        </c:set>
                        <fmt:formatNumber type="CURRENCY" value="${dishEntry.price}" currencySymbol="${currSymb}"/>
                        <c:if test="${loggedUser!=null}">
                            <i class="material-icons w3-circle w3-hover-amber w3-animate-fading">restaurant_menu</i>
                        </c:if>
                    </h5>
                </div>
                <p>${dishEntry.details}</p>
                <c:if test="${loggedUser!=null}">
                    <%--                </a>--%>
                </c:if>
            </i>
        </div>
        <c:if test="${iterations.count % 4 == 0}">
            </div>
        </c:if>
    </c:forEach>
</c:if>

<%--for the first enter, button with GREETINGS--%>

<c:if test="${dishesNames == null}">

    <a href="ServletController?action=category" style="text-decoration:none">
        <div class="w3-padding w3-display-middle w3-light-blue w3-round-large w3-third  w3-card-4 w3-hover-amber">
            <div class="w3-container  w3-padding-16 w3-margin w3-jumbo w3-center w3-animate-top w3-card-4 w3-round-large w3-hover-light-blue">
                <hr class="w3-border w3-border-white w3-center" style="margin: auto; width: 80%">
                <h1 class="w3-text-blue-gray">GREETINGS!</h1>
                <hr class="w3-border w3-border-white w3-center" style="margin: auto; width: 60%">
                <h3 class="w3-center w3-animate-top w3-text-blue-gray">& let`s go!</h3>
            </div>
        </div>
    </a>
</c:if>

<%-- end of button with GREETINGS--%>

</body>
