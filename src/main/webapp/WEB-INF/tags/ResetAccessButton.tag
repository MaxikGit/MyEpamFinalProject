<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="lang" type="java.lang.String" required="true" %>
<%@attribute name="totalCost" type="java.lang.Double" required="true" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>


    <div class="w3-cell-row w3-half w3-right">
        <h5>
            <button name="accepted" value="no" type="reset"
                    class="w3-button w3-card-4 w3-third w3-pale-red w3-hover-red w3-round-large">
                <fmt:message key="order.reset"/>
            </button>

            <a class="w3-third w3-hover-none"><fmt:message key="order.total"/>:
                <c:set var="currSymb"><fmt:message key="currency"/></c:set>
                <fmt:formatNumber type="CURRENCY" value="${totalCost}" currencySymbol="${currSymb}"/>
            </a>
            <button name="accepted" value="yes" type="submit"
                    class="w3-button w3-card-4 w3-third w3-pale-green w3-hover-green w3-round-large">
                <fmt:message key="order.accept"/>
            </button>
        </h5>
    </div>