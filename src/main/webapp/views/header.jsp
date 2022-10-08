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
        <a href="AuthorisationServlet">
            <h3 class="restik">Restaurant</h3>
        </a>
    </div>

</header>

