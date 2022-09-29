<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/font-awesome.min.css">

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Sofia&effect=fire">

</head>
<body class="w3-light-grey bgimg">

<jsp:include page="header.jsp"/>

<%--avtorization--%>

<div class="w3-display-container w3-center w3-padding ">
    <div class="w3-auto w3-display-middle w3-third " style="margin-top: 250px">
        <div class="w3-card-4 w3-centered w3-round-large w3-light-grey w3-animate-opacity">
            <div class="w3-container w3-round-large-up w3-black w3-border-black ">
                <h2 class="font-login">Авторизація</h2>
            </div>
            <%--        input form--%>
            <form class="w3-container w3-margin" action="${pageContext.request.contextPath}/AuthorisationServlet" method="post">
                <label class="w3-left w3-margin-top">Введите Ваш имейл</label>
                <input class="w3-input w3-border w3-hover-border-light-gray " type="email" name="email"
                       value="<c:out value="${sessionScope.email}" default="klimenko@gmail.com"/>" placeholder="Введите Ваш имейл" required>
                <label class="w3-left w3-margin-top">Введите Ваш пароль</label>
                <input class="w3-input w3-border w3-hover-border-light-gray w3-round-medium" type="password"
                       name="password" value="Klimenko" placeholder="Введите пароль" required>
                <button class="w3-btn w3-block w3-margin-top w3-margin-bottom w3-round-large w3-gray w3-opacity-min" type="submit" name="action" value="login">
                    Войти
                </button>
            </form>

        </div>
    </div>

</div>

<%--Wrong input line--%>
<div class="w3-container w3-center">
<c:if test="${!empty sessionScope.unsuccess}">
    <div class="w3-round-large w3-padding-large w3-pale-yellow w3-button w3-hover-none">${sessionScope.unsuccess}</div>
</c:if>
<myTag:BackHomeButton/>
</div>
<jsp:include page="footer.jsp"/>

</body>
</html>