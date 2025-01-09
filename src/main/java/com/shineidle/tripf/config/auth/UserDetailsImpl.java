package com.shineidle.tripf.config.auth;

import com.shineidle.tripf.user.type.UserAuth;
import com.shineidle.tripf.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;

    /**
     * 권한 리스트를 리턴
     * @return {@code Collection<? extends GrantedAuthority>}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserAuth auth = this.user.getAuth();

        log.info("유저 권한 : {}", auth.getAuthorities());

        return new ArrayList<>(auth.getAuthorities());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }
}
