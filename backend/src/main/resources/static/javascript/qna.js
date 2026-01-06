let qnaCache = [];
let selectedQuestionId = null;

function formatDate(dateString) {
    if (!dateString) return "-";
    const d = new Date(dateString);
    return `${d.getFullYear()}.${String(d.getMonth() + 1).padStart(2, "0")}.${String(d.getDate()).padStart(2, "0")}`;
}

function statusBadge(status) {
    const label = status === "SOLVED" ? "해결됨" : "대기";
    return `<span class="status-badge ${status === "SOLVED" ? "solved" : "waiting"}">${label}</span>`;
}

function renderTable() {
    const $tbody = $("#qnaTableBody");
    const $empty = $("#qnaEmpty");
    $tbody.empty();

    if (!qnaCache || qnaCache.length === 0) {
        $empty.removeClass("hidden");
        $("#qnaDetail").addClass("hidden");
        return;
    }
    $empty.addClass("hidden");

    qnaCache.forEach(q => {
        $tbody.append(`
            <tr class="board-row qna-row" data-id="${q.id}">
                <td>${statusBadge(q.status)}</td>
                <td class="board-title">${q.title}</td>
                <td>${q.authorUsername}</td>
                <td>${formatDate(q.createdAt)}</td>
                <td>${q.answers?.length || 0}</td>
            </tr>
        `);
    });
}
    function renderAnswers(qustion) {
        const answers = [...(question.answers || [])].sort((a, b) => (b.accepted === true) - (a.accepted === true));
        const $list = $("#answerList");
        $list.empty();

        if (answers.length === 0) {
            $list.append("<p class=\"board-empty\">아직 등록된 답변이 없습니다.</p>");
            return;
        }

        answers.forEach(a => {
            const canAccept = question.status === "WAITING"
                && currentUsername() === question.authorUsername
                && !isAdmin()
                && !a.accepted

            && isLogin();
        const acceptBtn = canAccept ? `<button class="btn-outline accept-btn" data-question="${question.id}" data-answer="${a.id}">채택하기</button>` : "";

            $list.append(`
            <div class="answer ${a.accepted ? "accepted" : ""}">
                <div class="answer-meta">
                    <span>${a.authorUsername}</span>
                    <span>${new Date(a.createdAt).toLocaleString()}</span>
                </div>
                <p>${a.content}</p>
                <div class="actions">
                    ${a.accepted ? '<span class="badge accepted">✅ 채택됨</span>' : acceptBtn}
                </div>
            </div>
        `);
    });
}


function openQuestion(questionId) {
    const question = qnaCache.find(q => q.id === questionId);
    if (!question) {
        return;
    }
    selectedQuestionId = questionId;

    $("#qnaDetail").removeClass("hidden");
    $("#qnaStatus").attr("class", `status-badge ${question.status === "SOLVED" ? "solved" : "waiting"}`).text(question.status === "SOLVED" ? "해결됨" : "대기");
    $("#qnaTitle").text(question.title);
    $("#qnaAuthor").text(question.authorUsername);
    $("#qnaDate").text(formatDate(question.createdAt));
    $("#qnaContent").text(question.content);

    renderAnswers(question);

    if (question.status === "WAITING" && isLogin()) {
        $("#answerForm").removeClass("hidden");
    } else {
        $("#answerForm").addClass("hidden");
    }
}

function loadQuestions(openId) {
    $.get("/api/qna", res => {

        qnaCache = res.data || [];
        renderTable();
        if (openId) {
            openQuestion(openId);
        } else if (qnaCache.length > 0) {
            openQuestion(qnaCache[0].id);
        }
    });
}

$("#writeBtn").click(() => {
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
        url: "/api/qna",
        contentType: "application/json",
        data: JSON.stringify({ title, content }),
        success: () => {
            $("#title").val("");
            $("#content").val("");
            loadQuestions();
        },
        error: (xhr) => alert(xhr.responseJSON?.message || "작성 실패")
    });
});

$("#refreshBtn").click(() => loadQuestions(selectedQuestionId));

$(document).on("click", ".qna-row", function () {
    const id = $(this).data("id");
    openQuestion(id);
});

$("#answerSubmit").click(() => {
    if (!selectedQuestionId) return;
    const content = $("#answerContent").val();
    if (!content) {
        alert("답변을 입력하세요.");
        return;
    }
    $.ajax({
        type: "POST",
        url: `/api/qna/${selectedQuestionId}/answers`,
            contentType: "application/json",
            data: JSON.stringify({content}),
            success: () => {
            $("#answerContent").val("");
            loadQuestions(selectedQuestionId);
        },
            error: (xhr) => alert(xhr.responseJSON?.message || "등록 실패")
    });
});

$(document).on("click", ".accept-btn", function () {
    const qId = $(this).data("question");
    const aId = $(this).data("answer");
    $.post(`/api/qna/${qId}/answers/${aId}/accept`, () => {
        loadQuestions();
        loadQuestions(qId);
    }).fail(xhr => alert(xhr.responseJSON?.message || "채택 실패"));
});

$(document).ready(() => {
    const params = new URLSearchParams(window.location.search);
    const fromLinkId = params.get("questionId");
    loadQuestions(fromLinkId ? Number(fromLinkId) : undefined);
});