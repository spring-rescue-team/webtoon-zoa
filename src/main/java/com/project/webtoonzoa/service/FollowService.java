package com.project.webtoonzoa.service;

import com.project.webtoonzoa.dto.FollowResponseDto;
import com.project.webtoonzoa.entity.Follow;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.repository.FollowRepository;
import com.project.webtoonzoa.repository.UserRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    private User checkExistUser(User user) {
        return userRepository.findById(user.getId()).orElseThrow(
            () -> new NoSuchElementException("유저가 존재하지 않습니다."));
    }

    private Follow checkExistFollow(Long followerId, Long followingId) {
        return followRepository.findByFollowerIdAndFollowingId(followerId, followingId).orElseThrow(
            () -> new NoSuchElementException("해당 팔로우가 존재하지 않습니다.")
        );
    }

    @Transactional
    public FollowResponseDto createFollow(User user, Long followingId) {
        User savedUser = checkExistUser(user);
        if (followRepository.existsByFollowerIdAndFollowingId(savedUser.getId(), followingId)) {
            throw new DataIntegrityViolationException("이미 해당 유저를 팔로우했습니다.");
        }
        Follow savedFollow = followRepository.save(
            new Follow(savedUser.getId(), followingId));
        return new FollowResponseDto(savedFollow);
    }

    @Transactional
    public FollowResponseDto deleteFollow(User user, Long followingId) {
        User savedUser = checkExistUser(user);
        Follow savedFollow = checkExistFollow(savedUser.getId(), followingId);
        followRepository.delete(savedFollow);

        return new FollowResponseDto(savedFollow);
    }
}
