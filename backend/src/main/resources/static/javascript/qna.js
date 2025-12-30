$(document).ready(function () {
    loadQna();
});

function loadQna() {
    $.ajax({
        url: "http://localhost:8080/api/posts?boardType=QNA",
        type: "GET",
        success: function (posts) {
            const tbody = $("#qnaList");
            tbody.empty();

            posts.forEach(post => {
                tbody.append(`
                    <tr>
                        <td>${post.id}</td>
                        <td>${post.title}</td>
                        <td>${post.authorUsername}</td>
                    </tr>
                `);
            });
        }
    });
}

$("#writeBtn").click(function () {
    $.ajax({
        url: "http://localhost:8080/api/posts",
        type: "POST",
        headers: authHeader(),
        data: JSON.stringify({
            title: $("#title").val(),
            content: $("#content").val(),
            boardType: "QNA"
        }),
        success: function () {
            alert("질문 등록 완료");
            loadQna();
        },
        error: function () {
            alert("로그인이 필요합니다");
        }
    });
});
