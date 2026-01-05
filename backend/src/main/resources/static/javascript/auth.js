function login() {
    $.ajax({
        type: "POST",
        url: "/api/auth/login",
        contentType: "application/json",
        data: JSON.stringify({
            username: $("#email").val(),
            password: $("#password").val()
        }),
        success: function (res) {
            localStorage.setItem("token", res.accessToken);
            localStorage.setItem("role", res.role);
            localStorage.setItem("username", res.username);
            location.href = "community.html";
        },
        error: function (jqXHR) {
            if (jqXHR.responseJSON && jqXHR.responseJSON.message) {
                alert(jqXHR.responseJSON.message);
            } else {
                alert("로그인에 실패했습니다.");
            }
        }
    });
}

function signup() {
    $.ajax({
        type: "POST",
        url: "/api/auth/signup",
        contentType: "application/json",
        data: JSON.stringify({
            username: $("#email").val(),
            password: $("#password").val()
        }),
        success: function () {
            alert("회원가입 완료");
            location.href = "login.html";
        },
        error: function (jqXHR) {
            if (jqXHR.responseJSON && jqXHR.responseJSON.message) {
                alert(jqXHR.responseJSON.message);
            } else {
                alert("회원가입에 실패했습니다.");
            }
        }
    });
}