$(document).ready(async function () {
    await loadCurrentUser();

    if (!currentUser) {
        alert("로그인이 필요합니다");
        location.href = "/login.html";
        return;
    }

    $("#submitBtn").on("click", async function () {
        const title = $("#postTitle").val().trim();
        const content = $("#postContent").val().trim();

        if (!title || !content) return alert("제목과 내용을 입력하세요");

        const res = await fetch("/api/posts", {
            method: "POST",
            headers: authHeader(),
            body: JSON.stringify({
                title,
                content,
                boardType: "COMMUNITY"
            })
        });

        if (res.ok) {
            alert("글 작성 완료");
            location.href = "/community.html";
        } else {
            alert("글 작성 실패");
        }
    });
});
