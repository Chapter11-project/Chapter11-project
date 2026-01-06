function buildCard(post, index) {
    const id = post.id || post.postId;
    const title = post.title || post.subject || "제목 없음";
    const author = post.authorUsername || post.writer || post.author || "익명";
    const content = post.content || "";
    const detailLink = id ? `community_details.html?id=${id}` : null;

    return `
      <div class="community-card">
        <div class="meta">
          <span class="badge secondary">No. ${index + 1}</span>
          <span class="author">${author}</span>
        </div>
        <h3>${title}</h3>
        <p class="excerpt">${content.length > 120 ? content.substring(0, 120) + "…" : content}</p>
        <div class="card-actions">
          ${detailLink ? `<a class="btn-link" href="${detailLink}">자세히 보기</a>` : ""}
        </div>
      </div>
    `;
}

function loadCommunity() {
    $.get("/api/community", list => {
        const $list = $("#communityList");
        $list.empty();
        (list || []).forEach((post, idx) => {
            $list.append(buildCard(post, idx));
        });
    });
}

function writePost() {
    if (!isLogin()) {
        alert("로그인이 필요합니다.");
        return;
    }
    const title = $("#title").val();
    const content = $("#content").val();
    if (!title || !content) {
        alert("제목과 내용을 입력하세요.");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/api/community",
        contentType: "application/json",
        data: JSON.stringify({ title, content }),
        success: () => {
            $("#title").val("");
            $("#content").val("");
            loadCommunity();
        },
        error: (xhr) => alert(xhr.responseJSON?.message || "등록 실패")
    });
}

$("#writeBtn").click(writePost);
$("#refreshBtn").click(loadCommunity);

$(document).ready(loadCommunity);
