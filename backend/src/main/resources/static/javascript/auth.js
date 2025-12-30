function login() {
    const username = $("#email").val();
    const password = $("#password").val();

    $.ajax({
        url: "http://localhost:8080/auth/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            username,
            password
        }),
        success: function (res) {
            localStorage.setItem("token", res.token);
            alert("로그인 성공");
            location.href = "home.html";
        },
        error: function () {
            alert("로그인 실패");
        }
    });
}