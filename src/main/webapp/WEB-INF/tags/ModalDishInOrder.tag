<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://restaurant.max.com" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytag" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<%@attribute name="orderData" type="com.max.restaurant.model.OrderData" required="true" %>
<%@attribute name="divNum" type="java.lang.Integer" required="true" %>
<%@attribute name="buttonText" type="java.lang.String" required="true" %>
<%@attribute name="buttonTextStyle" type="java.lang.String" required="false" %>

<button onclick="document.getElementById('id0${divNum}').style.display='block'" type="button"
        class="w3-border-0 ${buttonTextStyle}" style="background: transparent">${buttonText}
</button>

<div id="id0${divNum}" class="w3-modal">
    <div class="w3-modal-content w3-card-4 w3-animate-zoom">
        <div class="w3-container w3-ligh">
<span onclick="document.getElementById('id0${divNum}').style.display='none'"
      class="w3-button w3-display-topright">&times;</span>
            <h3 class="w3-left-align w3-margin-left w3-margin-top w3-padding font-login">
                <mytag:OrderCreatedDate custom="${orderData.custom}"/>
            </h3>
        </div>
        <div class="w3-container">

            <div class="w3-container w3-margin ">
                <table class="w3-table-all w3-hoverable w3-center w3-light-blue w3-margin-bottom ">
                    <tr class="w3-cell-row w3-hover-none w3-light-blue w3-cursive">
                        <%--                        column 1 /8.33% --%>
                        <th class="w3-cell-middle w3-col s2 w3-center"><fmt:message key="order.num"/></th>
                        <%--                        column 2 /41.66% --%>
                        <th class="w3-cell-middle w3-col s6"><fmt:message key="order.dish.name"/></th>
                        <%--                        column 3 /16.66%--%>
                        <th class="w3-cell-middle w3-col s2"><fmt:message key="order.price"/></th>
                        <%--                        column 4 /8.33%--%>
                        <th class="w3-cell-middle w3-col s2 w3-center"><fmt:message key="order.quantity"/></th>
                    </tr>

                    <c:forEach var="dishEntry" items="${orderData.dishes}" varStatus="num">
                        <tr class="w3-cell-row">
                                <%--                        column 1--%>
                            <td class="w3-cell-middle w3-col s2 w3-center">${num.count}</td>
                                <%--                        column 2--%>
                            <td class="w3-cell-middle w3-col s6">${dishEntry.key.name}</td>
                                <%--                        column 3--%>
                            <td class="w3-cell-bottom w3-col s2">
                                <c:set var="currSymb"><fmt:message key="currency"/></c:set>
                                <fmt:formatNumber type="CURRENCY" value="${dishEntry.key.price}"
                                                  currencySymbol="${currSymb}"/>
                            </td>
                                <%--                        column 4--%>
                            <td class="w3-cell-middle w3-col s2 w3-center ">${dishEntry.value}</td>
                        </tr>
                    </c:forEach>
                </table>

            </div>
        </div>
    </div>
</div>

<script>
    <%--// Get the modal--%>
    <%--var modal = document.getElementById('id0${divNum}');--%>

    <%--// When the user clicks anywhere outside the modal, close it--%>
    <%--window.onclick = function (event) {--%>
    <%--    if (event.target != modal) {--%>
    <%--        modal.style.display = "none";--%>
    <%--    }--%>
    <%--}--%>
    var modal = document.getElementById('id0${divNum}');
    document.addEventListener(
        "click",

        function (event) {
            // If user either clicks X button OR clicks outside the modal window, then close modal by calling closeModal()
            if (
                event.target.matches(modal) ||
                !event.target.closest(modal)
            ) {
                closeModal();
            }
        },
        false
    );

    function closeModal() {
        modal.style.display = "none";
    }
</script>