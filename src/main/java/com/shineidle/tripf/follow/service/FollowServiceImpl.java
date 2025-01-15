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
     * @param followingId 내가 팔로잉 할 사람의 Id
     */
    @Override
    public void createFollow(Long followingId) {
        User loginUser = UserAuthorizationUtil.getLoginUser();

        Long followerId = loginUser.getId();

        if (followerId.equals(followingId)) {
            throw new GlobalException(FollowErrorCode.NOT_SELF_FOLLOW);
        }

       userRepository.findById(followingId)
                .orElseThrow(() -> new GlobalException(FollowErrorCode.NOT_FOUND_FOLLOW));


        FollowPk followPk = new FollowPk(followerId, followingId);
        if (followRepository.existsById(followPk)) {
            throw new GlobalException(FollowErrorCode.ALREADY_FOLLOWED);
        }

        Follow follow = new Follow(followerId, followingId);
        followRepository.save(follow);
    }

    /**
     * 팔로워 조회(나를 팔로우한 사람들)
     * @return List<FollowResponseDto> 팔로워들 리스트
     */
    @Override
    public List<FollowResponseDto> findFollowers() {
        User loginUser = UserAuthorizationUtil.getLoginUser();
        Long followingId = loginUser.getId();

        List<Follow> followers = followRepository.findByFollowingId(followingId);

        return followers.stream()
                .map(f -> new FollowResponseDto(f.getFollowerId()))
                .collect(Collectors.toList());
    }

    /**
     * 팔로잉 조회(내가 팔로우한 사람들)
     * @return List<FollowResponseDto> 팔로잉 한 사람들 리스트
     */
    @Override
    public List<FollowResponseDto> findFollowings() {
        User loginUser = UserAuthorizationUtil.getLoginUser();
        Long followerId = loginUser.getId();

        List<Follow> followings = followRepository.findByFollowerId(followerId);

        return followings.stream()
                .map(f -> new FollowResponseDto(f.getFollowingId()))
                .collect(Collectors.toList());
    }

    /**
     *팔로잉 취소
     * @param followingId 내가 팔로잉한 사람의 Id
     */
    @Override
    @Transactional
    public void deleteFollowByFollowingId(Long followingId) {
        User loginUser = UserAuthorizationUtil.getLoginUser();
        Long followerId = loginUser.getId();

        FollowPk followPk = new FollowPk(followerId, followingId);
        if (followRepository.existsById(followPk)) {
            followRepository.deleteById(followPk);
        } else {
            throw new GlobalException(FollowErrorCode.FOLLOW_RELATION_NOT_FOUND);
        }
    }

    /**
     * 팔로우 취소
     * @param followerId 나를팔로우 한 사람의 Id
     */
    @Override
    @Transactional
    public void deleteFollowByFollowerId(Long followerId) {
        User loginUser = UserAuthorizationUtil.getLoginUser();
        Long followingId = loginUser.getId();

        FollowPk followPk = new FollowPk(followerId, followingId);
        if (followRepository.existsById(followPk)) {
            followRepository.deleteById(followPk);
        } else {
            throw new GlobalException(FollowErrorCode.FOLLOW_RELATION_NOT_FOUND);
        }
    }
}

