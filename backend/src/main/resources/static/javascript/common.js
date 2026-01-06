$.ajaxSetup({
    beforeSend: function (xhr) {
        const token = localStorage.getItem("token");
        if (token) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        }
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