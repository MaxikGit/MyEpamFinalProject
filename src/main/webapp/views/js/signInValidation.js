//signup form validation
$(function(){

    let errorNameNote = $('#errorname');
    let errorLNameNote = $('#errorlastname');
    let errorEmailNote = $('#erroremail');
    let errorPassNote = $('#errorpass');
    let errorConPassNote = $('#errorconpass');

    errorNameNote.hide();
    errorLNameNote.hide();
    errorEmailNote.hide();
    errorPassNote.hide();
    errorConPassNote.hide();

    let errorName = false;
    let errorLastName = false;
    let errorEmail = false;
    let errorPass = false;
    let errorConPass = false;

    let nameField = $('#name');
    let lastNameField = $('#last_name');
    let emailField = $('#email');
    let passField = $('#password');
    let conPassField = $('#re-password');

    nameField.focusout(function(){
        isNameWrong(nameField, errorNameNote);
    });
    lastNameField.focusout(function(){
        isLastNameWrong(lastNameField, errorLNameNote);
    });
    emailField.focusout(function(){
        isEmailWrong(emailField, errorEmailNote);
    });
    passField.focusout(function(){
        isPassWrong(passField, errorPassNote);
    });
    conPassField.focusout(function(){
        passNotConfirmed(passField, conPassField, errorConPassNote);
    });

    // form submit
    $('#reg_form').submit(function(){
        errorName = isNameWrong(nameField, errorNameNote);
        errorLastName = isLastNameWrong(lastNameField, errorLNameNote);
        errorEmail = isEmailWrong(emailField, errorEmailNote);
        errorPass = isPassWrong(passField, errorPassNote);
        errorConPass = passNotConfirmed(passField, conPassField, errorConPassNote);

        let b = !errorName && !errorLastName && !errorEmail
            && !errorPass && !errorConPass;
        return b;
    });
});