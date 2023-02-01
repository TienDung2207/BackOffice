$(document).ready(function () {
    let nameOfAccountUpdating
    let statusOfAccountUpdating

    // Get cookie
    let getCookie = (cookieName) => {
        let name = cookieName + "=";
        let decodedCookie = decodeURIComponent(document.cookie);
        let ca = decodedCookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) === 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }

    // Generate cookie
    let setCookie = (cookieName, cookieValue, exDays) => {
        let date = new Date();
        date.setTime(date.getTime() + (exDays * 24 * 60 * 60 * 1000));
        let expires = "expires=" + date.toUTCString();
        document.cookie = cookieName + "=" + cookieValue + ";" + expires + ";path=/";
    }

    // Delete cookie
    let deleteCookie = (cookieName) => {
        document.cookie = `${cookieName}=; path=/; expires=${new Date().toUTCString()}`;
    };

    //
    let changePagination = (data) => {
        $('.pagination .page_number').each((index, element) => {
            $(element).remove()
        })
        for (let i = 1; i <= data.totalPages; i++) {
            $('.pagination').append("<span class=\"page_number\"> " + i + " </span>")
        }
    }

    // Change active page number
    let changeActivePageNumber = () => {
        $('.pagination span:first-child').addClass('page_current')
    }

    //
    let addRowToTable = (data, i, STT) => {
        let text;
        let classValue;
        let lockIcon = ""
        if (data.users[i].status === 0) {
            text = "Active"
            classValue = "status user_active"
            lockIcon = "<i class=\"fa fa-ban lock\" aria-hidden=\"true\"></i>\n"
        } else {
            text = "Lock"
            classValue = "status user_lock"
        }
        $('table tbody').append(
            "                            <tr><td> " + STT + "</td>\n" +
            "                            <td class=\"name\"> " + data.users[i].username + " </td>\n" +
            "                            <td> " + data.users[i].fullName + " </td>\n" +
            "                            <td> " + data.users[i].email + " </td>\n" +
            "                            <td> " + data.users[i].roleName + " </td>\n" +
            "                            <td class='" + classValue + "'> " + text + "</td>\n" +
            "                            <td>\n" +
            "                                <i class=\"fas fa-edit update_account\"></i>\n" +
                                            lockIcon +
            "                                <i class=\"fas fa-share reset\"></i>\n" +
            "                            </td></tr>>")
    }

    // Update account
    let updateAccount = () => {
        $('.update_account').each((index, element) => {
            $(element).on("click", () => {
                $('.modal').addClass('open')
                nameOfAccountUpdating = $(element).closest('tr').children('.name').text().trim()
                let url = "/system/accounts/info-update?username=" + nameOfAccountUpdating
                $.get(url)
                    .done((response) => {
                        $('#modal_username').val(response.username)
                        $('#modal_email').val(response.email)
                        $('#modal_fullName').val(response.fullName)
                        $('#modal_role option[value="' + response.roleName + '"]').attr("selected", true)
                        $('#modal_password').val("")
                        $('#modal_confirm_password').val("")
                        if (response.status === 0) {
                            $('.modal__menu .menu-title:last-child').remove()
                            $('.menu-title:first-child').css("width", "100%")
                        }
                        statusOfAccountUpdating = response.status
                    })
            })
        })
    }

    // Reset pass
    let resetPass = () => {
        $('.reset').each((index, element) => {
            $(element).on("click", () => {
                let username = $(element).closest('tr').children('.name').text().trim()
                let url = "/system/accounts/reset-pass?username=" + username
                $.get(url)
                    .done((response) => {
                        (response)
                        if (response.rspCode === '00') {
                            alert(response.rspMessage)
                        } else {
                            $('.msg').text(response.rspMessage)
                        }
                    })
            })
        })
    }

    // Lock account
    let lockAccount = () => {
        $('.lock').each((index, element) => {
            $(element).on("click", () => {
                let username = $(element).closest('tr').children('.name').text().trim()
                let url = "/system/accounts/lock-account?username=" + username
                $.get(url)
                    .done((response) => {
                        if (response.rspCode === '00') {
                            alert(response.rspMessage)
                            $(element).closest('tr').children('.status').text("Lock")
                            $(element).closest('tr').children('.status').removeClass('user_active')
                            $(element).closest('tr').children('.status').addClass('user_lock')
                            $(element).remove()
                        } else {
                            $('.msg').text(response.rspMessage)
                        }
                    })
            })
        })
    }

//    Change page
    let changeTableContent = () => {
        $('.page_number').each((index, element) => {
            $(element).on("click", () => {
                let dataTableLength = $('.table_length').val()

                let postUrl = "/system/accounts/detail/" + $(element).html()

                let postData = {
                    "username": getCookie("username"),
                    "email": getCookie("email"),
                    "roleName": getCookie("roleName"),
                    "status": getCookie("status"),
                    "tableLength": dataTableLength
                }

                $.ajax({
                    type: "POST",
                    url: postUrl,
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(postData),
                    success: function (data) {
                        $('table tr:not(:first-child)').each((index, element) => {
                            $(element).remove()
                        })
                        let STT
                        if (data.users.length > 0) {
                            for (let i = 0; i < data.users.length; i++) {
                                STT = dataTableLength * (data.currentPage - 1) + (i + 1)
                                addRowToTable(data, i, STT)
                            }
                        }

                        updateAccount()
                        lockAccount()
                        resetPass()
                    },
                    error: function () {
                        console.log("fail")
                    }
                })

                if (!$(element).hasClass('page_current')) {
                    $('.page_current').removeClass('page_current')
                    $(element).addClass('page_current')
                }
            })
        })
    }

    //
    let changeMenuTitle = () => {
        $('.menu-title').each((i, e) => {
            $(e).on("click", () => {
                $('.menu-title.active').removeClass('active')
                $(e).addClass('active')

                $('.menu-content.active').removeClass('active')
                $('.menu-content').eq(i).addClass('active')
            })

        })
    }
    // CLOSE MODAL
    let closeModal = () => {
        if (statusOfAccountUpdating === 0) {
            $('.modal__menu').append("<div class=\"menu-title\">Cập nhật</div>")
            $('.menu-title:first-child').css("width", "50%")
        }
        $('.menu-title.active').removeClass('active')
        $('.modal__menu .menu-title:first-child').addClass('active')
        $('.menu-content.active').removeClass('active')
        $('.modal__content .menu-content:first-child').addClass('active')
        $('.modal').removeClass('open')
        $('#modal_role option[selected="selected"]').removeAttr("selected")

        changeMenuTitle()
    }
    $('.modal').on('click', closeModal)
    $('.btn-close').on('click', closeModal)
    $('.modal__container').on('click', (event) => {
        event.stopPropagation()
    })
    // END CLOSE MODAL


    $('#modal_btn-update').on("click", () => {
        let modalRole = $('#modal_role')
        let modalPassword = $('#modal_password')

        if (modalRole.val() === "") {
            $('#modal_position-error').text("Please choose the position")
        }
        if (modalPassword.val() !== $('#modal_confirm_password').val()) {
            $('#modal_conf_pass-error').text("Password didn't match")
        } else {
            if (modalRole.val() !== "" && modalPassword.val() !== "") {
                let username = $('#modal_username').val()
                let fullName = $('#modal_fullName').val()
                let email = $('#modal_email').val()
                let password = modalPassword.val()
                let role = modalRole.val()
                let hashPass
                if (password === "") {
                    hashPass = ""
                } else {
                    hashPass = CryptoJS.MD5(password).toString()
                }

                let user = {
                    "username": username,
                    "password": hashPass,
                    "email": email,
                    "fullName": fullName,
                    "role": role
                }

                $.ajax({
                    type: "POST",
                    url: "/system/accounts/update",
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(user),
                    success: function (data) {
                        let rspCode = data.rspCode
                        if (rspCode === "00") {
                            let alertE = $('.alert')
                            alertE.addClass('alert-success')
                            $('.alert span').text("Account updated successfully!")
                        }
                    },
                    error: function (data) {
                        if (data.responseJSON.rspCode !== "00") {
                            let alertE = $('.alert')
                            alertE.addClass('alert-error')
                            alertE.text("Update fail")
                        }
                        console.log("fail!")
                    }
                })

                let alertElement = $('.alert')
                if (alertElement.hasClass('alert-success')) {
                    $('.menu-content:first-child input').each((index, element) => {
                        $(element).on("input", () => {
                            alertElement.removeClass('alert-success')
                        })
                    })
                }
            }
        }
    })

    // Remove update notification - MODAL
    let modalAlert = $('.alert')
    $('#modal_role').on("change", () => {
        $('#modal_position-error').text("")
        if (modalAlert.hasClass('alert-error')) {
            modalAlert.removeClass('alert-error')
        }
        if (modalAlert.hasClass('alert-success')) {
            modalAlert.removeClass('alert-success')
        }
    })
    $('#modal_confirm_password').on("input", () => {
        $('#modal_conf_pass-error').text("")
    })
    $('.menu-content:first-child input').each((index, element) => {
        $(element).on("input", () => {
            if (modalAlert.hasClass('alert-error')) {
                modalAlert.removeClass('alert-error')
            }
            if (modalAlert.hasClass('alert-success')) {
                modalAlert.removeClass('alert-success')
            }
        })
    })


//    Search user(account)
    $('.btn-search').on("click", () => {
        let username = $('#username').val()
        let email = $('#email').val()
        let roleName = $('#role').val()
        let status = $('#status').val()
        let tableLength = $('.table_length [selected="selected"]').text()
        $('.table_length').val(tableLength)

        let postData = {
            "username": username,
            "email": email,
            "roleName": roleName,
            "status": status,
            "tableLength": tableLength
        }

        setCookie("username", username, 1)
        setCookie("email", email, 1)
        setCookie("roleName", roleName, 1)
        setCookie("status", status, 1)

        $.ajax({
            type: "POST",
            url: "/system/accounts/detail/1",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(postData),
            success: (data) => {
                $('table tr:not(:first-child)').each((index, element) => {
                    $(element).remove()
                })
                if (data.users.length > 0) {
                    for (let i = 0; i < data.users.length; i++) {
                        addRowToTable(data, i, i + 1)
                    }
                }

                changePagination(data)
                changeActivePageNumber()
                updateAccount()
                lockAccount()
                resetPass()
                changeTableContent()
            },
            error: () => {
                console.log("fail")
            }
        })
    })

    $('.table_length').on("change", () => {
        let username = getCookie("username")
        let email = getCookie("email")
        let roleName = getCookie("roleName")
        let status = getCookie("status")
        let tableLength = $('.table_length').val()

        let postData = {
            "username": username,
            "email": email,
            "roleName": roleName,
            "status": status,
            "tableLength": tableLength
        }

        $.ajax({
            type: "POST",
            url: "/system/accounts/detail/1",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(postData),
            success: (data) => {
                $('table tr:not(:first-child)').each((index, element) => {
                    $(element).remove()
                })
                if (data.users.length > 0) {
                    for (let i = 0; i < data.users.length; i++) {
                        addRowToTable(data, i, i + 1)
                    }
                }

                changePagination(data)
                changeActivePageNumber()
                updateAccount()
                lockAccount()
                resetPass()
                changeTableContent()
            },
            error: () => {
                console.log("fail")
            }
        })

        updateAccount()
    })

    $('.unlock').on("click", () => {
        let url = "/system/accounts/unlock-account?username=" + nameOfAccountUpdating
        $.get(url)
            .done((response) => {
                if (response.rspCode === "00") {
                    $('.name').each((index, element) => {
                        if ($(element).text().trim() === nameOfAccountUpdating) {
                            $(element).closest('tr').children('.status').text("Active")
                            $(element).closest('tr').children('.status').removeClass('user_lock')
                            $(element).closest('tr').children('.status').addClass('user_active')
                            $(element).closest('tr').children('td:last-child').children('.update_account').after("<i class=\"fa fa-ban lock\"  aria-hidden=\"true\"></i>")
                            lockAccount()
                            alert("Unlock account: " + nameOfAccountUpdating + " successfully!")
                        }
                    })
                }
            })
    })

    $('#add_account').on("click", () => {
        window.location.href = "/system/accounts/create"
    })

    lockAccount()
    resetPass()
    updateAccount()
    changeMenuTitle()
    changeTableContent()

    window.onbeforeunload = () => {
        deleteCookie("username")
        deleteCookie("email")
        deleteCookie("roleName")
        deleteCookie("status")
    }
});

