$(document).ready(function() {
    $('.userName').focus()
    $('.userName').on("input", () => {
        $('#name-error').text("")
    })

    let param = new URLSearchParams(window.location.search).get('error')
    if(param === 'fail') {
        $('#name-error').text("Username or Password invalid")
    }
    else if(param === 'locked') {
        $('#name-error').text("An account was locked")
    }

    $('#submit').click(function (e) {
        // e.preventDefault();
        let passValue = $('.password').val()

        $('.password').val(CryptoJS.MD5(passValue).toString())

    })

    console.log("TEST")
    console.log("TEST2")
})