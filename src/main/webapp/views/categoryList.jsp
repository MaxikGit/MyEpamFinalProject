<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Users list</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}views/styles/font-awesome.min.css">
</head>

<body>
<a href="AuthorisationServlet?action=category" class="w3-bar-item w3-button w3-padding w3-blue"><i
        class="material-icons">&#xe896;</i>&nbsp;
    Overview</a>
<div class="w3-bar-block">
    <c:if test="${categoryNames != null}">
        <c:forEach var="category" items="${categoryNames}">
            <a href="AuthorisationServlet?action=category&value=${category.id}"
               class="w3-bar-item w3-button w3-padding">
                <i class="fa fa-math fa-fw"></i>&nbsp; ${category.name}</a>
        </c:forEach>
    </c:if>
    <c:if test="${loggedUser != null}">

<%--        Managers only--%>

        <c:if test="${loggedUser.roleId == 1}">
            <hr class="w3-border-gray" style="width: 80%; margin: auto">
            <a href="AuthorisationServlet?action=management"
               class="w3-bar-item w3-button w3-padding">
                <i class="fa fa-math fa-fw"></i>&nbsp; Orders management</a>
        </c:if>
    </c:if>
</div>

</body>
</html>
