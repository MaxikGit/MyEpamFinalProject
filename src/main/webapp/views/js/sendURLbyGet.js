function sendURLbyGet(url) {
    let xhttp = initRequest();
    xhttp.onreadystatechange = function () {
    };
    xhttp.open("GET", url, true);
    xhttp.send();
}

function initRequest() {
    if (window.XMLHttpRequest) {
        if (navigator.userAgent.indexOf('MSIE') !== -1) {
            isIE = true;
        }
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
}

