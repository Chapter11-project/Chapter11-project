$.get("/api/mypage", data => {
    data.posts.forEach(p => {
        $("#myPosts").append(`<div>${p.title}</div>`);
    });
});
