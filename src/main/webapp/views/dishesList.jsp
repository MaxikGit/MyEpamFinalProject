<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users list</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/font-awesome.min.css">--%>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Amatic+SC">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <style>
        body,h1,h2,h3,h4,h5,h6 .w3-font{font-family: "Amatic SC", sans-serif}
    </style>
</head>

<body>
<fmt:setLocale value="uk_UA" />
<c:if test="${dishesNames != null}">
    <c:forEach var="dish" items="${dishesNames}" varStatus="iterations">
        <c:if test="${iterations.count % 4 == 1}">
            <div class="w3-row-padding w3-padding-16 w3-center">
        </c:if>
        <div class="w3-quarter w3-padding w3-animate-opacity">
            <img src="${pageContext.request.contextPath}${dish.imagePath}"
                 style="width:95%" alt="food picture" class="w3-round-large">
            <h3 class="w3-left-align"><c:out value="${dish.name}" default="едьба"/></h3>
            <p/>
            <hr class="w3-border w3-border-black w3-cente" style="margin: auto;">
            <h5 class="w3-right-align w3-margin-right">
                <fmt:formatNumber type="CURRENCY" value="${dish.price}"/>
                <c:if test="${loggedUser!=null}">

            <a href="AuthorisationServlet?action=order&value=${dish.id}"
               class=""><i class="material-icons w3-hover-text-greenn">&#xe561;</i>
            </a>
                </c:if>
            </h5>
            <p>details...</p>
        </div>
        <c:if test="${iterations.count % 4 == 0}">
            </div>
        </c:if>
    </c:forEach>
</c:if>

<%--for the first enter, button with GREETINGS--%>

<c:if test="${dishesNames == null}">

    <a href="AuthorisationServlet?action=category" style="text-decoration:none">
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

<%--for the first enter, button with GREETINGS--%>


</body>
</html>
