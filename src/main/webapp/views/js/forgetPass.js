$(document).ready(function () {

    let passField = $('#password');
    let newPassField = $('#pass_recovery');
    let emailCodeField = $('#email_code');
    let popUp = $('#popup');
    let overlay = $('#overlay');
    let forgetPassButt = $('#forget_pass');
    let error_pass = false;
    let error_code = false;

    let passErrorNote = $('#errorpass');
    let errorCodeNote = $('#errorcode');

    passErrorNote.hide();
    errorCodeNote.hide();

    //toggle 'required' attr for password
    forgetPassButt.hover(function () {
        passField.attr('required', false);
        passField.addClass('w3-pale-green');
    }, function () {
        passField.attr('required', true);
        passField.removeClass('w3-pale-green');
    });

    //click function on button
    forgetPassButt.click(function () {
        // if (!captchaNotChecked(captchaErrorNote)) {
        overlay.show();
        popUp.removeClass('w3-hide');
        // }
    });

    //check fields on input
    emailCodeField.focusout(function () {
        isEmailCodeWrong(emailCodeField, errorCodeNote);
    });
    newPassField.focusout(function () {
        isPassWrong(newPassField, passErrorNote)
    });

    $('#new_pass_form').submit(function () {
        error_code = isEmailCodeWrong(emailCodeField, errorCodeNote);
        error_pass = isPassWrong(newPassField, passErrorNote);
        if (!error_pass && !error_code === true) {
            hideNewPassLayer();
            return true;
        } else return false;
    });

    $('#close_popup').click(function (){
        hideNewPassLayer();
    });

    function hideNewPassLayer() {
        if (!popUp.hasClass('w3-hide')) {
            popUp.addClass('w3-hide');
        }
        overlay.hide();
    }
});