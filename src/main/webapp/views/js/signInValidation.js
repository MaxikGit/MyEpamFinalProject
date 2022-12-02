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
// // check name
// function checkName(){
//     let name = nameField.val();
//     let pattern = new RegExp(/^[\u0453-\u0457\u0403-\u0407\u0410-\u044Fa-zA-Z]{2,30}$/);
//     if (!pattern.test(name)) {
//         errorNameNote.show(300);
//         errorName = true;
//     }
//     else {
//         errorNameNote.hide(400);
//     }
// }
// // check username
// function checkLastName(){
//     let name = lastNameField.val();
//     let pattern = new RegExp(/^[\u0453-\u0457\u0403-\u0407\u0410-\u044Fa-zA-Z]{2,30}$/);
//     if (!pattern.test(name)) {
//         errorLNameNote.show(300);
//         errorLastName = true;
//     }
//     else {
//         errorLNameNote.hide(400);
//     }
// }

//check email
// function checkEmail(){
//     let pattern = new RegExp(/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
//     if (pattern.test(emailField.val())) {
//         errorEmailNote.hide(400);
//     }
//     else {
//         errorEmailNote.show(300);
//         errorEmail = true;
//     }
// }

// check password
// function checkPassSign(){
//     let password = passField.val();
//     // let pattern = new RegExp(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/);
//     let pattern = new RegExp(/^.{4,12}$/);
//
//     if (!pattern.test(password)) {
//         errorPassNote.show(300);
//         errorPass = true;
//     }
//     else {
//         errorPassNote.hide(400);
//     }
// }

// check confirm password
// function checkConPass(){
//     let password = passField.val();
//     let con_pass = conPassField.val();
//     if (password !== con_pass) {
//         errorConPassNote.show(300);
//         errorConPass = true;
//     }
//     else {
//         errorConPassNote.hide(400);
//     }
// }
