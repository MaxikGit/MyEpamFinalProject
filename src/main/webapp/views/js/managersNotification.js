
$(document).ready(function (){
setTimeout(checkNotification, 5_000);
});

function checkNotification() {
    let request = initRequest();
    let url = 'ServletController?action=notify';
    request.onreadystatechange = function () {
        if (request.readyState === 4) {
            if (request.status === 200) {
                $('#manager_notification').removeClass('w3-hide');
                setTimeout(checkNotification, 5_000);
            }else if (request.status === 404){
                $('#manager_notification').addClass('w3-hide');
                location.reload();
            }else {
                $('#manager_notification').addClass('w3-hide');
                setTimeout(checkNotification, 5_000);
            }
        }
    };
    request.open("GET", url, true);
    request.send();
}