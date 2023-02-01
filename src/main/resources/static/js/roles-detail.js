$(document).ready(() => {
    $('#add_role').on("click", () => {
        window.location.href = "/system/role/create"
    })

    $('.update_role').each((i, e) => {
        $(e).on("click", () => {
            let roleName = $(e).closest('tr').children('.name').text()
            window.location.href = "/system/role/update?roleName=" + roleName
        })
    })
})