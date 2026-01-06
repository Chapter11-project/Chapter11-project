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
        const link = `community_details.html?id=${post.id}`;
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

    $.get(endpoint, data => {
        const posts = admin ? (data.content || []) : data;
        renderPosts(posts, admin);
        $("#roleBadge").text(admin ? "관리자 모드" : "일반 사용자");
    }).fail(xhr => alert(xhr.responseJSON?.message || "내 글을 불러오지 못했습니다."));
}

$(document).ready(loadMyPosts);