//signup form validation
$(function(){

    $('#errorname').hide();
    $('#errorlastname').hide();
    $('#erroremail').hide();
    $('#errorpass').hide();
    $('#errorconpass').hide();

    let error_name = false;
    let error_username = false;
    let error_email = false;
    let error_pass = false;
    let error_conpass = false;

    $('#name').focusout(function(){
        checkName();
    });
    $('#last_name').focusout(function(){
      checkLastName();
    });
    $('#email').focusout(function(){
        checkEmail();
    });
    $('#password').focusout(function(){
        checkPass();
    });
    $('#re-password').focusout(function(){
        checkConPass();
    });

    // check name
    function checkName(){
        let name = $('#name').val();
        // let pattern = new RegExp(/^[а-яА-Яa-zA-Z ]{5,30}$/);
        let pattern = new RegExp(/^[\u0453-\u0457\u0403-\u0407\u0410-\u044Fa-zA-Z]{2,30}$/);
        if (!pattern.test(name)) {
            // $('#errorname').html('Should be between 5-30 contains only space');
            $('#errorname').show(300);
            error_name = true;
        }
        else {
            $('#errorname').hide(400);
        }
    }
    // check username
    function checkLastName(){
        let name = $('#last_name').val();
        let pattern = new RegExp(/^[\u0453-\u0457\u0403-\u0407\u0410-\u044Fa-zA-Z]{2,30}$/);
        if (!pattern.test(name)) {
            // $('#errorlastname').html('Should be between 5-30 contains only space');
            $('#errorlastname').show(300);
            error_name = true;
        }
        else {
            $('#errorlastname').hide(400);
        }
    }

    //check email
    function checkEmail(){
        let pattern = new RegExp(/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
        if (pattern.test($("#email").val())) {
            $('#erroremail').hide(400);
        }
        else {
            // $('#erroremail').html('Invalid email address');
            $('#erroremail').show(300);
            error_email = true;
        }
    }

    // check password
    function checkPass(){
        let password = $('#password').val();
        // let pattern = new RegExp(/^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/);
        let pattern = new RegExp(/^.{4,12}$/);

        if (!pattern.test(password)) {
            // $('#errorpass').html('Should be at least a uppercase,lowercase,number,special characters and minimum length 8');
            $('#errorpass').show(300);
            error_pass = true;
        }
        else {
            $('#errorpass').hide(400);
        }
    }

    // check confirm password
    function checkConPass(){
        let password = $('#password').val();
        let con_pass = $('#re-password').val();
        if (password !== con_pass) {
            // $('#errorconpass').html('Password not match');
            $('#errorconpass').show(300);
            error_conpass = true;
        }
        else {
            $('#errorconpass').hide(400);
        }
    }

    //form submit
    $('#reg_form').submit(function(){
        error_name = false;
        error_username = false;
        error_email = false;
        error_pass = false;
        error_conpass = false;
        checkName();
        checkLastName();
        checkEmail();
        checkPass();
        checkConPass();

        return error_name === false && error_username === false && error_email === false
            && error_pass === false && error_conpass === false;
    });

});