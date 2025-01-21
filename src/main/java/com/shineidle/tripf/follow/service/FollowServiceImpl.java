package com.shineidle.tripf.follow.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.FollowErrorCode;
import com.shineidle.tripf.common.util.UserAuthorizationUtil;
import com.shineidle.tripf.follow.dto.FollowResponseDto;
import com.shineidle.tripf.follow.entity.Follow;
import com.shineidle.tripf.follow.entity.FollowPk;
import com.shineidle.tripf.follow.repository.FollowRepository;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    /**
     * 팔로잉 추가
     *
     * @param followingId 팔로잉 식별자(내가 팔로우 할 사람의 식별자)
     */
    @Override
    public void createFollow(Long followingId) {
        User loginUser = UserAuthorizationUtil.getLoginUser();
        Long followerId = loginUser.getId();

        if (followerId.equals(followingId)) {
            throw new GlobalException(FollowErrorCode.NOT_SELF_FOLLOW);
        }

       User followingUser = userRepository.findById(followingId)
                .orElseThrow(() -> new GlobalException(FollowErrorCode.NOT_FOUND_FOLLOW));


        FollowPk followPk = new FollowPk(loginUser, followingUser);
        if (followRepository.existsById(followPk)) {
            throw new GlobalException(FollowErrorCode.ALREADY_FOLLOWED);
        }

        loginUser = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new GlobalException(FollowErrorCode.NOT_FOUND_FOLLOW));

        followingUser = userRepository.findById(followingUser.getId())
                .orElseThrow(() -> new GlobalException(FollowErrorCode.NOT_FOUND_FOLLOW));

        Follow follow = new Follow(loginUser, followingUser);
        followRepository.save(follow);
    }

    /**
     * 팔로워 조회(나를 팔로우한 사람들)
     *
     * @return {@link FollowResponseDto} 팔로우 응답 Dto
     */
    @Override
    public List<FollowResponseDto> findFollowers() {
        User loginUser = UserAuthorizationUtil.getLoginUser();
        Long followingId = loginUser.getId();

        List<Follow> followers = followRepository.findByFollowingId(loginUser);

        return followers.stream()
                .map(f -> new FollowResponseDto(f.getFollowerId().getId()))
                .collect(Collectors.toList());
    }

    /**
     * 팔로잉 조회(내가 팔로우한 사람들)
     *
     * @return {@link FollowResponseDto} 팔로우 응답 Dto
     */
    @Override
    public List<FollowResponseDto> findFollowings() {
        User loginUser = UserAuthorizationUtil.getLoginUser();
        Long followerId = loginUser.getId();

        List<Follow> followings = followRepository.findByFollowerId(loginUser);

        return followings.stream()
                .map(f -> new FollowResponseDto(f.getFollowingId().getId()))
                .collect(Collectors.toList());
    }

    /**
     * 팔로잉 취소
     *
     * @param followingId  팔로잉 식별자(내가 팔로우한 사람의 식별자)
     */
    @Override
    @Transactional
    public void deleteFollowByFollowingId(Long followingId) {
        User loginUser = UserAuthorizationUtil.getLoginUser();
        Long followerId = loginUser.getId();

        User followingUser = userRepository.findById(followingId)
                .orElseThrow(() -> new GlobalException(FollowErrorCode.NOT_FOUND_FOLLOW));

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
     * @param followerId 팔로워 식별자(나를 팔로우한 사람의 식별자)
     */
    @Override
    @Transactional
    public void deleteFollowByFollowerId(Long followerId) {
        User loginUser = UserAuthorizationUtil.getLoginUser();
        Long followingId = loginUser.getId();

        User followerUser = userRepository.findById(followerId)
                .orElseThrow(() -> new GlobalException(FollowErrorCode.NOT_FOUND_FOLLOW));

        FollowPk followPk = new FollowPk(followerUser, loginUser);
        if (followRepository.existsById(followPk)) {
            followRepository.deleteById(followPk);
        } else {
            throw new GlobalException(FollowErrorCode.FOLLOW_RELATION_NOT_FOUND);
        }
    }
}

