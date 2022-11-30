//check email
function isEmailWrong(emailField, emailErrorNote) {
    let pattern = new RegExp(/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
    if (pattern.test(emailField.val())) {
        emailErrorNote.hide(400);
        return false;
    } else {
        // $('#erroremail').html('Invalid email address');
        emailErrorNote.show(300);
        return true;
    }
}

// check password
function isPassWrong(passField, passErrorNote) {
    let password = passField.val();
    let pattern = new RegExp(/^.{4,12}$/);

    if (!pattern.test(password)) {
        passErrorNote.show(300);
        return true;
    } else {
        passErrorNote.hide(400);
        return false;
    }
}

function isEmailCodeWrong(emailField, errorCodeNote) {
    let emailCode = emailField.val();
    let pattern = new RegExp(/^[0-9]+$/);

    if (!pattern.test(emailCode)) {
        errorCodeNote.show(300);
        return true;
    } else {
        errorCodeNote.hide(400);
        return false;
    }
}

//check captcha checked - doesn't work
function captchaNotChecked(captchaErrorNote) {
    // if ($('.recaptcha-checkbox-border').css('display') !== 'none') {
    if ($('#recaptcha-anchor').getAttribute('aria-checked') === 'false') {
        captchaErrorNote.show(300);
        return true;
    } else {
        captchaErrorNote.hide(400);
        return false;
    }
}