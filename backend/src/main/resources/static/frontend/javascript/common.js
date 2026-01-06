$.ajaxSetup({
    beforeSend: function (xhr) {
        const token = localStorage.getItem("token");
        if (token) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        }
    },
    statusCode: {
        401: function () {
            alert("로그인이 필요합니다.");
            clearAuth();
            location.href = "login.html";
        },
        403: function () {
            alert("접근 권한이 없습니다.");
        }
    }
});

function isLogin() {
    return !!localStorage.getItem("token");
}

function isAdmin() {
    return localStorage.getItem("role") === "ADMIN";
}

function getLoginUserId() {
    return localStorage.getItem("userId");
}

function logout() {
    clearAuth();
    location.href = "home.html";
}

function clearAuth() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userId");
}
