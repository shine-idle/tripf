package com.shineidle.tripf.common.util;

import com.shineidle.tripf.config.auth.UserDetailsImpl;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.type.UserAuth;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAuthorizationUtil {
    private UserAuthorizationUtil() {
        throw new AssertionError();
    }

    private static UserDetailsImpl getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("인증된 사용자가 없습니다.");
        }

        return (UserDetailsImpl) authentication.getPrincipal();
    }


    public static User getLoginUser() {
        return getUserDetails().getUser();
    }

    public static Long getLoginUserId() {
        return getUserDetails().getUserId();
    }

    public static String getLoginUserEmail() {
        return getUserDetails().getUsername();
    }

    public static UserAuth getLoginUserAuthority() {
        return getUserDetails().getUserAuthority();
    }

    public static String getLoginUserPassword() {
        return getUserDetails().getPassword();
    }
}
