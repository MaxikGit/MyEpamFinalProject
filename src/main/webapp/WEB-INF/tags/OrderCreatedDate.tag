<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="custom" type="com.max.restaurant.model.entity.Custom" required="true" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<fmt:message key="order"/>${custom.id}, <fmt:message key="order.created"/>
<fmt:formatDate value="${custom.createTime}" type="BOTH" dateStyle = "long" timeStyle = "short"/>