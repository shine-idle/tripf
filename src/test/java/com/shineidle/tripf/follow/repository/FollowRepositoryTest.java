package com.shineidle.tripf.follow.repository;
//package com.shineidle.tripf.follow.repository;

import com.shineidle.tripf.domain.follow.entity.Follow;
import com.shineidle.tripf.domain.follow.entity.FollowPk;
import com.shineidle.tripf.domain.follow.repository.FollowRepository;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.repository.UserRepository;
import com.shineidle.tripf.domain.user.type.UserAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class FollowRepositoryTest {
    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    private User follower;
    private User following;

    @BeforeEach
    void setUp() {
        follower = new User("follower@example.com",
                "password",
                "follower",
                UserAuth.NORMAL,
                "Seoul"
        );

        following = new User("following@example.com",
                "password",
                "following",
                UserAuth.NORMAL,
                "Tokyo"
        );

        userRepository.save(follower);
        userRepository.save(following);
    }

    @Test
    void testFindByFollowerId() {
        Follow follow = new Follow(follower, following);
        followRepository.save(follow);

        List<Follow> result = followRepository.findByFollowerId(follower);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(follower.getId(), result.get(0).getFollowerId().getId());
        assertEquals(following.getId(), result.get(0).getFollowingId().getId());
    }

    @Test
    void testFindByFollowingId() {
        Follow follow = new Follow(follower, following);
        followRepository.save(follow);

        List<Follow> result = followRepository.findByFollowingId(following);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(following.getId(), result.get(0).getFollowingId().getId());
        assertEquals(follower.getId(), result.get(0).getFollowerId().getId());
    }

    @Test
    void testExistsById() {
        Follow follow = new Follow(follower, following);
        followRepository.save(follow);

        FollowPk followPk = new FollowPk(follower, following);
        boolean exists = followRepository.existsById(followPk);

        assertTrue(exists);
    }

    @Test
    void testExistsById_NotExist() {
        User nonExistentUser = new User("nonExistentUser@example.com", "password", "nonExistent", UserAuth.NORMAL, "City");
        userRepository.save(nonExistentUser);

        FollowPk followPk = new FollowPk(follower, nonExistentUser);
        boolean exists = followRepository.existsById(followPk);

        assertFalse(exists);
    }

    @Test
    void testDeleteFollow() {
        Follow follow = new Follow(follower, following);
        followRepository.save(follow);

        FollowPk followPk = new FollowPk(follower, following);
        assertTrue(followRepository.existsById(followPk));

        followRepository.delete(follow);

        assertFalse(followRepository.existsById(followPk));
    }
}
