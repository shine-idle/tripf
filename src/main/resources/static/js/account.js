// static/js/account.js
import { getJwtToken, clearAllCookies } from "/js/jwt-utils.js";

document.addEventListener("DOMContentLoaded", function() {
    const jwtToken = getJwtToken();
    const dropdown = document.getElementById("accountDropdown");

    if (jwtToken) {
        dropdown.innerHTML = `
            <a href="/mypage">마이페이지</a>
            <a href="/feeds/myFeed">나의여행</a>
            <a href="/logout" id="logoutLink">로그아웃</a>
        `;

        // 로그아웃 시 쿠키 삭제 및 페이지 이동
        document.getElementById("logoutLink").addEventListener("click", function(event) {
            event.preventDefault();
            clearAllCookies();
            window.location.href = "/logout";
        });
    } else {
        dropdown.innerHTML = `<a href="/login">로그인</a>`;
    }
});