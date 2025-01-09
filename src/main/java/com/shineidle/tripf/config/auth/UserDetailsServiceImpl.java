package com.shineidle.tripf.config.auth;

import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 입력받은 이메일에 해당하는 유저의 정보를 찾아 리턴
     * @param username username
     * @return {@link UserDetailsImpl}
     * @throws UsernameNotFoundException 이메일에 해당하는 유저를 찾지 못한 경우 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        return new UserDetailsImpl(user);
    }
}
