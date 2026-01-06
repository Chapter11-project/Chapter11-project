function renderNotifications(list) {
    const dropdown = $("#notificationDropdown");
    const badge = $("#notificationBadge");
    const listWrap = $("#notificationList");
    const empty = $(".notification-empty");

    listWrap.empty();
    empty.addClass("hidden");

    const unreadCount = list.filter(n => !n.read).length;
    if (unreadCount > 0) {
        badge.text(unreadCount).removeClass("hidden");
    } else {
        badge.addClass("hidden");
    }

    if (list.length === 0) {
        empty.removeClass("hidden");
        return;
    }

    list.forEach(n => {
        const item = $(`
          <li class="${n.read ? "read" : "unread"}">
            <span class="msg">${n.message}</span>
            <span class="time">${new Date(n.createdAt).toLocaleString()}</span>
          </li>
        `);
        item.click(() => {
            $.post(`/notifications/read/${n.id}`, () => {
                if (n.link) {
                    location.href = n.link;
                } else {
                    loadNotifications();
                }
            });
        });
        listWrap.append(item);
    });
}

function loadNotifications() {
    if (!isLogin()) {
        return;
    }
    $.get("/notifications", res => {
        renderNotifications(res.data || []);
    });
}

$(document).ready(() => {
    $("#notificationBtn").click(() => {
        $("#notificationDropdown").toggleClass("hidden");
    });
    $(document).click((e) => {
        if (!$(e.target).closest(".notification-area").length) {
            $("#notificationDropdown").addClass("hidden");
        }
    });
    loadNotifications();
});