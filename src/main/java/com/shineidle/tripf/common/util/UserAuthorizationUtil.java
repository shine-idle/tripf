package com.shineidle.tripf.common.util;

import com.shineidle.tripf.config.auth.UserDetailsImpl;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.UserRepository;
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

        Object principal = authentication.getPrincipal();

        if (principal instanceof String) {
            // `String` 타입이면 다시 DB에서 `UserDetailsImpl`을 로드
            return loadUserDetailsByUsername((String) principal);
        } else if (principal instanceof UserDetailsImpl) {
            return (UserDetailsImpl) principal;
        } else {
            throw new IllegalStateException("인증된 사용자 정보가 예상과 다릅니다.");
        }
    }

    private static UserDetailsImpl loadUserDetailsByUsername(String username) {
        UserRepository userRepository = ApplicationContextProvider.getBean(UserRepository.class);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        return new UserDetailsImpl(user);
    }

    /**
     * JWT에서 USER 타입으로 로그인한 유저 정보 반환
     * @return User
     */
    public static User getLoginUser() {
        return getUserDetails().getUser();
    }

    /**
     * JWT에서 Long 타입으로 로그인한 유저 Id 반환
     * @return UserId
     */
    public static Long getLoginUserId() {
        return getUserDetails().getUserId();
    }

    /**
     * JWT에서 String 타입으로 로그인한 유저 Email 반환
     * @return Username
     */
    public static String getLoginUserEmail() {
        return getUserDetails().getUsername();
    }

    /**
     * JWT에서 UserAuth 타입으로 로그인한 유저 권한 반환
     * @return UserAuthority
     */
    public static UserAuth getLoginUserAuthority() {
        return getUserDetails().getUserAuthority();
    }

    /**
     * JWT에서 String 타입으로 로그인한 유저 비밀번호 반환
     * @return Password
     */
    public static String getLoginUserPassword() {
        return getUserDetails().getPassword();
    }
}
