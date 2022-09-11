<%--
  Created by IntelliJ IDEA.
  User: maxim
  Date: 25.08.22
  Time: 18:11
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<!-- view-source:https://www.w3schools.com/w3css/tryw3css_templates_analytics.htm -->
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Restaurant EPAM final project</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/w3.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/font-awesome.min.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Amatic+SC">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <style>
        body, h1, h2, h3, h4, h5, h6 .w3-font {
            font-family: "Amatic SC", sans-serif
        }
    </style>
</head>
<body class="w3-light-grey w3-font">

<!-- Top container -->
<div class="w3-bar w3-top w3-black w3-large" style="z-index:4">

    <%--    Hidden Menu Button--%>
    <button class="w3-bar-item w3-button w3-hide-large w3-hover-none w3-hover-text-amber"
            onclick="w3_open();"><i class="material-icons">&#xe5d2</i> &nbsp;Menu
    </button>

    <%--    Hidden Menu Button    --%>

    <span class="w3-bar-item w3-hover-shadow">Restaurant</span>

    <%--    Selector of sorting    --%>

    <%--    login and signIn buttons--%>

    <form class="w3-right" action="AuthorisationServlet" method="get">
        <button name="action" value="login" class="w3-bar-item w3-button w3-hover-none w3-hover-text-amber">
            <i>Login</i>
        </button>
        <i class="w3-bar-item">/</i>
        <button name="action" value="sign_up" class="w3-bar-item w3-button w3-hover-none w3-hover-text-amber">
            <i>Sign Up&nbsp;</i>
        </button>
    </form>

    <%--    login and signIn buttons end --%>

<%--    shopping cart--%>

    <c:if test="${dishIds!=null}">
        <a href="AuthorisationServlet?action=order" class="w3-bar-item w3-hover-amber w3-right">
            <i class="material-icons w3-border-white">&#xe8cc</i>
        </a>
    </c:if>
</div>
<%--shopping cart ends--%>

<!-- Sidebar/menu -->
<nav class="w3-sidebar w3-collapse w3-white w3-animate-opacity" style="z-index:3;width:300px;" id="mySidebar"><br>
    <div class="w3-container w3-row">
        <div class="w3-col s4">
            <c:if test="${loggedUser!=null}">
                <img src="${pageContext.request.contextPath}/views/images/smile.png"
                     class="w3-circle w3-margin-right" style="width:66px" alt=":/">
            </c:if>
            <c:if test="${loggedUser==null}">
                <img src="${pageContext.request.contextPath}/views/images/question_mark.png"
                     class="w3-circle w3-margin-right" style="width:66px" alt=":/">
            </c:if>
        </div>
        <div class="w3-col s8 w3-bar">
            <h3>Welcome, <strong><c:out value="${sessionScope.loggedUser.name}"
                                        default="User"/></strong></h3><br>
        </div>
    </div>
    <hr>
    <!-- Sidebar/menu categories menu list-->
    <div class="w3-container">
        <h3><strong>Dish categories</strong></h3>
    </div>
    <div class="w3-bar-block">
        <a href="#" class="w3-bar-item w3-button w3-padding-16 w3-hide-large w3-blue w3-hover-amber"
           onclick="w3_close()" title="close menu"><i class="material-icons">&#xe5cd;</i>&nbsp; Close Menu</a>

        <jsp:include page="views/categoryList.jsp"/>
        <%--        <jsp:param name="categoryNames"  value="${categoryNames}"/>--%>
        <%--        </jsp:include>--%>


        <br><br>
    </div>
</nav>

<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large w3-animate-opacity" onclick="w3_close()"
     style="cursor:pointer" title="close side menu" id="myOverlay"></div>

<!-- !PAGE CONTENT! -->
<div class="w3-main" style="margin-left:300px;margin-top:43px;">

    <!-- Header -->
    <div class="w3-container w3-margin-left w3-cell-row">
            <h4 class="w3-cell w3-cell-middle"><strong>${sessionScope.categoryNames[categoryId-1].name}</strong></h4>

        <%--    Selector of sorting    --%>
<c:if test="${dishesNames != null}">
        <div class="w3-cell w3-cell-middle w3-dropdown-hover w3-right w3-small w3-light-grey">
            <button class="w3-button " title="More">Sort by order of <i class="material-icons w3-small">&#xe5c5;</i></button>
            <div class="w3-dropdown-content w3-bar-block ">
                <a href="AuthorisationServlet?action=sortDishes&value=name" class="w3-bar-item w3-button">name</a>
                <a href="AuthorisationServlet?action=sortDishes&value=cost" class="w3-bar-item w3-button">cost</a>
                <a href="AuthorisationServlet?action=sortDishes&value=category" class="w3-bar-item w3-button">category</a>
            </div>
        </div>
</c:if>
        <%--    End Selector of sorting    --%>

    </div>
    <%-- main context--%>
    <div class="w3-padding ">
        <jsp:include page="/views/dishesList.jsp"/>
    </div>
</div>

<jsp:include page="views/footer.jsp"/>
<!-- End page content -->

<script>
    // Get the Sidebar
    var mySidebar = document.getElementById("mySidebar");

    // Get the DIV with overlay effect
    var overlayBg = document.getElementById("myOverlay");

    // Toggle between showing and hiding the sidebar, and add overlay effect
    function w3_open() {
        if (mySidebar.style.display === 'block') {
            mySidebar.style.display = 'none';
            overlayBg.style.display = "none";
        } else {
            mySidebar.style.display = 'block';
            overlayBg.style.display = "block";
        }
    }

    // Close the sidebar with the close button
    function w3_close() {
        mySidebar.style.display = "none";
        overlayBg.style.display = "none";
    }
</script>
</body>
</html>

<%--        <div class="w3-cell w3-cell-middle w3-right w3-margin-right ">--%>
<%--            <div class="w3-cell-row w3-cell-middle w3-small w3-button w3-hover-none">--%>
<%--                <form action="AuthorisationServlet" method="get" name="sortType" >--%>
<%--                    <strong>--%>
<%--                        <label for="sortDishes" class="w3-cell  ">Sort by order of&nbsp</label>--%>
<%--                        <select name="sortDishes" id="sortDishes" onchange="this.form.submit()"--%>
<%--                                class="w3-cell w3-left-align w3-light-grey w3-border-light-gray w3-hover-border-light-grey">--%>
<%--                            <option value="" selected disabled>None</option>--%>
<%--                            <option value="name" ><a href="AuthorisationServlet?action=sort">am name</a></option>--%>
<%--                            <option value="cost">cost</option>--%>
<%--                            <option value="category">category</option>--%>
<%--                        </select>--%>
<%--                    </strong>--%>
<%--                </form>--%>
<%--            </div>--%>
<%--        </div>--%>