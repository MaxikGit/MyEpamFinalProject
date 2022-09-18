<%@attribute name="commandName" type="java.lang.String" required="true" %>
<%@attribute name="idToDelete" type="java.lang.Integer" required="true" %>


<a href="${pageContext.request.contextPath}/AuthorisationServlet?action=${commandName}&deleteId=${idToDelete}">
    <i class="material-icons w3-xxxlarge w3-text-grey w3-hover-shadow w3-circle">&#xe92b;</i>
</a>