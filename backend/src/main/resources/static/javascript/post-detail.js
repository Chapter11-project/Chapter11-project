const urlParams = new URLSearchParams(window.location.search);
const postId = urlParams.get("postId");

$(document).ready(function () {
    $("#commentBtn").on("click", function () {
        createComment(postId);
    });
});

function createComment(postId) {
    const content = $("#commentContent").val().trim();
    const token = localStorage.getItem("accessToken");

    if (!token) {
        alert("로그인이 필요합니다.");
        return;
    }

    if (!content) {
        alert("댓글 내용을 입력하세요.");
        return;
    }

    $.ajax({
        url: `/api/posts/${postId}/comments`,
        type: "POST",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + token
        },
        data: JSON.stringify({ content }),
        success: function () {
            alert("댓글 작성 완료");
            location.reload();
        },
        error: function (err) {
            console.error(err);
            alert("댓글 작성 실패");
        }
    });
}
