package com.shineidle.tripf.domain.follow.service;

import com.shineidle.tripf.domain.user.service.UserService;
import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.FollowErrorCode;
import com.shineidle.tripf.global.common.util.auth.UserAuthorizationUtil;
import com.shineidle.tripf.domain.follow.dto.FollowResponseDto;
import com.shineidle.tripf.domain.follow.entity.Follow;
import com.shineidle.tripf.domain.follow.entity.FollowPk;
import com.shineidle.tripf.domain.follow.repository.FollowRepository;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// TODO: 사용되지 않는 변수 삭제 유무
@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;

    /**
     * 팔로잉 추가
     *
     * @param followingId 내가 팔로우 할 사용자의 ID
     */
    @Override
    public void createFollow(Long followingId) {
        Long followerId = UserAuthorizationUtil.getLoginUserId();

        if (followerId.equals(followingId)) {
            throw new GlobalException(FollowErrorCode.NOT_SELF_FOLLOW);
        }

        User loginUser = userService.getUserById(followerId);
        User followingUser = userService.getUserById(followingId);

        FollowPk followPk = new FollowPk(loginUser, followingUser);
        if (followRepository.existsById(followPk)) {
            throw new GlobalException(FollowErrorCode.ALREADY_FOLLOWED);
        }

        Follow follow = new Follow(loginUser, followingUser);
        followRepository.save(follow);
    }

    /**
     * 팔로워 목록 조회(나를 팔로우 한 사람들)
     *
     * @return {@link FollowResponseDto} 나를 팔로우 한 사용자들의 ID 목록
     */
    @Override
    public List<FollowResponseDto> findFollowers() {
        User loginUser = userService.getUserById(UserAuthorizationUtil.getLoginUserId());

        List<Follow> followers = followRepository.findByFollowingId(loginUser);

        return followers.stream()
                .map(f -> new FollowResponseDto(f.getFollowerId().getId()))
                .collect(Collectors.toList());
    }

    /**
     * 팔로잉 목록 조회(내가 팔로우 한 사람들)
     *
     * @return  {@link FollowResponseDto}  내가 팔로우한 사용자들의 ID 목록
     */
    @Override
    public List<FollowResponseDto> findFollowings() {
        User loginUser = userService.getUserById(UserAuthorizationUtil.getLoginUserId());

        List<Follow> followings = followRepository.findByFollowerId(loginUser);

        return followings.stream()
                .map(f -> new FollowResponseDto(f.getFollowingId().getId()))
                .collect(Collectors.toList());
    }

    /**
     * 팔로잉 취소
     *
     * @param followingId 내가 팔로우한 사용자의 ID
     */
    @Override
    @Transactional
    public void deleteFollowByFollowingId(Long followingId) {
        Long followerId = UserAuthorizationUtil.getLoginUserId();

        User loginUser = userService.getUserById(followerId);
        User followingUser = userService.getUserById(followingId);

        FollowPk followPk = new FollowPk(loginUser, followingUser);
        if (followRepository.existsById(followPk)) {
            followRepository.deleteById(followPk);
        } else {
            throw new GlobalException(FollowErrorCode.FOLLOW_RELATION_NOT_FOUND);
        }
    }

    /**
     * 팔로워 취소
     *
     * @param followerId 나를 팔로우한 사용자의 ID
     */
    @Override
    @Transactional
    public void deleteFollowByFollowerId(Long followerId) {
        Long followingId = UserAuthorizationUtil.getLoginUserId();

        User loginUser = userService.getUserById(followingId);
        User followerUser = userService.getUserById(followerId);

        FollowPk followPk = new FollowPk(followerUser, loginUser);
        if (followRepository.existsById(followPk)) {
            followRepository.deleteById(followPk);
        } else {
            throw new GlobalException(FollowErrorCode.FOLLOW_RELATION_NOT_FOUND);
        }
    }
}