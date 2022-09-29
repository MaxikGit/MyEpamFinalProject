<%--
  User: maxim
  Date: 18.08.22
  Time: 12:30
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%--<%--%>
<%--    //    String message = "Imitation of some error";//pageContext.getException().getMessage();--%>
<%--//    String excep = "Very dangerous exception happened! Beware!";//pageContext.getException().getClass().toString();--%>
<%--    String message = pageContext.getException().getMessage();--%>
<%--    String excep = pageContext.getException().getClass().toString();--%>
<%--%>--%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Exception page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
</head>
<body class="w3-light-grey">

<jsp:include page="header.jsp"/>

<div class="w3-container w3-center w3-margin-bottom w3-padding">

    <c:if test="${loggedUser.roleId == 1}">
        <div class="w3-panel w3-red w3-display-container w3-card-4 w3-round">
            <h2>Exception occurred while processing the request</h2>
        </div>
        <div class="w3-card-4 w3-round-large">
            <div class="w3-container  w3-light-blue w3-round-large">
                <h4 class="w3-border-red w3-text-black w3-animate-fading">
                    Exception has status code : ${pageContext.errorData.statusCode}  </h4>
                <hr>
                <div class="w3-card-4 w3-margin w3-border-light-green">
                        <%--                <c:forEach var="stackStrings" items="${pageContext.errorData.throwable.stackTrace}">--%>
                        <%--                <c:forEach var="stackStrings" items="${pageContext.errorData.throwable.cause.stackTrace}">--%>
                        <%--                    <p> Exception in : ${stackStrings}</p>--%>
                        <%--                </c:forEach>--%>
                    <p>Throwable: ${pageContext.errorData.throwable}</p>
                    <p>Localized Message: ${pageContext.errorData.throwable.cause.localizedMessage}</p>
                </div>
                <hr>
                <div class="w3-card-4 w3-margin w3-border-light-green">
                    Stack Trace [0]: ${pageContext.errorData.throwable.cause.stackTrace[0]}
                </div>
                <hr>
                <div class="w3-card-4 w3-margin w3-border-light-green">
                    Exception Message: ${pageContext.exception.message}
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${loggedUser.roleId != 1}">
    <div class="w3-display-container" >
        <div class="w3-display-middle w3-image" style="width: 100%; margin-top: 350px">
        <img class="" src="${pageContext.request.contextPath}/views/images/for_exception_pic.webp" style="width: 100%"/>
        </div>

        <div class="w3-half w3-display-topleft w3-margin w3-animate-opacity">

                <span class="w3-padding-large w3-jumbo w3-text-gray font-login">Oooops!</span>

        </div>
        <div class="w3-half w3-display-topright w3-animate-opacity w3-xlarge font-login">

                <p class="w3-text-dark-gray ">Something went wrong!</p>
                <p class="w3-text-dark-gray">Please stay calm and call our manager!</p>
<%--                <p class="w3-text-dark-gray">call our manager!</p>--%>

        </div>
    </div>
    </c:if>
</div>
<div class="w3-margin w3-center">

    <myTag:BackHomeButton/>

    <jsp:include page="footer.jsp"/>
</div>
</body>
</html>