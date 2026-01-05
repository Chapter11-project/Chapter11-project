function loadCommunity() {
    $.get("/api/community", list => {
        $("#communityList").empty();
        list.forEach(p => {
            $("#communityList").append(`
        <tr>
          <td>${p.title}</td>
          <td>${p.writer}</td>
        </tr>
      `);
        });
    });
}

$("#writeBtn").click(() => {
    $.ajax({
        type: "POST",
        url: "/api/community",
        contentType: "application/json",
        data: JSON.stringify({
            title: $("#title").val(),
            content: $("#content").val()
        }),
        success: loadCommunity
    });
});

loadCommunity();
