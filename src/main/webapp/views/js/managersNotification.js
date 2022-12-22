let timeout = 30;
//when document loaded, shoot 1 time
$(document).ready(function () {
    checkNotification();
});

function checkNotification() {
    //creates request object
    let request = initRequest();
    let url = notification_url;
    request.onreadystatechange = function () {

        //if request finished and response is ready
        if (request.readyState === 4) {
            //notification received, need to send one more
            if (request.status === 200) {
                let answer = request.responseText.replace(/[_\W]+/g, "");
                if (answer === '0')
                    $('#manager_notification').addClass('w3-hide');
                else
                    $('#manager_notification').removeClass('w3-hide');

                if ($('#manager_notification_count').text() !== answer)
                    $('#manager_notification_count').text(answer);

                setTimeout(checkNotification, timeout);
            }
            //server said you are not manager
            else if (request.status === 404) {
                $('#manager_notification').addClass('w3-hide');
                location.reload();
            }
        }
    };
    request.open("GET", url, true);
    request.send();
}