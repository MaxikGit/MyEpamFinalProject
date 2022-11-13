//signup form validation
$(function(){

    $('#erroremail').hide();
    $('#errorpass').hide();

    let error_email = false;
    let error_pass = false;

    $('#email').focusout(function(){
        checkEmail();
    });
    $('#password').focusout(function(){
        checkPass();
    });

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

    //form submit
    $('#reg_form').submit(function(){
        error_email = false;
        error_pass = false;
        checkEmail();
        checkPass();

        return error_email === false && error_pass === false;
    });

});