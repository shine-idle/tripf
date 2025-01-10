package com.shineidle.tripf.common.util;

import com.shineidle.tripf.config.auth.UserDetailsImpl;
import com.shineidle.tripf.user.type.UserAuth;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAuthorizationUtil {
    private UserAuthorizationUtil() {
        throw new AssertionError();
    }

    final static Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final static UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    public static Long getLoginUserId() {
        return userDetails.getUserId();
    }

    public static String getLoginUserEmail() {
        return userDetails.getUsername();
    }

    public static UserAuth getLoginUserAuthority() {
        return userDetails.getUserAuthority();
    }
}
