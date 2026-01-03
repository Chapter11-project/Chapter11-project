$(document).ready(function() {
    loadCommunity();

    $("#writeBtn").click(function() {
        if (!isLogin()) {
            alert("로그인이 필요합니다.");
            return;
        }
        const title = $("#title").val();
        const content = $("#content").val();

        $.ajax({
            type: "POST",
            url: "/api/community",
            contentType: "application/json",
            data: JSON.stringify({ title, content }),
            success: function() {
                $("#title").val("");
                $("#content").val("");
                loadCommunity();
            }
        });
    });
});

function loadCommunity() {
    $.get("/api/community", function(list) {
        $("#communityList").empty();
        list.forEach(p => {
            $("#communityList").append(`
                <tr>
                    <td>${p.id}</td>
                    <td class="qna-title" style="cursor:pointer" onclick="location.href='community_detail.html?id=${p.id}'">${p.title}</td>
                    <td>${p.writer}</td>
                    <td>${p.createdAt || '-'}</td>
                    <td><span class="status-done">게시중</span></td>
                </tr>
            `);
        });
    });
}