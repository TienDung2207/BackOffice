$(document).ready(function() {
    $('#show_pass').on("click", () => {
        if($('.password').attr("type") === "password")
        {
            $('.password').attr("type","text")
            $('.conf_pass').attr("type","text")
        }
        else {
            $('.password').attr("type","password")
            $('.conf_pass').attr("type","password")
        }

    })

    $('input').each((index, element) => {
        $(element).on("input", () => {
            $(element).next().text("")
        })
    })

    $('#position').on("change", () => {
        $('#position-error').text("")
    })

    $('#submit').click(function(e) {
        e.preventDefault();
        if($('.alert.alert-success')) {
            $('.alert').removeClass('alert-success')
        }

        let username = $('.userName').val().trim()
        let password = $('.password').val().trim()
        let confPass = $('.conf_pass').val().trim()
        let email = $('.email').val().trim()
        let fullName = $ ('.fullName').val().trim()
        let role = $('#position').val()

        let fullNameError = $('#fullName-error')
        let emailError = $('#email-error')
        let usernameError = $('#username-error')
        let passError = $('#pass-error')
        let confirmPassError = $('#conf_pass-error')
        let roleError = $('#position-error')

        if(fullName.length === 0) {
            fullNameError.text("Please Fill the full name!")
        }
        if(email.length === 0) {
            emailError.text("Please Fill the email!")
        }
        if(username.length === 0) {
            usernameError.text("Please Fill the username")
        }
        if(password.length === 0) {
            passError.text("Please Fill the password")
        }
        if(confPass.length === 0) {
            confirmPassError.text("Please Fill the confirm password")
        }
        if(role.length === 0) {
            roleError.text("Please choose the position")
        }

        let reg_email =/^[A-Za-z0-9]+([_\.\-]?[A-Za-z0-9])*@[A-Za-z0-9]+([\.\-]?[A-Za-z0-9]+)*(\.[A-Za-z]+)+$/
        if(!reg_email.test(email)) {
            emailError.text("Email must be a valid email")
        }

        if(password.length !== 0) {
            if(confPass !== password) {
                confirmPassError.text("Password didn't match")
            }
        }

        if(password !== "" && confPass !== "" && password === confPass && role !== "") {
            let user = {
                "username" : username,
                "password" : CryptoJS.MD5(password).toString(),
                "email" : email,
                "fullName" : fullName,
                "role" : role
            }

            $.ajax({
                type: "POST",
                url: "/auth/register",
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(user),
                success: function (data) {
                    if(data.rspCode === "00") {
                        if($('.alert').length !== 0) {
                            $('.alert').addClass("alert-success")
                            $('.alert span').text(data.rspMessage)

                            if($('.alert.alert-success')) {
                                $('input').each((index, element) => {
                                    $(element).on("input", () => {
                                        $('.alert').removeClass('alert-success')
                                    })
                                })

                                $('#position').on("change", () => {
                                    $('.alert').removeClass('alert-success')
                                })
                            }
                        }
                        else {
                            alert("Create Account Success! Click OK to continue")
                            window.location.href = '/login'
                        }
                    }
                },
                error: function (data) {
                    let rspCode = data.responseJSON.rspCode
                    if(rspCode !== "00") {
                        $('#position-error').text(data.responseJSON.rspMessage)
                    }
                    console.log("fail!")
                }
            })
        }
    })
})