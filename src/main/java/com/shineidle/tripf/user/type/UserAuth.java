package com.shineidle.tripf.user.type;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

// TODO : javadoc 작성
// TODO : 예외처리 수정 필요, 하드코딩된 문자열 처리
@Getter
public enum UserAuth {
    NORMAL("normal"),
    ADMIN("admin");

    private final String name;

    UserAuth(String name) {
        this.name = name;
    }

    public static UserAuth of(String userAuth) throws IllegalArgumentException {
        for (UserAuth auth : values()) {
            if (auth.getName().equals(userAuth.toLowerCase())) {
                return auth;
            }
        }
        throw new IllegalArgumentException(userAuth + "권한은 존재하지 않습니다.");
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("AUTH_" + this.name));
    }
}