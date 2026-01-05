$(function () {
    const params = new URLSearchParams(window.location.search);
    const postId = params.get("id");

    if (!postId) {
        alert("잘못된 접근입니다.");
        location.href = "community.html";
        return;
    }

    function renderPost(post) {
        $("#title").text(post.title);
        $("#writer").text(post.authorUsername);
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

        $.ajax({
            type: "PUT",
            url: `/api/posts/${postId}`,
            contentType: "application/json",
            data: JSON.stringify({
                title: newTitle,
                content: newContent
            }),
            success: loadPost
        });
    }

    function deletePost() {
        if (!confirm("게시글을 삭제하시겠습니까?")) return;

        $.ajax({
            type: "DELETE",
            url: `/api/posts/${postId}`,
            success: function () {
                alert("삭제되었습니다.");
                location.href = "community.html";
            }
        });
    }

    function deleteComment(commentId) {
        $.ajax({
            type: "DELETE",
            url: `/api/comments/${commentId}`,
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