package com.shineidle.tripf.domain.user.type;

import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.UserErrorCode;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter
public enum UserAuth {
    NORMAL("normal"),
    ADMIN("admin");

    private final String name;

    UserAuth(String name) {
        this.name = name;
    }

    /**
     * 주어진 문자열을 기반으로 해당 권한을 반환
     *
     * @param userAuth 권한 이름
     * @return 해당 권한
     */
    public static UserAuth of(String userAuth) throws IllegalArgumentException {
        for (UserAuth auth : values()) {
            if (auth.getName().equals(userAuth.toLowerCase())) {
                return auth;
            }
        }
        throw new GlobalException(UserErrorCode.INVALID_AUTH);
    }

    /**
     * 권한 목록을 반환
     *
     * @return 권한 목록
     */
    public List<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("AUTH_" + this.name));
    }
}