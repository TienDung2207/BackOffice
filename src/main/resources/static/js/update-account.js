$(document).ready(function()
{
    $('#role').on("change", () => {
        $('#position-error').text("")
    })

    $('#confirm_password').on("input", () => {
        $('#conf_pass-error').text("")
    })
//    handle update
    $('#btn-create').on("click", () => {
        if($('#role').val() === "") {
            $('#position-error').text("Please choose the position")
        }
        if($('#password').val() !== $('#confirm_password').val()) {
            $('#conf_pass-error').text("Password didn't match")
        }
        else {
            if($('#role').val() !== "") {
                let username = $('#username').val()
                let fullName = $('#fullName').val()
                let email = $('#email').val()
                let password = $('#password').val()
                let role = $('#role').val()
                let hashPass
                if(password === "") {
                    hashPass = ""
                }
                else {
                    hashPass = CryptoJS.MD5(password).toString()
                }

                let user = {
                    "username" : username,
                    "password" : hashPass,
                    "email" : email,
                    "fullName" : fullName,
                    "role" : role
                }

                $.ajax({
                    type: "POST",
                    url: "/system/accounts/update",
                    dataType : "json",
                    contentType : "application/json; charset=utf-8",
                    data: JSON.stringify(user),
                    success: function (data) {
                        let rspCode = data.rspCode
                        if(rspCode === "00") {
                            let alertE = $('.alert')
                            alertE.addClass('alert-success')
                            $('.alert span').text("Account updated successfully!")
                        }
                    },
                    error: function (data) {
                        if(data.responseJSON.rspCode !== "00") {
                            let alertE = $('.alert')
                            alertE.addClass('alert-error')
                            alertE.text("Update fail")
                        }
                        console.log("fail!")
                    }
                })

                let alertElement = $('.alert')
                if(alertElement.hasClass('alert-success')) {
                    $('input').each((index, element) => {
                        $(element).on("click", () => {
                            $(element).val("")
                            alertElement.removeClass('alert-success')
                        })
                    })
                }
            }
        }
    })
});

