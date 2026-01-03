// 회원가입 버튼 클릭 이벤트
$("#signupBtn").on("click", function () {
    const username = $("#username").val();
    const password = $("#password").val();

    if (!username || !password) {
        alert("아이디와 비밀번호를 모두 입력해주세요.");
        return;
    }

    $.ajax({
        url: "http://localhost:8080/auth/signup",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ username, password }),
        success: function() {
            alert("회원가입 성공");
            location.href = "login.html"; // 회원가입 후 로그인 페이지 이동
        },
        error: function(xhr) {
            alert(xhr.responseText || "회원가입 실패");
        }
    });
});