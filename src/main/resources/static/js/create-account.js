$(document).ready(function() {
    let passwordEyeEle = $('.fas.fa-eye')
    let passwordEyeSlashEle = $('.fas.fa-eye-slash')

    $('.form-group span i').each((index, element) => {
        $(element).on("click", () => {
            if(passwordEyeSlashEle.hasClass('show')) {
                $(passwordEyeSlashEle).removeClass('show')
                $(passwordEyeEle).addClass('show')
                $('#password').attr("type", "password")
                $('#confirm_password').attr("type", "password")
            }
            else {
                $(passwordEyeEle).removeClass('show')
                $(passwordEyeSlashEle).addClass('show')
                $('#password').attr("type", "text")
                $('#confirm_password').attr("type", "text")
            }
        })
    })
});

