// =========================
// 토큰 유틸
// =========================
function getToken() {
    return localStorage.getItem("token");
}

function authHeader() {
    return {
        "Authorization": "Bearer " + getToken(),
        "Content-Type": "application/json"
    };
}

// =========================
// 로그아웃
// =========================
function logout() {
    localStorage.removeItem("token");
    alert("로그아웃 되었습니다.");
    location.href = "home.html";
}

// =========================
// 로그인 상태 UI 처리
// =========================
$(document).ready(function () {
    const token = getToken();

    const loginBtn = $("#loginBtn");
    const signupBtn = $("#signupBtn");
    const logoutLi = $("#logoutLi");

    if (token) {
        // 로그인 상태
        loginBtn.text("로그인 완료");
        loginBtn.prop("disabled", true);
        signupBtn.hide();
        logoutLi.show();
    } else {
        // 비로그인 상태
        loginBtn.text("로그인");
        loginBtn.prop("disabled", false);
        signupBtn.show();
        logoutLi.hide();

        loginBtn.off("click").on("click", function () {
            location.href = "login.html";
        });
    }
});
