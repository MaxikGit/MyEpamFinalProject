<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="altName" type="java.lang.String" required="false" %>
<%@ attribute name="altPath" type="java.lang.String" required="false" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<c:set var="backHome"><fmt:message key="back.home"/></c:set>
<div class="w3-container w3-bottom w3-grey w3-opacity w3-padding w3-round-medium w3-right-align" style="margin-bottom:55px">
    <button class="w3-btn w3-round-large"
            onclick="location.href='${pageContext.request.contextPath}<c:out value="${altPath}" default="/"/>'">
        <c:out value="${altName}" default="${backHome}"/>
    </button>
</div>