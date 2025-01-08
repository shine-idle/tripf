package com.shineidle.tripf.user.service;

import com.shineidle.tripf.user.dto.UserRequestDto;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public String create(UserRequestDto dto) {
        User user = new User(dto.getEmail(), dto.getPassword(), dto.getName(), dto.getAuth(), dto.getAddress());
        userRepository.save(user);
        return "회원 가입이 완료되었습니다! 이제 모든 서비스를 마음껏 이용해 보세요.";
    }
}
