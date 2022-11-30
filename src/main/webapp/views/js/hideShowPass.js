//you need to add class = "toggle-password class" to the element, to make it works
$(document).ready(function () {
    $('.toggle-password').click(function () {
        if ($(this).text() === 'visibility_off')
            $(this).text('visibility');
        else $(this).text('visibility_off');
        //tag with 'eye' element should be inside another tag, input tag had to be next then
        let input = $(this).parent().next();
        input.attr('type', input.attr('type') === 'password' ? 'text' : 'password');
    })
});