<%--
  Created by IntelliJ IDEA.
  User: maxim
  Date: 25.08.22
  Time: 18:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<head>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Sofia&effect=fire">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Caveat:wght@700&display=swap">
    <style>
        .restik {
            font-family: "Sofia", sans-serif;
            font-size: 25px;
        }

        body,h1,h5 {font-family: "Raleway", sans-serif}
        body, html {height: 100%}
        .bgimg {
            background-image: url('${pageContext.request.contextPath}/views/images/serverovka_stola.jpg');
            min-height: 100%;
            background-position: center;
            background-size: cover;
        }
        .font-login {
            font-family: 'Caveat', cursive;
            font-size: 30px;
        }
    </style>
</head>
<header>
    <div class="w3-container w3-black w3-left-align font-effect-fire restik ">
        <h3 class="restik">Restaurant</h3>
    </div>

<%--    <div class="w3-container w3-bar w3-top w3-black" >--%>

<%--        <div class="w3-bar-item font-effect-fire restik w3-center">Restaurant</div>--%>

<%--        &lt;%&ndash;    Hidden Menu Button&ndash;%&gt;--%>

<%--    </div>--%>
</header>

