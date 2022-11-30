<%@page contentType="text/html" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html lang="${lang}">
<head>
    <meta charset="UTF-8">
    <title>Restaurant EPAM final project</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3my.css">
<%--    <script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>--%>
    <script src="${pageContext.request.contextPath}/views/js/jquery-1.11.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/views/js/hideShoppingCart.js"></script>
    <script src="${pageContext.request.contextPath}/views/js/sidebarHideOnSmall.js"></script>
    <c:if test="${loggedUser != null}">
    <script>
        $(document).ready(function () {
            let shoppingCart = $("#shoppingCart");
            let cartSizeElement = $("#cartSize");
            let cartNum = cartSizeElement.text();

            $(".dish").click(function (e) {
                e.preventDefault();
                if (shoppingCart.is(":hidden")) {
                    // $("#cartSize").text(cartNum++);
                    shoppingCart.show();
                }
                cartSizeElement.text(++cartNum);
            });
        });

    </script>
    </c:if>
</head>

<body class="w3-light-grey ">
<header>
    <!-- Top container -->
    <div class="w3-container w3-bar w3-cell w3-top w3-black" style="z-index:4">

        <div class="w3-bar-item font-effect-fire restik w3-center"><u>Restaurant</u></div>

        <c:if test="${loggedUser == null}">
            <span class="w3-display-topmiddle w3-animate-fading w3-text-light-blue font-login w3-xlarge w3-third"
                  style="margin-top: 18px;">
            <fmt:message key="main.please.login"/>
            </span>
        </c:if>
        <%--    Hidden Menu Button--%>
        <button class="w3-bar-item w3-button w3-hide-large w3-hover-none w3-hover-text-amber"
                onclick="w3_open();"><i class="material-icons">&#xe5d2</i> &nbsp;<fmt:message key="main.menu"/>
        </button>

        <%--    Hidden Menu Button    --%>

        <my:LanguageButton lang="${lang}"/>

        <%--    login and signIn buttons--%>

        <span class="w3-bar-item w3-cell-middle w3-right w3-quarter">
            <a href="ServletController?action=login" class="w3-bar-item w3-hover-none w3-hover-text-amber"
               style="text-decoration: none">
                <c:if test="${empty loggedUser}"><i><fmt:message key="main.login"/></i></c:if>
                <c:if test="${!empty loggedUser}"><i><fmt:message key="main.logout"/></i></c:if>
            </a>
            <i class="w3-bar-item">/</i>
            <a href="ServletController?action=sign_up"
               class="w3-bar-item w3-hover-none w3-hover-text-amber w3-hover-shadow" style="text-decoration: none">
                <i><fmt:message key="main.signup"/></i>
            </a>
    </span>

        <%--    login and signIn buttons end --%>

        <%--    shopping cart--%>

        <%--        <c:if test="${not empty dishIds}">--%>
        <div id="shoppingCart" class="w3-bar-item w3-right w3-hover-amber" style="margin-top: 5px">
            <a href="ServletController?action=orderEdit" style="margin-bottom: 35px; text-decoration: none">
                <i class="material-icons w3-border-white">shopping_cart</i>
                <i id="cartSize">${dishIds.size()}</i>
            </a>
        </div>
        <%--        </c:if>--%>

        <%--shopping cart ends--%>
    </div>
</header>

<!-- Sidebar/menu -->

