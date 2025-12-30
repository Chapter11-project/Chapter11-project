$(document).ready(function () {
    $("#submitBtn").on("click", submitPost);
});

function submitPost() {
    const token = getToken();

    if (!token) {
        alert("로그인이 필요합니다");
        location.href = "login.html";
        return;
    }

    $.ajax({
        url: "http://localhost:8080/api/posts",
        type: "POST",
        contentType: "application/json",
        headers: {
            Authorization: "Bearer " + token
        },
        data: JSON.stringify({
            title: $("#title").val(),
            content: $("#content").val(),
            boardType: "GENERAL"
        }),
        success: function () {
            alert("글 등록 완료");
            location.href = "community.html";
        },
        error: function () {
            alert("글 등록 실패");
        }
    });
}
