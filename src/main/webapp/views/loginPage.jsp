<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/font-awesome.min.css">
</head>
<body class="w3-light-grey">

<jsp:include page="header.jsp"/>

<%--avtorization--%>

<div class="w3-container w3-center w3-margin w3-padding w3-display-container">
    <div class="w3-auto ">
        <div class="w3-card-4 w3-centered">
            <div class="w3-container w3-round-large-up w3-light-blue w3-border-light-blue ">
                <h2>Авторізація</h2>
            </div>
            <%--        input form--%>
            <form class="w3-container w3-margin" action="${pageContext.request.contextPath}/AuthorisationServlet" method="post">
                <label class="w3-left w3-margin-top">Введите Ваш имейл</label>
                <input class="w3-input w3-border w3-hover-border-light-gray " type="email" name="email"
                       value="<c:out value="${sessionScope.email}" default="klimenko@gmail.com"/>" placeholder="Введите Ваш имейл" required>
                <label class="w3-left w3-margin-top">Введите Ваш пароль</label>
                <input class="w3-input w3-border w3-hover-border-light-gray w3-round-medium" type="password"
                       name="password" value="Klimenko" placeholder="Введите пароль" required>
                <button class="w3-btn w3-block w3-margin-top w3-margin-bottom w3-round-large" type="submit" name="action" value="login">
                    Войти
                </button>
            </form>

        </div>
    </div>
</div>
<div class="w3-container w3-grey w3-opacity w3-padding w3-round-large w3-center">
<%--<div class="w3-container w3-grey w3-opacity w3-right-align w3-padding">--%>
    <span class="w3-text-white w3-hover-text-amber w3-bar-block">
        <c:out value="${sessionScope.unsuccess}"/>
    </span>
    <button class="w3-btn w3-round-large w3-right" onclick="location.href='${pageContext.request.contextPath}/'">Back to main
    </button>
</div>
<jsp:include page="footer.jsp"/>

</body>
</html>