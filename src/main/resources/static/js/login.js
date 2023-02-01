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


    let arr1 = [0, 1, 2];
    const arr2 = [3, 4, 5];
    console.log({...arr1, ...arr2})
    arr1 = [...arr1, ...arr2];
    console.log(arr1)
    console.log({...arr1, ...arr2})
})