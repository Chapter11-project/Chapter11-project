$(document).ready(async function () {

    // 로그인 + 관리자 권한 체크
    if (!isLoggedIn()) {
        alert("로그인이 필요합니다");
        location.href = "/login.html";
        return;
    }

    await loadCurrentUser();

    if (currentUser.role !== "ADMIN") {
        alert("관리자 권한이 없습니다");
        location.href = "/home.html";
        return;
    }

    loadAccessLogs();
});

async function loadAccessLogs() {
    try {
        const res = await fetch("/api/admin/access-logs", {
            headers: authHeader()
        });

        if (!res.ok) {
            alert("접속 로그 조회 실패");
            return;
        }

        const page = await res.json();
        const logs = page.content;

        const tbody = $("#accessLogList");
        tbody.empty();

        logs.forEach(log => {
            tbody.append(`
                <tr>
                    <td>${log.username}</td>
                    <td>${log.ipAddress}</td>
                    <td>${log.requestUri}</td>
                    <td>${log.httpMethod}</td>
                    <td>${log.accessedAt?.substring(0,19).replace('T',' ')}</td>
                </tr>
            `);
        });

    } catch (e) {
        console.error(e);
        alert("오류 발생");
    }
}
