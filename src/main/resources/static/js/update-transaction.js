$(document).ready(function () {
    $('#btn-update').on("click", () => {
        let id = $('#id').val()
        let status = $('#status').val()

        let postData = {
            "id" : id,
            "status" : status
        }

        $.ajax({
            type : "POST",
            url : "/transaction/update",
            dataType : "json",
            contentType : "application/json; charset=utf-8",
            data : JSON.stringify(postData),
            success: (data) => {
                if(data.rspCode === "00") {
                    $('.alert').addClass('alert-success')
                    $('.alert > span').text(data.rspMessage)
                }
                else {
                    $('.alert').addClass('alert-error')
                    $('.alert > span').text(data.rspMessage)
                }
            }
        })
    })
})