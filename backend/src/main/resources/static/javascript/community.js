$(document).ready(function () {

    // ✅ 글쓰기 버튼
    $("#writeBtn").on("click", function () {
        const token = getToken();

        if (!token) {
            alert("로그인이 필요합니다.");
            location.href = "login.html";
            return;
        }

        location.href = "write.html";
    });

    loadCurrentUser();
    loadPosts("GENERAL");
});

/* ===============================
   로그인 유저 정보
================================ */
let currentUser = null;

async function loadCurrentUser() {
    const token = getToken();
    if (!token) return;

    const res = await fetch("/api/users/me", {
        headers: authHeader()
    });

    if (res.ok) {
        currentUser = await res.json();
    }
}

/* ===============================
   게시글 목록
================================ */
async function loadPosts(boardType) {
    const res = await fetch(`/api/posts?boardType=${boardType}`);
    const posts = await res.json();
    renderPosts(posts);
}

function renderPosts(posts) {
    const list = $("#postList");
    list.empty();

    posts.forEach(post => {
        const li = $("<li>");

        li.append(`<h3>${post.title}</h3>`);
        li.append(`<p>${post.content}</p>`);
        li.append(`<small>작성자: ${post.authorUsername}</small><br>`);

        if (canDelete(post)) {
            const deleteBtn = $("<button>삭제</button>");
            deleteBtn.on("click", () => deletePost(post.id));
            li.append(deleteBtn);
        }

        list.append(li);
    });
}

function canDelete(post) {
    if (!currentUser) return false;
    if (currentUser.role === "ADMIN") return true;
    return post.authorId === currentUser.id;
}

async function deletePost(postId) {
    if (!confirm("정말 삭제하시겠습니까?")) return;

    const res = await fetch(`/api/posts/${postId}`, {
        method: "DELETE",
        headers: authHeader()
    });

    if (res.ok) {
        alert("삭제 완료");
        location.reload();
    } else {
        alert("삭제 권한 없음");
    }
}
