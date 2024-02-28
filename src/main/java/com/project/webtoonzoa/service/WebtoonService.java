package com.project.webtoonzoa.service;

import com.project.webtoonzoa.dto.webtoon.WebtoonLikesResponseDto;
import com.project.webtoonzoa.dto.webtoon.WebtoonRequestDto;
import com.project.webtoonzoa.dto.webtoon.WebtoonResponseDto;
import com.project.webtoonzoa.entity.Enum.UserRoleEnum;
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
    public Long createWebtoon(User user, WebtoonRequestDto requestDto) {
        checkRole(user);
        Webtoon savedWebtoon = webtoonRepository.save(new Webtoon(requestDto));
        return savedWebtoon.getId();
    }

    public List<WebtoonResponseDto> findAllWebtoon() {
        List<Webtoon> webtoons = webtoonRepository.findAllByDeletedAtIsNull();

        return webtoons.stream()
            .map(WebtoonResponseDto::new)
            .collect(Collectors.toList());
    }


    public WebtoonResponseDto readWebtoon(Long webtoonId) {
        Webtoon webtoon = findWebtoon(webtoonId);
        return new WebtoonResponseDto(webtoon);
    }

    @Transactional
    public WebtoonResponseDto updateWebtoon(User user, Long webtoonId,
        WebtoonRequestDto requestDto) {
        checkRole(user);
        Webtoon webtoon = findWebtoon(webtoonId);

        webtoon.update(requestDto);
        return new WebtoonResponseDto(webtoon);
    }

    @Transactional
    public Long deleteWebtoon(User user, Long webtoonId) {
        checkRole(user);
        Webtoon webtoon = findWebtoon(webtoonId);
        webtoon.softDelete();
        return webtoon.getId();
    }

    private void checkRole(User user) {
        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }

    private Webtoon findWebtoon(Long webtoonId) {
        return webtoonRepository.findById(webtoonId).orElseThrow(
            () -> new NoSuchElementException("웹툰이 존재하지 않습니다."));
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

        savedWebtoon.increaseLikes();
        WebtoonLikes savedWebtoonLikes = webtoonLikesRepository.save(
            new WebtoonLikes(savedUser, savedWebtoon));
        checkLikeCount(savedWebtoon);

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

        savedWebtoon.decreaseLikes();
        webtoonLikesRepository.delete(new WebtoonLikes(savedUser, savedWebtoon));
        checkLikeCount(savedWebtoon);

        return new WebtoonLikesResponseDto(savedWebtoonLikes);
    }

    public List<WebtoonResponseDto> findTop5PopularWebtoons() {
        List<Webtoon> top5List = webtoonRepository.findTop5ByOrderByLikesDesc();
        return top5List.stream()
            .map(WebtoonResponseDto::new)
            .collect(Collectors.toList());
    }

    private void checkLikeCount(Webtoon webtoon) {
        Long dbLikes = webtoonRepository.countLikesByWebtoonId(webtoon.getId());
        if (!webtoon.getLikes().equals(dbLikes)) {
            throw new IllegalStateException("DB와 웹툰의 좋아요 수가 다릅니다.");
        }
    }

}
