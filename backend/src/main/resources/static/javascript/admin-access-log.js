$(document).ready(function () {

    if (!isLoggedIn()) {
        alert("로그인이 필요합니다");
        location.href = "/login.html";
        return;
    }

    loadAccessLogs();
});

async function loadAccessLogs() {
    try {
        const res = await fetch("/api/admin/access-logs", {
            headers: authHeader()
        });

        if (res.status === 403) {
            alert("관리자 권한이 필요합니다");
            return;
        }

        if (!res.ok) {
            alert("접속 로그 조회 실패");
            return;
        }

        const logs = await res.json();
        const tbody = $("#logTableBody");
        tbody.empty();

        logs.forEach(log => {
            tbody.append(`
                <tr>
                    <td>${log.username}</td>
                    <td>${log.ipAddress}</td>
                    <td>${log.requestUri}</td>
                    <td>${log.httpMethod}</td>
                    <td>${log.accessedAt.replace("T", " ").substring(0, 19)}</td>
                </tr>
            `);
        });

    } catch (e) {
        console.error("접속 로그 오류", e);
        alert("서버 오류 발생");
    }
}
