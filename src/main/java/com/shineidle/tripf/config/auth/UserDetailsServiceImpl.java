package com.shineidle.tripf.config.auth;

import com.shineidle.tripf.common.util.RedisUtils;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final RedisUtils redisUtils;

    /**
     * 입력받은 이메일에 해당하는 유저의 정보를 찾아 리턴 (카카오의 경의 providerId)
     *
     * @param username 유저 이름
     * @return {@link UserDetailsImpl}
     * @throws UsernameNotFoundException 이메일에 해당하는 유저를 찾지 못한 경우 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User orFetchFromDB = redisUtils.getOrFetchFromDB(username, () -> loadUser(username), Duration.ofMinutes(3));
        return new UserDetailsImpl(orFetchFromDB);
    }

    private User loadUser(String username) {

        return this.userRepository.findByEmail(username)
                .orElseGet(() -> this.userRepository.findByProviderId(username)
                        .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다.")));
    }
}
