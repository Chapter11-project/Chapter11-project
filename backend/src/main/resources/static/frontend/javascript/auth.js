function login() {
    $.ajax({
        type: "POST",
        url: "/api/auth/login",
        contentType: "application/json",
        data: JSON.stringify({
            email: $("#email").val(),
            password: $("#password").val()
        }),
        success: function (res) {
            localStorage.setItem("token", res.accessToken);
            localStorage.setItem("role", res.role);
            localStorage.setItem("userId", res.userId);
            location.href = "community.html";
        }
    });
}

function signup() {
    $.ajax({
        type: "POST",
        url: "/api/auth/signup",
        contentType: "application/json",
        data: JSON.stringify({
            email: $("#email").val(),
            password: $("#password").val()
        }),
        success: function () {
            alert("회원가입 완료");
            location.href = "login.html";
        }
    });
}
