<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Registration form</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
</head>

<body class="w3-light-grey w3-display-container bgimg">
<jsp:include page="header.jsp"/>

<div class="w3-center w3-third w3-display-topmiddle w3-card-2 w3-round-large w3-border-black w3-light-grey w3-animate-opacity"
     style="margin-top: 75px">
    <form action="${pageContext.request.contextPath}/AuthorisationServlet" method="post" name="register">
        <div class="w3-container w3-black w3-bottombar w3-padding w3-round-large-up">
            <span class="w3-margin font-login">Register form</span>
            <%--                <p>Please fill in this form to create an account</p>--%>
        </div>
        <div class="w3-container w3-border-0">
            <label class="w3-left w3-margin-top" for="name"><b>Name</b></label>
            <input type="text" placeholder="Enter Your Name" name="name" id="name" required
                   class="w3-input w3-border w3-hover-border-light-gray">

            <label class="w3-left w3-margin-top" for="last_name"><b>Last Name</b></label>
            <input type="text" placeholder="Enter Your Last Name" name="last_name" id="last_name"
                   required class="w3-input w3-hover-border-light-gray">

            <label class="w3-left w3-margin-top" for="email"><b>Email</b></label>
            <input type="email"
                   placeholder="<c:out value="${sessionScope.email}" default="Enter Email"/>"
                   name="email" id="email" required
                   class="w3-input w3-hover-border-light-gray">

            <label class="w3-left w3-margin-top" for="password"><b>Password</b></label>
            <input type="password" placeholder="Enter Password"
                   title="Must contain from 4 to 12 symbols" name="password" id="password"
                   maxlength="12" minlength="4" required
                   class="w3-input w3-hover-border-light-gray">

            <label class="w3-left w3-margin-top" for="re-password"><b>Repeat Password</b></label>
            <input type="password" placeholder="Repeat Password" name="re-password" id="re-password"
                   required class="w3-input w3-hover-border-light-gray">
        </div>
        <%--                    <span id="message2" style="color:red"> </span>--%>

        <div class="w3-container">
            <%--                By creating an account you agree to our <a href="#">Terms & Privacy</a>--%>
            <button type="submit" class="w3-btn w3-block w3-margin-top w3-margin-bottom w3-round-large w3-gray w3-opacity-min"
                    name="action" value="sign_up">Register
            </button>


            <div class="container signin">
                Already have an account? <a href="loginPage.jsp">Sign in</a>
            </div>
        </div>
    </form>

</div>

<%--<div class="w3-container w3-grey w3-opacity w3-right-align w3-padding ">--%>
<%--    <button class="w3-btn w3-round-large" onclick="location.href='${pageContext.request.contextPath}/'">Back to main--%>
<%--    </button>--%>
<%--    &lt;%&ndash;    <a href="/views/table.jsp" id="AuthorizationManager">To table</a>&ndash;%&gt;--%>
<%--    &lt;%&ndash;    <form action="${pageContext.request.contextPath}views/table.jsp" class="w3-animate-right w3-left">&ndash;%&gt;--%>
<%--    &lt;%&ndash;    <button class="w3-btn w3-round-large" onclick="location.href='${pageContext.request.contextPath}/views/table.jsp'">To table</button>&ndash;%&gt;--%>
<%--    &lt;%&ndash;    </form>&ndash;%&gt;--%>
<%--</div>--%>
<div class="w3-margin">
    <%--    <div class="w3-container w3-grey w3-opacity w3-padding w3-round-large w3-center">--%>
    <%--        <span class="w3-text-white w3-hover-text-amber w3-bar-block"><c:out value="${sessionScope.unsuccess}" /></span>--%>
    <%--        <button class="w3-btn w3-round-large w3-right" onclick="location.href='${pageContext.request.contextPath}/'">Back to main--%>
    <%--        </button>--%>
    <%--    </div>--%>
    <c:if test="${!empty sessionScope.unsuccess}">
        <div class="w3-round-large w3-padding-large w3-pale-yellow w3-button w3-hover-none">${sessionScope.unsuccess}</div>
    </c:if>
    <myTag:BackHomeButton/>

</div>
<jsp:include page="footer.jsp"/>

</body>

</html>
