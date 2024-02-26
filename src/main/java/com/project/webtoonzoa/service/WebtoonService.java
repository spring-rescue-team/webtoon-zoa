package com.project.webtoonzoa.service;

import com.project.webtoonzoa.dto.webtoon.WebtoonRequestDto;
import com.project.webtoonzoa.dto.webtoon.WebtoonResponseDto;
import com.project.webtoonzoa.entity.Enum.UserRoleEnum;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.entity.Webtoon;
import com.project.webtoonzoa.repository.WebtoonRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;

    @Transactional
    public WebtoonResponseDto createWebtoon(User user, WebtoonRequestDto requestDto) {
        checkRole(user);
        Webtoon savedWebtoon = webtoonRepository.save(new Webtoon(requestDto));
        return new WebtoonResponseDto(savedWebtoon);
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
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }

    private Webtoon findWebtoon(Long webtoonId) {
        return webtoonRepository.findById(webtoonId).orElseThrow(
            () -> new NoSuchElementException("웹툰이 존재하지 않습니다."));
    }
}
