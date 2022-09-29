<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="custom" type="com.max.restaurant.model.entity.Custom" required="true" %>

Order #${custom.id}, created at <fmt:formatDate value="${custom.createTime}" type="BOTH" dateStyle = "long" timeStyle = "short"/>