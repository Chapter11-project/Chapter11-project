const postId = new URLSearchParams(location.search).get("id");

/* 게시글 상세 */
$.get(`/api/community/${postId}`, post => {
    $("#title").text(post.title);
    $("#writer").text(post.writer);
    $("#content").text(post.content);

    if (isLogin() && (isAdmin() || String(post.userId) === getLoginUserId())) {
        $("#postActions").append(`
            <button onclick="editPost()">수정</button>
            <button onclick="deletePost()">삭제</button>
        `);
    }
});

function deletePost() {
    if (!confirm("게시글 삭제?")) return;

    $.ajax({
        type: "DELETE",
        url: `/api/community/${postId}`,
        success: () => location.href = "community.html",
        error: function (err) {
            if (err.status === 403) {
                alert("게시글을 삭제할 권한이 없습니다.");
            }
        }
    });
}

function editPost() {
    location.href = `community_edit.html?id=${postId}`;
}

/* 댓글 */
function loadComments() {
    $("#commentList").empty();

    $.get(`/api/community/${postId}/comments`, list => {
        list.forEach(c => {
            let btn = "";

            if (isLogin() && (isAdmin() || String(c.userId) === getLoginUserId())) {
                btn = `<button onclick="deleteComment(${c.id})">삭제</button>`;
            }

            $("#commentList").append(`
                <div>
                    <b>${c.writer}</b> : ${c.content}
                    ${btn}
                </div>
            `);
        });
    });
}

function writeComment() {
    if (!isLogin()) {
        alert("로그인 필요");
        return;
    }

    $.ajax({
        type: "POST",
        url: `/api/community/${postId}/comments`,
        contentType: "application/json",
        data: JSON.stringify({
            content: $("#commentContent").val()
        }),
        success: () => {
            $("#commentContent").val("");
            loadComments();
        }
    });
}

function deleteComment(id) {
    if (!confirm("댓글 삭제?")) return;

    $.ajax({
        type: "DELETE",
        url: `/api/comments/${id}`,
        success: loadComments,
        error: function (err) {
            if (err.status === 403) {
                alert("댓글을 삭제할 권한이 없습니다.");
            }
        }
    });
}

loadComments();
