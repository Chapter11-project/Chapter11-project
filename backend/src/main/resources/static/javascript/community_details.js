$(function () {
    const params = new URLSearchParams(window.location.search);
    const postId = params.get("id");

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

        $("#editPostBtn").click(() => editPost(post));
        $("#deletePostBtn").click(deletePost);
    }

    function loadPost() {
        $.get(`/api/posts/${postId}`, renderPost);
    }

    function loadComments() {
        $.get(`/api/comments/post/${postId}`, list => {
            $("#commentCount").text(`총 ${list.length}개`);
            $("#commentList").empty();
            list.forEach(c => {
                const canDelete = isAdmin() || currentUsername() === c.authorUsername;
                const deleteButton = canDelete ? `<button class="delete-comment" data-id="${c.id}">삭제</button>` : "";
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

    function editPost(post) {
        const newTitle = prompt("제목을 수정하세요", post.title);
        if (newTitle === null) return;
        const newContent = prompt("내용을 수정하세요", post.content);
        if (newContent === null) return;
    $(function () {
        success: loadComments
    });
    }

    window.writeComment = function () {
        const content = $("#commentContent").val();
        if (!content || content.trim().length === 0) {
            alert("댓글 내용을 입력하세요.");
            return;
        }

        $.ajax({
            type: "POST",
            url: `/api/comments/post/${postId}`,
            contentType: "application/json",
            data: JSON.stringify({content}),
            success: function () {
                $("#commentContent").val("");
                loadComments();
            }
        });
    };

    loadPost();
    loadComments();
});