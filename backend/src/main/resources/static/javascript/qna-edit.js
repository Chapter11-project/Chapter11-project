const postId = new URLSearchParams(location.search).get("postId");

$(document).ready(async function () {
    await loadCurrentUser();
    loadPost();

    $("#saveBtn").on("click", updatePost);
});

function loadPost() {
    $.get(`/api/posts/${postId}`, function (res) {
        if (
            !currentUser ||
            (currentUser.username !== res.authorUsername &&
                currentUser.role !== "ADMIN")
        ) {
            alert("수정 권한이 없습니다.");
            location.href = `/qna-detail.html?postId=${postId}`;
            return;
        }

        $("#title").val(res.title);
        $("#content").val(res.content);
    });
}

function updatePost() {
    const title = $("#title").val().trim();
    const content = $("#content").val().trim();

    if (!title || !content) {
        alert("제목과 내용을 입력하세요.");
        return;
    }

    $.ajax({
        url: `/api/posts/${postId}`,
        type: "PUT",
        headers: authHeader(),
        data: JSON.stringify({ title, content }),
        success: () => {
            alert("수정 완료");
            location.href = `/qna-detail.html?postId=${postId}`;
        },
        error: () => alert("수정 실패")
    });
}
