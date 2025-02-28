// static/js/jwt-utils.js
// 쿠키에서 JWT 토큰 가져오기
export function getJwtToken() {
    const cookies = document.cookie.split('; ');
    for (const cookie of cookies) {
        const [name, value] = cookie.split('=');
        if (name === 'Authorization') {
            return value;
        }
    }
    return null;
}

// JWT 토큰을 사용해 API 호출 예시
export async function postWithJwt(url, data) {
    const token = getJwtToken();
    if (!token) {
        alert("로그인이 필요합니다.");
        window.location.href = "/login";
        return;
    }

    const response = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(data)
    });

    return response.json();
}

// 쿠키 삭제 (로그아웃 시 활용)
export function clearAllCookies() {
    document.cookie.split("; ").forEach(cookie => {
        const [name] = cookie.split("=");
        document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
    });
}