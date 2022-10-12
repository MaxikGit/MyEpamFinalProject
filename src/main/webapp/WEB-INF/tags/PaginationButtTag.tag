<%@tag import="com.max.restaurant.utils.UtilsCommandNames" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="pagesMax" required="true" type="java.lang.Integer" %>
<%@ attribute name="pagesMin" required="true" type="java.lang.Integer" %>

<c:if test="${pagesMax > 1}">
    <div class="w3-center">
        <div class="w3-bar">
            <button type="submit" name="pageNum" value="${pagesMin - 1}"
                    class="w3-bar-item w3-button">&laquo;
            </button>
            <c:forEach begin="${pagesMin}" end="${pagesMax}"
                       varStatus="num">
                <input type="submit" name="pageNum" value="${num.index}"
                       class="w3-button">
            </c:forEach>
            <button type="submit" name="pageNum" value="${pagesMax + 1}"
                    class="w3-button">&raquo;
            </button>
        </div>
    </div>
</c:if>