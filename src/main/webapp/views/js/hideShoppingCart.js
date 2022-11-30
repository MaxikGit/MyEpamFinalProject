// let jQueryScript = document.createElement('script');
// jQueryScript.setAttribute('src', 'http://code.jquery.com/jquery-1.11.3.min.js');
// document.head.appendChild(jQueryScript);

$(document).ready(function () {
    let cartNum = $("#cartSize").text();
    if (cartNum < 1) {
        $("#shoppingCart").hide();
    }
});