<nav class="w3-sidebar w3-collapse w3-white w3-bar-block" style="z-index:3;width:300px;" id="mySidebar"><br>
    <div class="w3-container w3-cell-row">
        <div class="w3-cell w3-col s4 w3-cell-middle">
            <c:if test="${loggedUser!=null}">
                <img src="${pageContext.request.contextPath}/views/images/smile.png"
                     class="w3-circle w3-margin-right " style="width:86px" alt=":/">
            </c:if>
            <c:if test="${loggedUser==null}">
                <img src="${pageContext.request.contextPath}/views/images/question_mark.png"
                     class="w3-circle w3-margin-right" style="width:86px" alt=":/">
            </c:if>
        </div>
        <div class="w3-cell w3-col s8 w3-center font-login w3-xxlarge">
            <fmt:message key="main.welcome"/>,<br>
            <c:set var="userSet"><fmt:message key="main.user"/></c:set>
            <strong>${not empty loggedUser.name ? loggedUser.name : userSet}</strong>
        </div>
    </div>

    <%--    Pending orders info    --%>

    <c:if test="${not empty customsList }">

        <div class="w3-block w3-bar-item w3-dropdown-hover w3-small w3-white">
            <button class="w3-button w3-cursive" title="More">
                <i><fmt:message key="main.order.info"/></i>
                <i class="material-icons w3-small">arrow_drop_down</i>
            </button>
            <div class="w3-dropdown-content w3-bar-block w3-light-grey">
                <c:forEach var="orderData" items="${customsList}" varStatus="counter">
                    <c:set var="statusName" value="${orderData.status.name}"/>
                    <div class="w3-bar-item w3-button  w3-cursive">
                        <c:set var="orderBriefInfo">
                            <fmt:message key="order"/>${counter.count}, <fmt:message key="${statusName}"/>
                            , <fmt:formatDate value="${orderData.custom.createTime}" type="BOTH" dateStyle="short"
                                              timeStyle="short"/>
                        </c:set>
                        <my:ModalDishInOrder orderData="${orderData}" divNum="${counter.count}"
                                             buttonText="${orderBriefInfo}" buttonTextStyle="w3-left-align"/>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:if>
    <c:if test="${empty customsList }">
        <br>
    </c:if>

    <%-- Pending orders info. The end.    --%>

    <div class="w3-container">
        <hr class="w3-border-gray" style="width: 95%; margin: auto">
    </div>

    <!-- Sidebar/menu categories menu list-->

    <div class="w3-container">
        <h4 class="font-login"><strong><fmt:message key="main.dish.category"/></strong></h4>
    </div>
    <div class="w3-container">
        <hr class="w3-border-gray" style="width: 95%; margin: auto">
    </div>
    <div class="w3-bar-block">
        <a href="#" class="w3-bar-item w3-button w3-padding-16 w3-hide-large w3-blue w3-hover-amber"
           onclick="w3_close()" title="close menu"><i class="material-icons">&#xe5cd;</i>&nbsp;
            <fmt:message key="main.close.menu"/>
        </a>
        <jsp:include page="views/categoryList.jsp"/>
        <br><br>
    </div>
</nav>

<jsp:include page="views/footer.jsp"/>

<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large w3-animate-opacity" onclick="w3_close()"
     style="cursor:pointer" title="close side menu" id="myOverlay"></div>

<!-- !PAGE CONTENT! -->
<div class="w3-main" style="margin-left:300px;margin-top:50px">

    <!-- Header -->
    <div class="w3-container w3-margin-left w3-bar">

        <%--    Selector of sorting    --%>

        <c:if test="${dishesNames != null}">
            <div class="w3-cell w3-cell-middle w3-dropdown-hover w3-right w3-medium w3-light-grey  w3-cursive">
                <button class="w3-button w3-cursive" title="More">
                    <i><fmt:message key="main.sort"/>&nbsp;</i>
                    <i class="material-icons w3-small">arrow_drop_down</i>
                </button>
                <div class="w3-dropdown-content w3-bar-block ">
                    <a href="ServletController?action=sortDishes&value=name" class="w3-bar-item w3-button">
                        <i><fmt:message key="main.sort.name"/></i>
                    </a>
                    <a href="ServletController?action=sortDishes&value=cost" class="w3-bar-item w3-button">
                        <i><fmt:message key="main.sort.cost"/></i>
                    </a>
                    <a href="ServletController?action=sortDishes&value=category" class="w3-bar-item w3-button">
                        <i><fmt:message key="main.sort.category"/></i>
                    </a>
                </div>
            </div>
            <%--    End Selector of sorting    --%>
        </c:if>
    </div>
    <%-- main context--%>
    <div class="w3-cursive" style="margin-bottom: 65px">
        <jsp:include page="/views/dishesList.jsp"/>
    </div>
</div>
<!-- End page content -->
</body>
</html>