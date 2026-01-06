$(function () {
    const params = new URLSearchParams(window.location.search);
    const postId = params.get("id");
    let currentPost = null;

    function formatDate(dateString) {
        if (!dateString) return "-";
        const d = new Date(dateString);
        return `${d.getFullYear()}.${String(d.getMonth() + 1).padStart(2, "0")}.${String(d.getDate()).padStart(2, "0")}`;
    }

    function boardLabel(type) {
        if (type === "GENERAL") return "커뮤니티";
        if (type === "QNA") return "Q&A";
        return type || "-";
    }

    if (!postId) {
        alert("잘못된 접근입니다.");
        location.href = "community.html";
        return;
    }

    function renderPost(post) {
        currentPost = post;
        $("#title").text(post.title);
        $("#writer").text(post.authorUsername);
        $("#createdAt").text(formatDate(post.createdAt));
        $("#postType").text(boardLabel(post.boardType));
        $("#content").text(post.content);

        const owner = currentUsername() && currentUsername() === post.authorUsername;
        const admin = isAdmin();
        let actions = "";

        if (owner) {
            actions += `<button id="editPostBtn">수정</button>`;
        }
        if (owner || admin) {
            actions += `<button id="deletePostBtn">삭제</button>`;
        }

        $("#postActions").html(actions);

        if (owner) {
            $("#editPostBtn").click(editPost);
        }
        if (owner || admin) {
            $("#deletePostBtn").click(deletePost);
        }
    }

    function loadPost() {
        $.get(`/api/posts/${postId}`, renderPost)
            .fail(xhr => alert(xhr.responseJSON?.message || "게시글을 불러오지 못했습니다."));
    }

    function loadComments() {
        $.get(`/api/comments/post/${postId}`, list => {
            $("#commentCount").text(`총 ${list.length}개`);
            $("#commentList").empty();

            list.forEach(c => {
                const canDelete = isAdmin() || currentUsername() === c.authorUsername;
                const deleteButton = canDelete
                    ? `<button class="delete-comment" data-id="${c.id}">삭제</button>`
                    : "";

                $("#commentList").append(`
          <div class="comment" data-id="${c.id}">
            <p>${c.content}</p>
            <small>${c.authorUsername}</small>
            ${deleteButton}
          </div>
        `);
            });

            $(".delete-comment").click(function () {
                const commentId = $(this).data("id");
                deleteComment(commentId);
            });
        });
    }

    function editPost() {
        if (!currentPost) return;

        const newTitle = prompt("제목을 수정하세요", currentPost.title);
        if (newTitle === null) return;

        const newContent = prompt("내용을 수정하세요", currentPost.content);
        if (newContent === null) return;

        $.ajax({
            type: "PUT",
            url: `/api/posts/${postId}`,
            contentType: "application/json",
            data: JSON.stringify({
                title: newTitle,
                content: newContent,
                boardType: currentPost.boardType
            }),
            success: loadPost,
            error: (xhr) => {
                const status = xhr.status;
                if (status === 401) {
                    alert("로그인이 필요합니다.");
                } else if (status === 403) {
                    alert("수정 권한이 없습니다.");
                } else {
                    alert(xhr.responseJSON?.message || "게시글 수정에 실패했습니다.");
                }
            }
        });
    }

    function deletePost() {
        if (!confirm("게시글을 삭제하시겠습니까?")) return;

        $.ajax({
            type: "DELETE",
            url: `/api/posts/${postId}`,
            success: () => {
                alert("게시글이 삭제되었습니다.");
                location.href = "community.html";
            },
            error: (xhr) => {
                const status = xhr.status;
                if (status === 401) {
                    alert("로그인이 필요합니다.");
                } else if (status === 403) {
                    alert("삭제 권한이 없습니다.");
                } else {
                    alert(xhr.responseJSON?.message || "게시글 삭제에 실패했습니다.");
                }
            }
        });
    }

    function deleteComment(commentId) {
        $.ajax({
            type: "DELETE",
            url: `/api/comments/${commentId}`,
            success: loadComments,
            error: (xhr) => {
                const status = xhr.status;
                if (status === 401) {
                    alert("로그인이 필요합니다.");
                } else if (status === 403) {
                    alert("삭제 권한이 없습니다.");
                } else {
                    alert(xhr.responseJSON?.message || "댓글 삭제에 실패했습니다.");
                }
            }
        });
    }

    window.writeComment = function () {
        const content = $("#commentContent").val();
        if (!content || content.trim().length === 0) {
            alert("댓글 내용을 입력하세요.");
            return;
        }

        if (!isLogin()) {
            alert("로그인이 필요합니다.");
            return;
        }

        $.ajax({
            type: "POST",
            url: `/api/comments/post/${postId}`,
            contentType: "application/json",
            data: JSON.stringify({ content }),
            success: function () {
                $("#commentContent").val("");
                loadComments();
            },
            error: (xhr) => {
                const status = xhr.status;
                if (status === 401) alert("로그인이 필요합니다.");
                else alert(xhr.responseJSON?.message || "댓글 작성에 실패했습니다.");
            }
        });
    };

    // 초기 로딩
    loadPost();
    loadComments();
});
