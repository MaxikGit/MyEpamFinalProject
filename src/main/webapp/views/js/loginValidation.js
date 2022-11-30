//log in form validation
$(function () {

    let emailErrorNote = $('#erroremail');
    let passErrorNote = $('#errorpass');
    let captchaErrorNote = $('#errorcaptcha');
    let passField = $('#password');
    let emailField = $('#email');

    emailErrorNote.hide();
    passErrorNote.hide();
    captchaErrorNote.hide();

    let error_email = false;
    let error_pass = false;
    // let error_captcha = false;

    emailField.focusout(function () {
        isEmailWrong(emailField, emailErrorNote);
    });
    passField.focusout(function () {
        isPassWrong(passField, passErrorNote);
    });
    $('#rc-anchor-container').focusout(function () {
        captchaNotChecked(captchaErrorNote);
    });

    // form submit
    $('#reg_form').submit(function () {
        error_pass = false;
        error_email = isEmailWrong(emailField, emailErrorNote);
        // error_captcha = captchaNotChecked(captchaErrorNote);
        if (passField.is(':required')) {
            error_pass = isPassWrong(passField, passErrorNote);
        } else error_pass = false;
        return !error_email && !error_pass;
    });

});