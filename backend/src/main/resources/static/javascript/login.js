$(document).ready(function () {
    $("#loginForm").on("submit", function (e) {
        e.preventDefault();

        const username = $("#username").val().trim();
        const password = $("#password").val().trim();

        if (!username || !password) {
            alert("아이디와 비밀번호를 모두 입력해주세요.");
            return;
        }

        $.ajax({
            url: "/auth/login",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ username, password }),
            success: function (res) {
                localStorage.setItem("accessToken", res.data);
                localStorage.setItem("username", username);

                alert("로그인 성공");
                location.href = "/home.html"; // ⭐ 홈으로만 이동
            },
            error: function () {
                alert("아이디 또는 비밀번호가 올바르지 않습니다.");
            }
        });
    });
});
