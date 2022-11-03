<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>
<head>
    <title>
        Header on Restaurant pages
    </title>
    <style>
        .bgimg {
            background-image: url('${pageContext.request.contextPath}/views/images/serverovka_stola.jpg');
            min-height: 100%;
            background-position: center;
            background-size: cover;
        }
    </style>
</head>
<header>
    <div class="w3-container w3-bar w3-black" style="z-index: 4">

        <div class="w3-bar-item font-effect-fire restik w3-center">
            <a href="${pageContext.request.contextPath}/ServletController">
                <span class="restik"><u>Restaurant</u></span>
            </a>
        </div>
        <my:LanguageButton lang="${lang}"/>
    </div>

</header>

