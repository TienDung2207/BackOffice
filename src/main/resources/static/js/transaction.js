$(document).ready(function () {
    let getCookie = (cookieName) => {
        let name = cookieName + "=";
        let decodedCookie = decodeURIComponent(document.cookie);
        let ca = decodedCookie.split(';');
        for(let i = 0; i <ca.length; i++) {
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

    let setCookie = (cookieName, cookieValue, exDays) => {
        let date = new Date();
        date.setTime(date.getTime() + (exDays * 24 * 60 * 60 * 1000));
        let expires = "expires="+ date.toUTCString();
        document.cookie = cookieName + "=" + cookieValue + ";" + expires + ";path=/";
    }

    let deleteCookie = (cookieName) => {
        document.cookie = `${cookieName}=; path=/; expires=${new Date().toUTCString()}`;
    };

    let addPageNumber = (index) => {
        $('.page').append(
            "                       <span>\n" +
            "                            <span class=\"page_number\"> " + index + " </span>\n" +
            "                        </span>")
    }

    let changeActivePageNumber = () => {
        $('.page_number.page_current').removeClass('page_current')
        $('.page span:first-child span').addClass('page_current')
    }

    let changeRowData = (data, STT, element, index) => {
        $(element).html(
            "                            <td> " + STT + "</td>\n" +
            "                            <td style=\"text-align: end\"> " + data.payments[index].amount + " </td>\n" +
            "                            <td> " + data.payments[index].orderInfo + " </td>\n" +
            "                            <td> " + data.payments[index].status + " </td>\n" +
            "                            <td>\n" +
            "                                <i class=\"fas fa-edit update_account\"></i>\n" +
            "                                <i class=\"fas fa-eye\"></i>\n" +
            "                                <i class=\"fas fa-share\"></i>\n" +
            "                            </td>")
    }

    let addRowData = (data, i, STT) => {
        $('table').append(
            "                            <tr><td> " + STT + "</td>\n" +
            "                            <td style=\"text-align: end\"> " + data.payments[i].amount + " </td>\n" +
            "                            <td> " + data.payments[i].orderInfo + " </td>\n" +
            "                            <td> " + data.payments[i].status + " </td>\n" +
            "                            <td>\n" +
            "                                <i class=\"fas fa-edit update_account\"></i>\n" +
            "                                <i class=\"fas fa-eye\"></i>\n" +
            "                                <i class=\"fas fa-share\"></i>\n" +
            "                            </td></tr>>")
    }

    let updatePayment = () => {
        $('.update_payment').each((index, element) => {
            $(element).on("click", () => {
                let id = $(element).closest('tr').find('td:first-child').text()
                let url = "/transaction/update?id=" + id

                window.location.href = url
            })
        })
    }
    updatePayment()

    let showDetailPayment = () => {
        $('.fas.fa-eye').each((index, element) => {
            $(element).on("click", () => {
                let modal = $('.modal')
                let id = $(element).closest('tr').children('td:first-child').text()
                let url = "/transaction/payment?id=" + id
                $.ajax({
                    type: "GET",
                    url: url,
                    success: function(data) {
                        modal.find('.payment-id').text(data.payId)
                        modal.find('.payment-amount').text(data.amount)
                        modal.find('.payment-info').text(data.orderInfo)
                        modal.find('.payment-time').text(data.payDate)
                        modal.find('.payment-ip').text(data.ipAddress)
                        modal.find('.payment-status').text(data.status)
                    }
                })
                modal.addClass('open')
            })
        })
    }
    showDetailPayment()

    let changAllRowsTable = () => {
        $('.page_number').each((index, element) => {
            $(element).on("click", () => {
                let amount = getCookie("amount")
                let ipAddress = getCookie("ipAddress")
                let info = getCookie("info")
                let status = getCookie("status")

                let postUrl = "/transaction/payment/detail/" + $(element).html()
                let postData

                postData = {
                    "amount" : amount,
                    "ipAddress" : ipAddress,
                    "info" : info,
                    "status" : status
                }

                $.ajax({
                    type: "POST",
                    url: postUrl,
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(postData),
                    success: function (data) {
                        let totalRowsShowing = $('table tr:not(:first-child)').length

                        $('table tr:not(:first-child)').each((index, element) => {
                            if(data.currentPage < data.totalPages) {
                                if(totalRowsShowing === data.payments.length) {
                                    changeRowData(data, totalRowsShowing * (data.currentPage - 1) + (index + 1), element, index)
                                }
                                else if(totalRowsShowing < data.payments.length) {
                                    changeRowData(data, data.payments.length * (data.currentPage - 1) + (index + 1), element, index)
                                    if(index + 1 === totalRowsShowing) {
                                        for(let i = totalRowsShowing; i < data.payments.length; i++) {
                                            addRowData(data, i, data.payments.length * (data.currentPage - 1) + (i + 1))
                                        }
                                    }
                                }
                            }
                            else if(data.currentPage === data.totalPages) {
                                if(totalRowsShowing === data.payments.length) {
                                    changeRowData(data, totalRowsShowing * (data.currentPage - 1) + (index + 1), element, index)
                                }
                                else if(totalRowsShowing > data.payments.length) {
                                    if(index < data.payments.length) {
                                        changeRowData(data, totalRowsShowing * (data.currentPage - 1) + (index + 1), element, index)
                                    }
                                    else {
                                        $(element).remove()
                                    }
                                }
                                else {
                                    if(totalRowsShowing === 0) {
                                        for(let i = totalRowsShowing; i < data.payments.length; i++) {
                                            addRowData(data, i, data.payments.length * (data.currentPage - 1) + (i + 1))
                                        }
                                    }
                                    else {
                                        changeRowData(data, data.payments.length * (data.currentPage - 1) + (index + 1), element, index)
                                        if(index + 1 === totalRowsShowing) {
                                            for(let i = totalRowsShowing; i < data.payments.length; i++) {
                                                addRowData(data, i, data.payments.length * (data.currentPage - 1) + (i + 1))
                                            }
                                        }
                                    }
                                }
                            }
                        })
                        //day
                        updatePayment()
                        showDetailPayment()
                    },
                    error: function (data) {
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
    changAllRowsTable()

//    Search user(account)
    $('.btn-search').on("click", () => {
        let amount = $('.amount').val()
        let ipAddress = $('.ipAddress').val()
        let info = $('.info').val()
        let status = $('.status').val()

        let postData = {
            "amount" : amount,
            "ipAddress" : ipAddress,
            "info" : info,
            "status" : status
        }

        setCookie("amount", amount, 1)
        setCookie("ipAddress", ipAddress, 1)
        setCookie("info", info, 1)
        setCookie("status", status, 1)

        $.ajax({
            type: "POST",
            url: "/transaction/payment/detail/1",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(postData),
            success: (data) => {
                let totalRowsShowing = $('table tr:not(:first-child)').length

                if(data.payments.length === 0) {
                    $('table tr:not(:first-child)').each((index, element) => {
                        $(element).remove()
                    })
                }
                else {
                    if(totalRowsShowing === data.payments.length) {
                        $('table tr:not(:first-child)').each((index, element) => {
                            changeRowData(data, index + 1, element, index)
                        })
                    }
                    else if(totalRowsShowing > data.payments.length) {
                        $('table tr:not(:first-child)').each((index, element) => {
                            if (index < data.payments.length) {
                                changeRowData(data, index + 1, element, index)
                            } else {
                                $(element).remove()
                            }
                        })
                    }
                    else {
                        if(totalRowsShowing === 0) {
                            for(let i = totalRowsShowing; i < data.payments.length; i++) {
                                addRowData(data, i, i + 1)
                            }
                        }
                        else {
                            $('table tr:not(:first-child)').each((index, element) => {
                                    changeRowData(data, index + 1, element, index)

                                    if(index + 1 === totalRowsShowing) {
                                        for(let i = totalRowsShowing; i < data.payments.length; i++) {
                                            addRowData(data, i, i + 1)
                                        }
                                    }
                            })
                        }
                    }
                }

                let pageLength = $('.page').children().length
                if(data.totalPages < pageLength) {
                    for(let i = pageLength; i > 1; i--) {
                        if(data.totalPages < i) {
                            $('.page').children().last().remove()
                            pageLength--
                        }
                    }
                }
                else if(data.totalPages > pageLength){
                    for(let i = pageLength; i <= data.totalPages; i++) {
                        if(i > pageLength) {
                            addPageNumber(i)
                        }
                    }
                }

                // changeActivePageNumber()
                // changAllRowsTable()
            },
            error: (data) => {
            }
        })
        // changeActivePageNumber()
        // changAllRowsTable()
    })



    let modal = $('.modal')
    let close = $('.close-ticket-btn')
    let modalContainer = $('.modal__container')
    function closeModal() {
        $('.modal').removeClass('open')
    }
    modal.on('click', closeModal)
    close.on('click', closeModal)
    modalContainer.on('click', (event) => {
        event.stopPropagation()
    })

    window.onbeforeunload = () => {
        deleteCookie("amount")
        deleteCookie("ipAddress")
        deleteCookie("info")
        deleteCookie("status")
    }
});

