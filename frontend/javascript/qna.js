$.get("/api/questions", list => {
    list.forEach(q => {
        $("#qnaList").append(`
      <tr>
        <td>${q.title}</td>
        <td>${q.status}</td>
      </tr>
    `);
    });
});
