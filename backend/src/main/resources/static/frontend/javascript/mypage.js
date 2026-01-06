$(document).ready(function() {
    if (!isLogin()) {
        alert("로그인이 필요합니다.");
        location.href = "login.html";
        return;
    }

    $.ajax({
        type: "GET",
        url: "/api/mypage",
        success: function(data) {
            $("#myPosts").empty();
            // data.posts 가 리스트인 경우를 가정 (백엔드 구조에 맞춰 수정)
            const list = data.posts || data;

            if(list.length === 0) {
                $("#myPosts").append("<tr><td colspan='3'>작성한 글이 없습니다.</td></tr>");
            } else {
                list.forEach(p => {
                    $("#myPosts").append(`
                        <tr>
                            <td>일반게시판</td>
                            <td style="cursor:pointer" onclick="location.href='community_detail.html?id=${p.id}'">${p.title}</td>
                            <td>${p.createdAt || '-'}</td>
                        </tr>
                    `);
                });
            }
        },
        error: function(err) {
            if(err.status === 403) alert("로그인 세션이 만료되었습니다.");
        }
    });
});