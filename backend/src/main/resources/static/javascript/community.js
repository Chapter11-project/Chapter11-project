const COMMUNITY_BOARD = "GENERAL";

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

        function renderCommunity(list) {
            const $tbody = $("#communityList");
            const $empty = $("#communityEmpty");
            $tbody.empty();

            if (!list || list.length === 0) {
                $empty.removeClass("hidden");
                return;
            }
            $empty.addClass("hidden");

            list.forEach(post => {
                const createdAt = formatDate(post.createdAt);
                $tbody.append(`
            <tr class="board-row community-row" data-id="${post.id}">
                <td><span class="badge secondary">${boardLabel(post.boardType)}</span></td>
                <td class="board-title">${post.title}</td>
                <td>${post.authorUsername}</td>
                <td>${createdAt}</td>
            </tr>
        `);
            });
        }

        function loadCommunity() {
            $.get("/api/posts", {boardType: COMMUNITY_BOARD}, list => {
                renderCommunity(list);
            }).fail(xhr => alert(xhr.responseJSON?.message || "게시글을 불러오지 못했습니다."));
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
                url: "/api/posts",
                contentType: "application/json",
                data: JSON.stringify({ title, content, boardType: COMMUNITY_BOARD }),
                success: () => {
                    $("#title").val("");
                    $("#content").val("");
                    loadCommunity();
                },
                error: (xhr) => alert(xhr.responseJSON?.message || "등록 실패")
            });
        }

        $(document).on("click", ".community-row", function () {
            const id = $(this).data("id");
            location.href = `community_details.html?id=${id}`;
        });

        $("#writeBtn").click(writePost);
        $("#refreshBtn").click(loadCommunity);

        $(document).ready(loadCommunity);