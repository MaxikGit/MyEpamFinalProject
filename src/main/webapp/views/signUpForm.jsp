<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<html lang="${lang}">

<head>
    <title>Registration form</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3my.css">
<%--    <script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>--%>
    <script src="${pageContext.request.contextPath}/views/js/jquery-1.11.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/views/js/signInValidation.js"></script>
    <script src="${pageContext.request.contextPath}/views/js/hideShowPass.js"></script>
</head>

<body class="w3-light-grey bgimg">
<jsp:include page="header.jsp"/>

<div class="w3-center w3-third w3-display-topmiddle w3-card-2 w3-round-large w3-border-black w3-light-grey w3-animate-opacity"
     style="margin-top: 80px">
    <form id="reg_form" action="${pageContext.request.contextPath}/ServletController" method="post" name="register">
        <div class="w3-container w3-black w3-bottombar w3-padding w3-round-large-up">
            <span class="w3-margin font-login"><fmt:message key="signup.page"/></span>
        </div>
        <div class="w3-container w3-border-0">
            <c:set var="namePlace"><fmt:message key="signup.name.place"/></c:set>
            <c:set var="lastNamePlace"><fmt:message key="signup.lastname.place"/></c:set>
            <c:set var="emailPlace"><fmt:message key="signup.email.place"/></c:set>
            <c:set var="passPlace"><fmt:message key="signup.pass.place"/></c:set>
            <c:set var="passRepeat"><fmt:message key="signup.pass.repeat"/></c:set>
            <label class="w3-left w3-margin-top" for="name">
                <b><fmt:message key="signup.name"/></b>
            </label>
            <input type="text" placeholder="${namePlace}" name="name" id="name" required
                   minlength="2" maxlength="30" class="w3-input w3-border w3-hover-border-light-gray">

            <label class="w3-left w3-margin-top" for="last_name">
                <b><fmt:message key="signup.lastname"/></b>
            </label>
            <input type="text" placeholder="${lastNamePlace}" name="last_name" id="last_name"
                   minlength="1" maxlength="30" required class="w3-input w3-hover-border-light-gray">

            <label class="w3-left w3-margin-top" for="email">
                <b><fmt:message key="signup.email"/></b>
            </label>
            <input type="email"
                   placeholder="<c:out value="${sessionScope.email}" default="${emailPlace}"/>"
                   name="email" id="email" required
                   class="w3-input w3-hover-border-light-gray">

            <label class="w3-left w3-margin-top" for="password">
                <b><fmt:message key="signup.pass"/></b>
                <i class="toggle-password w3-button material-icons w3-medium">visibility_off</i>
            </label>
            <input type="password" placeholder="${passPlace}" name="password" id="password"
                   maxlength="12" minlength="4" required
                   class="w3-input w3-hover-border-light-gray">
            <label class="w3-left w3-margin-top" for="re-password"><b>${passRepeat}</b>
                <button type="button" class="toggle-password w3-button material-icons w3-medium">visibility_off</button>
            </label>
            <input type="password" placeholder="${passRepeat}" id="re-password"
                   required class="w3-input w3-hover-border-light-gray ">
        </div>
        <%--                    <span id="message2" style="color:red"> </span>--%>
        <div class="w3-container">
            <%--                By creating an account you agree to our <a href="#">Terms & Privacy</a>--%>
            <button type="submit"
                    class="w3-btn w3-block w3-margin-top w3-margin-bottom w3-round-large w3-gray w3-opacity-min"
                    name="action" value="sign_up"><fmt:message key="signup.enter"/>
            </button>

            <div class="container">
                <fmt:message key="signup.haveacc"/>?
                <a href="${pageContext.request.contextPath}/ServletController?action=login">
                    <fmt:message key="login.enter"/>
                </a>
            </div>
        </div>
    </form>
</div>
<div class="w3-margin">
    <c:if test="${!empty sessionScope.unsuccess}">
        <div class="w3-round-large w3-display-topmiddle w3-padding-large w3-pale-yellow w3-card-4">
            <fmt:message key="${sessionScope.unsuccess}"/></div>
    </c:if>
    <div id="errorname" class="w3-round-large w3-display-topmiddle w3-padding-large w3-pale-yellow w3-card-4">
        <fmt:message key="signup.err.name"/>
    </div>
    <div id="errorlastname" class="w3-round-large w3-display-topmiddle w3-padding-large w3-pale-yellow w3-card-4">
        <fmt:message key="signup.err.last_name"/>
    </div>
    <div id="erroremail" class="w3-round-large w3-display-topmiddle w3-padding-large w3-pale-yellow w3-card-4">
        <fmt:message key="signup.err.email"/>
    </div>
    <div id="errorpass" class="w3-round-large w3-display-topmiddle w3-padding-large w3-pale-yellow w3-card-4">
        <fmt:message key="signup.err.pass"/>
    </div>
    <div id="errorconpass" class="w3-round-large w3-display-topmiddle w3-padding-large w3-pale-yellow w3-card-4">
        <fmt:message key="signup.err.repass"/>
    </div>

    <myTag:BackHomeButton/>
</div>
<jsp:include page="footer.jsp"/>
<%--<script>--%>
<%--    //you need to add class = "toggle-password class" to the element, to make it works--%>
<%--    $(document).ready(function () {--%>
<%--        $('.toggle-password').click(function () {--%>
<%--            if ($(this).text() == 'visibility_off')--%>
<%--                $(this).text('visibility');--%>
<%--            else $(this).text('visibility_off');--%>
<%--    //tag with 'eye' element should be inside another tag, input tag had to be next then--%>
<%--            let input = $(this).parent().next();--%>
<%--            input.attr('type', input.attr('type') === 'password' ? 'text' : 'password');--%>
<%--        })--%>
<%--    });--%>
<%--</script>--%>
</body>
</html>
