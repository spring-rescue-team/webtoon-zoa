package com.project.webtoonzoa.service;

import com.project.webtoonzoa.dto.WebtoonLikesResponseDto;
import com.project.webtoonzoa.dto.WebtoonRequestDto;
import com.project.webtoonzoa.dto.WebtoonResponseDto;
import com.project.webtoonzoa.entity.CommentLikes;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.entity.Webtoon;
import com.project.webtoonzoa.entity.WebtoonLikes;
import com.project.webtoonzoa.repository.UserRepository;
import com.project.webtoonzoa.repository.WebtoonLikesRepository;
import com.project.webtoonzoa.repository.WebtoonRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonLikesRepository webtoonLikesRepository;
    private final UserRepository userRepository;

    @Transactional
    public WebtoonResponseDto createWebtoon(User user, WebtoonRequestDto requestDto) {
        Webtoon savedWebtoon = webtoonRepository.save(new Webtoon(requestDto));
        return new WebtoonResponseDto(savedWebtoon);
    }

    public List<WebtoonResponseDto> findAllWebtoon() {
        List<Webtoon> webtoons = webtoonRepository.findAll();

        return webtoons.stream()
            .map(WebtoonResponseDto::new)
            .collect(Collectors.toList());
    }


    public WebtoonResponseDto readWebtoon(Long webtoonid) {
        Webtoon webtoon = findWebtoon(webtoonid);
        return new WebtoonResponseDto(webtoon);
    }

    @Transactional
    public WebtoonResponseDto updateWebtoon(User user, Long webtoonid,
        WebtoonRequestDto requestDto) {
        Webtoon webtoon = findWebtoon(webtoonid);
        checkUser(webtoonid, user.getId());

        webtoon.update(requestDto);
        return new WebtoonResponseDto(webtoon);
    }

    @Transactional
    public WebtoonResponseDto deleteWebtoon(User user, Long webtoonid) {
        Webtoon webtoon = findWebtoon(webtoonid);
        checkUser(webtoonid, user.getId());

        webtoonRepository.delete(webtoon);
        return new WebtoonResponseDto(webtoon);
    }

    private Webtoon findWebtoon(Long webtoonid) {
        return webtoonRepository.findById(webtoonid).orElseThrow(
            () -> new NoSuchElementException("웹툰이 존재하지 않습니다."));
    }

    private void checkUser(Long webtoonid, Long Userid) {
        if (!webtoonid.equals(Userid)) {
            throw new AccessDeniedException("웹툰 작성자가 아닙니다");
        }
    }
    private User checkExistUser(User user) {
        return userRepository.findById(user.getId()).orElseThrow(
            () -> new NoSuchElementException("유저가 존재하지 않습니다."));
    }

    private WebtoonLikes checkExistWebtoonLikes(Long webtoonLikesId) {
        return webtoonLikesRepository.findById(webtoonLikesId).orElseThrow(
            () -> new NoSuchElementException("해당 웹툰 좋아요가 존재하지 않습니다.")
        );
    }

    @Transactional
    public WebtoonLikesResponseDto createWebtoonLikes(User user, Long webtoonId) {
        User savedUser = checkExistUser(user);
        Webtoon savedWebtoon = findWebtoon(webtoonId);
        if (webtoonLikesRepository.existsByUserAndWebtoon(savedUser, savedWebtoon)) {
            throw new DataIntegrityViolationException("이미 웹툰에 좋아요를 했습니다.");
        }
        WebtoonLikes savedWebtoonLikes = webtoonLikesRepository.save(
            new WebtoonLikes(savedUser, savedWebtoon));
        return new WebtoonLikesResponseDto(savedWebtoonLikes);
    }

    @Transactional
    public WebtoonLikesResponseDto deleteWebtoonLikes(User user, Long webtoonId) {
        User savedUser = checkExistUser(user);
        Webtoon savedWebtoon = findWebtoon(webtoonId);
        WebtoonLikes savedWebtoonLikes = checkExistWebtoonLikes(webtoonId);
        if (!webtoonLikesRepository.existsByUserAndWebtoon(savedUser, savedWebtoon)) {
            throw new NoSuchElementException("해당 댓글 좋아요가 존재하지 않습니다.");
        }
        webtoonLikesRepository.delete(new WebtoonLikes(savedUser, savedWebtoon));

        return new WebtoonLikesResponseDto(savedWebtoonLikes);
    }
}
