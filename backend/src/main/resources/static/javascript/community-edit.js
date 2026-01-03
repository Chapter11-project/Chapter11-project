const postId = new URLSearchParams(location.search).get("postId");
let currentUser = null;

$(document).ready(async function () {
    await loadCurrentUser();

    if (!currentUser) {
        alert("로그인이 필요합니다");
        location.href = "/login.html";
        return;
    }

    if (!postId) {
        alert("잘못된 접근입니다");
        location.href = "/community.html";
        return;
    }

    // 기존 글 불러오기
    const res = await fetch(`/api/posts/${postId}`);
    if (!res.ok) return alert("글 정보를 불러올 수 없습니다");

    const post = await res.json();

    if (currentUser.username !== post.authorUsername && currentUser.role !== "ADMIN") {
        alert("수정 권한이 없습니다");
        location.href = "/community.html";
        return;
    }

    $("#postTitle").val(post.title);
    $("#postContent").val(post.content);

    // 수정 버튼
    $("#updateBtn").on("click", async function () {
        const title = $("#postTitle").val().trim();
        const content = $("#postContent").val().trim();

        if (!title || !content) return alert("제목과 내용을 입력하세요");

        const res = await fetch(`/api/posts/${postId}`, {
            method: "PUT",
            headers: authHeader(),
            body: JSON.stringify({ title, content })
        });

        if (res.ok) {
            alert("글 수정 완료");
            location.href = `/community-detail.html?postId=${postId}`;
        } else {
            alert("글 수정 실패");
        }
    });
});
