<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@attribute name="lang" type="java.lang.String" required="true" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<div class="w3-button w3-right w3-bar-item w3-circle w3-amber w3-card-2" style="margin-top: 5px">
    <a href="${pageContext.request.contextPath}/ServletController?action=language&lang=${lang}" style="text-decoration: none">
        <i class="w3-medium"><fmt:message key="language"/></i>
    </a>
</div>