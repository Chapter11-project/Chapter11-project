let currentUser = null;
const postId = new URLSearchParams(location.search).get("postId");

$(document).ready(async function () {
    await loadCurrentUser();
    if (!postId) return alert("잘못된 접근입니다.");

    loadQnaPostDetail();
    loadQnaComments();

    $("#commentWriteBtn").on("click", createQnaComment);
});

// 게시글 상세
async function loadQnaPostDetail() {
    const res = await fetch(`/api/posts/${postId}`);
    const post = await res.json();

    $("#postTitle").text(post.title);
    $("#postAuthor").text(post.authorUsername);
    $("#postCreatedAt").text(post.createdAt?.substring(0,10));
    $("#postContent").text(post.content);

    if (currentUser && (currentUser.username === post.authorUsername || currentUser.role === "ADMIN")) {
        $("#editPostBtn").show().click(() => location.href = `qna-edit.html?postId=${post.id}`);
        $("#deletePostBtn").show().click(() => deleteQnaPost(post.id));
    }
}

// 댓글 목록
async function loadQnaComments() {
    const res = await fetch(`/api/posts/${postId}/comments`);
    const comments = await res.json();

    const list = $("#commentList");
    list.empty();

    comments.forEach(c => {
        const canDelete = currentUser && (currentUser.username === c.authorUsername || currentUser.role === "ADMIN");

        list.append(`
            <div class="comment">
                <b>${c.authorUsername}</b> <span style="font-size:12px;color:#777;">(${c.createdAt?.substring(0,10)})</span>
                <div>${c.content}</div>
                ${canDelete ? `<button onclick="deleteQnaComment(${c.id})">삭제</button>` : ""}
            </div>
        `);
    });
}

// 댓글 작성
async function createQnaComment() {
    const content = $("#commentContent").val().trim();
    if (!currentUser) return alert("로그인이 필요합니다.");
    if (!content) return alert("댓글 내용을 입력하세요");

    const res = await fetch(`/api/posts/${postId}/comments`, {
        method: "POST",
        headers: authHeader(),
        body: JSON.stringify({ content })
    });

    if (res.ok) {
        $("#commentContent").val("");
        loadQnaComments();
    } else {
        alert("댓글 작성 실패");
    }
}

// 댓글 삭제
async function deleteQnaComment(commentId) {
    if (!confirm("댓글을 삭제하시겠습니까?")) return;

    const res = await fetch(`/api/posts/${postId}/comments/${commentId}`, {
        method: "DELETE",
        headers: authHeader()
    });

    if (res.ok) loadQnaComments();
    else alert("삭제 권한이 없습니다");
}

// 게시글 삭제
async function deleteQnaPost(postId) {
    if (!confirm("게시글을 삭제하시겠습니까?")) return;

    const res = await fetch(`/api/posts/${postId}`, {
        method: "DELETE",
        headers: authHeader()
    });

    if (res.ok) location.href = "/qna.html";
    else alert("삭제 실패");
}
