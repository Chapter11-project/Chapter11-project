function renderAnswers(question) {
    const sorted = [...question.answers].sort((a, b) => (b.accepted === true) - (a.accepted === true));
    return sorted.map(a => {
        const acceptBtn = (question.status === "WAITING"
            && currentUsername() === question.authorUsername
            && !isAdmin()
            && !a.accepted
            && isLogin())
            ? `<button class="accept-btn" data-question="${question.id}" data-answer="${a.id}">채택하기</button>`
            : "";
        return `
          <div class="answer ${a.accepted ? "accepted" : ""}">
            <div class="meta">
              <span>${a.authorUsername}</span>
              <span>${new Date(a.createdAt).toLocaleString()}</span>
            </div>
            <div>${a.content}</div>
            <div class="actions">
              ${a.accepted ? '<span class="badge accepted">✅ 채택됨</span>' : acceptBtn}
            </div>
          </div>
        `;
    }).join("");
}

function renderQuestionCard(question) {
    const badgeClass = question.status === "SOLVED" ? "solved" : "waiting";
    const answers = renderAnswers(question);
    const answerBox = isLogin() ? `
      <div class="qna-actions">
        <textarea placeholder="답변을 입력하세요" data-question="${question.id}" class="answer-content"></textarea>
        <button class="answer-submit" data-question="${question.id}">등록</button>
      </div>
    ` : '<p>로그인 후 답변을 작성할 수 있습니다.</p>';

    return `
      <div class="qna-card" data-question="${question.id}">
        <div class="meta">
          <span class="badge ${badgeClass}">${question.status}</span>
          <span>${question.authorUsername}</span>
          <span>${new Date(question.createdAt).toLocaleDateString()}</span>
        </div>
        <h3>${question.title}</h3>
        <p>${question.content}</p>
        <div class="answer-list">
          ${answers || "<p>아직 답변이 없습니다.</p>"}
        </div>
        ${question.status === "WAITING" ? answerBox : ""}
      </div>
    `;
}

function loadQuestions() {
    $.get("/api/qna", res => {
        $("#qnaList").empty();
        const list = res.data || [];
        list.forEach(q => $("#qnaList").append(renderQuestionCard(q)));
    });
}

$("#writeBtn").click(() => {
    if (!isLogin()) {
        alert("로그인이 필요합니다.");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/api/qna",
        contentType: "application/json",
        data: JSON.stringify({
            title: $("#title").val(),
            content: $("#content").val()
        }),
        success: () => {
            $("#title").val("");
            $("#content").val("");
            loadQuestions();
        },
        error: (xhr) => alert(xhr.responseJSON?.message || "작성 실패")
    });
});

$(document).on("click", ".answer-submit", function () {
    const qId = $(this).data("question");
    const content = $(this).siblings(".answer-content").val();
    if (!content) {
        alert("답변을 입력하세요.");
        return;
    }
    $.ajax({
        type: "POST",
        url: `/api/qna/${qId}/answers`,
        contentType: "application/json",
        data: JSON.stringify({content}),
        success: loadQuestions,
        error: (xhr) => alert(xhr.responseJSON?.message || "등록 실패")
    });
});

$(document).on("click", ".accept-btn", function () {
    const qId = $(this).data("question");
    const aId = $(this).data("answer");
    $.post(`/api/qna/${qId}/answers/${aId}/accept`, () => {
        loadQuestions();
    }).fail(xhr => alert(xhr.responseJSON?.message || "채택 실패"));
});

loadQuestions();