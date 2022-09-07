<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="com.max.restaurant.controller.command.UtilsCommandNames.*" %>
<%@ page import="com.max.restaurant.model.entity.User" %><%--
  Created by IntelliJ IDEA.
  User: maxim
  Date: 18.08.22
  Time: 12:30
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration form</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
</head>

<body class="w3-light-grey">
<jsp:include page="header.jsp"/>

<div class="w3-container w3-center w3-margin-bottom w3-padding">
    <div class="w3-card-4">
        <div class="w3-container w3-light-blue w3-round-medium">
            <form action="${pageContext.request.contextPath}/AuthorisationServlet" method="post" name="register">

                <div class="w3-container w3-row ">
                    <h2>Register form</h2>
                    <div class="w3-container w3-display-container ">
                        <p>Please fill in this form to create an account</p>
                        <hr>
                        <p><label for="name"><b>Name</b></label>
                            <input type="text" placeholder="Enter Your Name" name="name" id="name" required>
                        </p>
                        <p>
                            <label for="last_name"><b>Last Name</b></label>
                            <input type="text" placeholder="Enter Your Last Name" name="last_name" id="last_name"
                                   required>
                        </p>
                        <p>
                            <label for="email"><b>Email</b></label>
                            <input type="email"
                                   placeholder="<c:out value="${sessionScope.email}" default="Enter Email"/>"
                                   name="email" id="email" required>
                        </p>
                        <p>
                            <label for="password"><b>Password</b></label>
                            <input type="password" placeholder="Enter Password"
                                   title="Must contain from 4 to 12 symbols" name="password" id="password"
                                   maxlength="12" minlength="4" required>
                        </p>
                        <p>
                            <label for="re-password"><b>Repeat Password</b></label>
                            <input type="password" placeholder="Repeat Password" name="re-password" id="re-password"
                                   required>
                            <span id="message2" style="color:red"> </span>
                        </p>
                        <hr>

                        <p>By creating an account you agree to our <a href="#">Terms & Privacy</a>.</p>
                        <button type="submit" class="registerbtn" name="action" value="sign_up">Register</button>
                    </div>
                </div>
                <div class="container signin">
                    <p>Already have an account? <a href="loginPage.jsp">Sign in</a>.</p>
                </div>
            </form>
        </div>
    </div>
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
    <div class="w3-container w3-grey w3-opacity w3-padding w3-round-large w3-center">
        <span class="w3-text-white w3-hover-text-amber w3-bar-block"><c:out value="${sessionScope.unsuccess}" /></span>
        <button class="w3-btn w3-round-large w3-right" onclick="location.href='${pageContext.request.contextPath}/'">Back to main
        </button>
    </div>

    <%--    <jsp:include page="footer.jsp"/>--%>
</div>
<jsp:include page="footer.jsp"/>

</body>

</html>
