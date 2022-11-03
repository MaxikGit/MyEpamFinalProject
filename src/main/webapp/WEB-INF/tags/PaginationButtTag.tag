<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="pagesMax" required="true" type="java.lang.Integer" %>
<%@ attribute name="pagesMin" required="true" type="java.lang.Integer" %>
<%@ attribute name="pageNum" required="true" type="java.lang.Integer" %>
<%@ attribute name="pagesTotal" required="true" type="java.lang.Integer" %>

<c:if test="${pagesMax > 1}">

    <div class="w3-center">
        <div class="w3-bar">
            <c:if test="${pagesMin != 1}">
                <button type="submit" name="pageNum" value="${pagesMin - 1}"
                        class="w3-bar-item w3-button">&laquo;
                </button>
            </c:if>
            <c:forEach begin="${pagesMin}" end="${pagesMax}"
                       varStatus="num">
                <c:choose>
                    <c:when test="${num.index == pageNum}"><c:set var="color" value="w3-light-blue"/></c:when>
                    <c:otherwise><c:set var="color" value=""/></c:otherwise>
                </c:choose>
                <input type="submit" name="pageNum" value="${num.index}"
                       class="w3-button ${color}">
            </c:forEach>
            <c:if test="${pagesMax != pagesTotal}">
                <button type="submit" name="pageNum" value="${pagesMax + 1}"
                        class="w3-button">&raquo;
                </button>
            </c:if>
        </div>
    </div>
</c:if>