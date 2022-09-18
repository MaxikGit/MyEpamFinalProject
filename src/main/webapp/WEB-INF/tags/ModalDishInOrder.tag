<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@attribute name="orderData" type="com.max.restaurant.model.OrderData" required="true" %>
<%@attribute name="divNum" type="java.lang.Integer" required="true" %>


<button onclick="document.getElementById('id0${divNum}').style.display='block'" type="button"
        class="w3-button w3-hover-shadow w3-round-large"> Oder details...
</button>

<div id="id0${divNum}" class="w3-modal">
    <div class="w3-modal-content w3-card-4 w3-animate-zoom">
        <div class="w3-container w3-ligh">
        <span onclick="document.getElementById('id0${divNum}').style.display='none'"
              class="w3-button w3-display-topright">&times;</span>
            <h3 class="w3-left-align w3-margin-left w3-margin-top" >Order #${orderData.custom.id},
                created at <fmt:formatDate value="${orderData.custom.createTime}" type="BOTH" dateStyle = "long" timeStyle = "short"/> </h3>
        </div>
        <div class="w3-container">

            <div class="w3-container w3-margin ">
                <table class="w3-table-all w3-hoverable w3-center w3-light-blue w3-margin-bottom ">
                    <tr class="w3-cell-row w3-hover-none w3-light-blue">
                        <%--                        column 1 /8.33% --%>
                        <th class="w3-cell-middle w3-col s2 w3-center">id#</th>
                        <%--                        column 2 /41.66% --%>
                        <th class="w3-cell-middle w3-col s6">Dish</th>
                        <%--                        column 3 /16.66%--%>
                        <th class="w3-cell-middle w3-col s2">Price</th>
                        <%--                        column 4 /8.33%--%>
                        <th class="w3-cell-middle w3-col s2 w3-center">Quantity</th>
                    </tr>

                    <c:forEach var="dishEntry" items="${orderData.dishes}" varStatus="num">
                        <tr class="w3-cell-row">
                                <%--                        column 1--%>
                            <td class="w3-cell-middle w3-col s2 w3-center">${num.count}</td>
                                <%--                        column 2--%>
                            <td class="w3-cell-middle w3-col s6">${dishEntry.key.name}</td>
                                <%--                        column 3--%>
                            <td class="w3-cell-bottom w3-col s2">
                                <fmt:setLocale value="uk_UA"/>
                                <fmt:formatNumber type="CURRENCY" value="${dishEntry.key.price}" currencySymbol="UAH"/>
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
    // Get the modal
    var modal = document.getElementById('id0${divNum}');

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
</script>