package com.shineidle.tripf.user;

import lombok.Getter;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
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
