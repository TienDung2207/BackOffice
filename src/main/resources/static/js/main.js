$(document).ready(function() {
    $(".navbar-item-user").click(function(e) {
        $(".navbar-user-menu").toggle();
        e.stopPropagation();
    });

    $(document).click(function(e) {
        if (!$(e.target).is('.navbar-user-menu, .navbar-user-menu *')) {
            $(".navbar-user-menu").hide();
        }
    });

    //
    $('.menu-item-wrap').click(function() {
        if($(this).children('div:first-child').children('span').text() !== "Màn hình chính") {
            if($(this).closest('.menu-item').hasClass("active")) {
                $(this).closest('.menu-item').removeClass("active")
            }
            else {
                $(this).closest('.menu-item').addClass("active")
                if($(this).closest('.menu-item').children('.sub-menu-list').children('.sub-menu-item').hasClass("active")) {
                    $(this).closest('.menu-item').children('.sub-menu-list').children('.sub-menu-item').removeClass("active")
                }
            }
        }
    })

    $('.sub-menu-item-wrap').click(function() {
        if($(this).closest('.sub-menu-item').hasClass("active")) {
            $(this).closest('.sub-menu-item').removeClass("active")
        }
        else {
            $(this).closest('.sub-menu-item').addClass("active")
        }
    })

    $('.menu-item-wrap div:first-child span:last-child').each((index, element) => {
        let actionName = $(element).html()

        switch (actionName) {
            case "Màn hình chính":
                $(element).closest('div').children('i').addClass("fas fa-home")
                break;
            case "Báo cáo":
                $(element).closest('div').children('i').addClass("fas fa-chart-line")
                break;
            case "Hệ thống":
                $(element).closest('div').children('i').addClass("fas fa-cogs")
                break;
            case "Quản lí giao dịch":
                $(element).closest('div').children('i').addClass("fas fa-undo")
                break;
            default:
                $(element).closest('div').children('i').removeClass()
        }
    })

    $('.menu-item-wrap').each((index, element) => {
        $(element).on('click', () => {
            if($(element).children('div:first-child').children('span').text() === "Màn hình chính") {
                let pathName = window.location.href.substring(window.location.href.lastIndexOf('/'));
                if(pathName !== "/home") {
                    location.href = "/home"
                }

                return false;
            }
        })
    })

    $('.sub-menu-3 span').each((index, element) => {
        let elementText = $(element).html()
        $(element).closest('a').on("click", () => {
            switch (elementText) {
                case "Thêm mới quyền":
                    location.href = "/system/role/create"
                    break;
                case "Cập nhật quyền":
                    location.href = "/system/role/update"
                    break;
                case "Chi tiết quyền":
                    location.href = "/system/role/detail"
                    break;
                case "Chi tiết tài khoản":
                    location.href = "/system/accounts/detail"
                    break;
                case "Thêm mới tài khoản":
                    location.href = "/system/accounts/create"
                    break;
                case "Cập nhật tài khoản":
                    let myUsername = $('.myUsername').text()
                    let url = "/system/accounts/update?username=" + myUsername
                    location.href = url
                    break;
                default:
            }
        })
    })

    $('.sub-menu-item-wrap div:first-child span:last-child').each((index, element) => {
        let elementText = $(element).html()

        $(element).closest('.sub-menu-item-wrap').on("click", () => {
            switch (elementText) {
                case "Chi tiết giao dịch":
                    location.href = "/transaction"
                    break;
                default:
            }
        })
    })

    let pathName = window.location.pathname
    if(pathName.includes("/system/role")) {
        $('.sub-menu-item-wrap').each((index, element) => {
            if($(element).children('div:first-child').children('span:last-child').text() === "Quản lí quyền") {
                $(element).closest('.sub-menu-item').addClass('active')
                $(element).closest('.menu-item').addClass('active')
                return false;
            }
        })
    }
    if(pathName.includes("/system/accounts")) {
        $('.sub-menu-item-wrap').each((index, element) => {
            if($(element).children('div:first-child').children('span:last-child').text() === "Quản lí tài khoản") {
                $(element).closest('.sub-menu-item').addClass('active')
                $(element).closest('.menu-item').addClass('active')
                return false;
            }
        })
    }
    if(pathName.includes("/transaction")) {
        $('.menu-item-wrap').each((index, element) => {
            if($(element).children('div:first-child').children('span').text() === "Quản lí giao dịch") {
                $(element).closest('.menu-item').addClass('active')

                return false;
            }
        })
    }
});

