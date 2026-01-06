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

function renderPosts(posts, showAuthor) {
    const $tbody = $("#myPostList");
    const $empty = $("#myPostEmpty");
    const $authorHeader = $("#authorHeader");

    $tbody.empty();
    $authorHeader.toggleClass("hidden", !showAuthor);

    if (!posts || posts.length === 0) {
        $empty.removeClass("hidden");
        return;
    }
    $empty.addClass("hidden");

    posts.forEach(post => {
        const createdAt = formatDate(post.createdAt);
        const link = post.link || (post.boardType === "QNA"
            ? `qna.html?questionId=${post.id}`
            : `community_details.html?id=${post.id}`);
        const authorCell = showAuthor ? `<td>${post.authorUsername}</td>` : "";
        $tbody.append(`
            <tr class="board-row">
                <td><span class="badge secondary">${boardLabel(post.boardType)}</span></td>
                <td class="board-title"><a href="${link}">${post.title}</a></td>
                ${authorCell}
                <td>${createdAt}</td>
            </tr>
        `);
    });
}

function loadMyPosts() {
    if (!isLogin()) {
        alert("로그인이 필요합니다.");
        location.href = "login.html";
        return;
    }
    const admin = isAdmin();
    const endpoint = admin ? "/api/admin/mypage/posts?size=200" : "/api/mypage/posts/all";

    if (admin) {
        $.get(endpoint, data => {
            const posts = data.content || [];
            renderPosts(posts, true);
            $("#roleBadge").text("관리자 모드");
        }).fail(xhr => alert(xhr.responseJSON?.message || "내 글을 불러오지 못했습니다."));
        return;
    }

    $.when(
        $.get("/api/mypage/posts/all"),
        $.get("/api/mypage/questions")
    ).done((postsRes, questionsRes) => {
        const posts = postsRes[0] || [];
        const questions = questionsRes[0] || [];
        const mapped = [
            ...posts.map(p => ({ ...p, boardType: p.boardType || "GENERAL" })),
            ...questions.map(q => ({
                id: q.id,
                title: q.title,
                authorUsername: q.authorUsername,
                createdAt: q.createdAt,
                boardType: "QNA",
                link: `qna.html?questionId=${q.id}`
            }))
        ].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

        renderPosts(mapped, false);
        $("#roleBadge").text("일반 사용자");
    }).fail((xhr) => {
        alert(xhr.responseJSON?.message || "내 글을 불러오지 못했습니다.");
    });
}

$(document).ready(loadMyPosts);