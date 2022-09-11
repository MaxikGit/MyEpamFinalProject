<%--
  User: maxim
  Date: 18.08.22
  Time: 12:30
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

            <%--            <p>Type: <%= excep%>--%>
            <%--            </p>--%>
            <%--            <hr>--%>
            <%--            <div class="w3-card-4 w3-margin w3-border-light-green">--%>
            <%--                Message: <%= message %>--%>

            <%--            </div>--%>
        </div>
    </div>
</div>
<div class="w3-margin">
    <div class="w3-container w3-grey w3-opacity w3-right-align w3-padding w3-round-medium">
        <button class="w3-btn w3-round-large" onclick="location.href='${pageContext.request.contextPath}/'">Back to
            main
        </button>

    </div>

    <jsp:include page="footer.jsp"/>
</div>
</body>
</html>