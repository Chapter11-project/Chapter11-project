$.ajaxSetup({
    beforeSend: function (xhr) {
        const token = localStorage.getItem("token");
        if (token) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        }
    }
});

$(document).ajaxError(function (event, jqXHR, settings) {
    if (settings && settings.url && settings.url.includes("/api/auth/")) {
        return;
    }
    if (jqXHR.status === 401) {
        alert("로그인이 필요합니다.");
        logout();
        return;
    }
    if (jqXHR.status === 403) {
        alert("권한이 없습니다.");
        return;
    }

    if (jqXHR.responseJSON && jqXHR.responseJSON.message) {
        alert(jqXHR.responseJSON.message);
    } else {
        alert("오류가 발생했습니다.");
    }
});

function isLogin() {
    return !!localStorage.getItem("token");
}

function isAdmin() {
    return localStorage.getItem("role") === "ADMIN";
}

function currentUsername() {
    return localStorage.getItem("username");
}

function logout() {
    localStorage.clear();
    location.href = "home.html";
}
