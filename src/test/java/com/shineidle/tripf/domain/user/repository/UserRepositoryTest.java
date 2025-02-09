package com.shineidle.tripf.domain.user.repository;

import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.type.UserAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User activeUser;
    private User deletedUser;
    private User socialLoginUser;

    @BeforeEach
    void setUp() {
        activeUser = new User("activeUser@gmail.com", "password", "activeUser", UserAuth.NORMAL, "address");
        deletedUser = new User("deletedUser@gmail.com", "password", "deletedUser", UserAuth.NORMAL, "address");
        socialLoginUser = new User("social@gmail.com", "socialLoginUser", "Kakao", "provider123");
    }

    @Test
    @DisplayName("이메일로 유저 찾기")
    void testFindByEmail() {
        // Given
        User savedUser = userRepository.save(activeUser);

        // When
        Optional<User> foundUser = userRepository.findByEmail(savedUser.getEmail());

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("activeUser@gmail.com");
    }

    @Test
    @DisplayName("ProviderId로 유저 찾기")
    void testFindByProviderId() {
        // Given
        userRepository.save(socialLoginUser);

        // When
        Optional<User> foundUser = userRepository.findByProviderId("provider123");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getProviderId()).isEqualTo("provider123");
    }

    @Test
    @DisplayName("활성 유저 이메일 목록 조회")
    void testFindAllActiveEmails() {
        // Given
        userRepository.save(activeUser);
        User savedDeletedUser = userRepository.save(deletedUser);
        savedDeletedUser.deactivate();

        // When
        List<String> activeEmails = userRepository.findAllActiveEmails();

        // Then
        assertThat(activeEmails).containsExactly("activeUser@gmail.com");
        assertThat(activeEmails).doesNotContain("deletedUser@gmail.com");
    }
}
