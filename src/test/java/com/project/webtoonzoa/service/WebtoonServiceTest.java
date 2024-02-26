package com.project.webtoonzoa.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.project.webtoonzoa.dto.WebtoonRequestDto;
import com.project.webtoonzoa.dto.WebtoonResponseDto;
import com.project.webtoonzoa.entity.Enum.Category;
import com.project.webtoonzoa.entity.Enum.Day;
import com.project.webtoonzoa.entity.Enum.UserRoleEnum;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.entity.Webtoon;
import com.project.webtoonzoa.repository.WebtoonRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class WebtoonServiceTest {
    @Mock
    WebtoonRepository webtoonRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    WebtoonService webtoonService;

    User user, user2;
    Long userId1 = 1L;
    Long userId2 = 2L;

    User admin;
    Long adminId = 3L;

    @BeforeEach
    void setUp() {
        user = new User();
        ReflectionTestUtils.setField(user, "id", userId1);
        ReflectionTestUtils.setField(user, "email", "test1@gmail.com");
        ReflectionTestUtils.setField(user, "nickname", "test");
        ReflectionTestUtils.setField(user, "password",
            passwordEncoder.encode("test1"));
        ReflectionTestUtils.setField(user, "role", UserRoleEnum.USER);

        user2 = new User();
        ReflectionTestUtils.setField(user, "id", userId2);
        ReflectionTestUtils.setField(user, "email", "test2@gmail.com");
        ReflectionTestUtils.setField(user, "nickname", "test2");
        ReflectionTestUtils.setField(user, "password",
            passwordEncoder.encode("test2"));
        ReflectionTestUtils.setField(user, "role", UserRoleEnum.USER);

        admin = new User();
        ReflectionTestUtils.setField(user, "id", adminId);
        ReflectionTestUtils.setField(user, "email", "admin1@gmail.com");
        ReflectionTestUtils.setField(user, "nickname", "admin");
        ReflectionTestUtils.setField(user, "password",
            passwordEncoder.encode("admin1"));
        ReflectionTestUtils.setField(user, "role", UserRoleEnum.ADMIN);
    }

    @Nested
    @DisplayName("웹툰 생성")
    class createWebtoon{
        WebtoonRequestDto requestDto;
        Webtoon webtoon;
        Long webtoonId = 1L;
        @BeforeEach
        void setup(){
            requestDto = new WebtoonRequestDto();
            ReflectionTestUtils.setField(requestDto, "title", "title");
            ReflectionTestUtils.setField(requestDto, "description", "description");
            ReflectionTestUtils.setField(requestDto, "category", Category.COMEDY);
            ReflectionTestUtils.setField(requestDto, "author", "test");
            ReflectionTestUtils.setField(requestDto, "day", Day.FRI);

            webtoon = new Webtoon(requestDto);
            ReflectionTestUtils.setField(webtoon, "id", webtoonId);
        }

        @Test
        @DisplayName("웹툰 생성 성공")
        void successCreate(){
            // given
            given(webtoonRepository.save(any(Webtoon.class))).willReturn(webtoon);

            // when
            WebtoonResponseDto savedWebtoon = webtoonService.createWebtoon(user, requestDto);

            // then
            assertEquals(webtoon.getId(), webtoonId, "ID가 다릅니다");
            assertEquals(requestDto.getTitle(), savedWebtoon.getTitle(), "title 이 다릅니다");
            assertEquals(requestDto.getDescription(), savedWebtoon.getDescription(), "description 이 다릅니다");
            assertEquals(requestDto.getCategory(), savedWebtoon.getCategory(), "category 가 다릅니다");
            assertEquals(requestDto.getAuthor(), savedWebtoon.getAuthor(), "author 가 다릅니다");
            assertEquals(requestDto.getDay(), savedWebtoon.getDay(), "day 가 다릅니다");
        }
    }

    @Nested
    @DisplayName("웹툰 전체 조회")
    class findAllWebtoon{
        Webtoon webtoon;
        List<Webtoon> webtoonList = new ArrayList<>();
        List<WebtoonResponseDto> webtoons;
        @BeforeEach
        void setup(){
            Long webtoonId = 1L;
            webtoon = new Webtoon();
            ReflectionTestUtils.setField(webtoon, "id", webtoonId);
            ReflectionTestUtils.setField(webtoon, "title", "title");
            ReflectionTestUtils.setField(webtoon, "description", "description");
            ReflectionTestUtils.setField(webtoon, "category", Category.COMEDY);
            ReflectionTestUtils.setField(webtoon, "author", "test");
            ReflectionTestUtils.setField(webtoon, "day", Day.FRI);
            webtoonList.add(webtoon);
        }

        @Test
        @DisplayName("웹툰 전체 조회 성공")
        void successFindAll(){
            // given
            given(webtoonRepository.findAll()).willReturn(webtoonList);

            // when
            webtoons = webtoonService.findAllWebtoon();

            // then
            assertFalse(webtoons.isEmpty(), "웹툰 목록이 비어있습니다");

            assertEquals(webtoon.getTitle(), webtoons.get(0).getTitle(), "title 이 다릅니다");
            assertEquals(webtoon.getDescription(), webtoons.get(0).getDescription(), "description 이 다릅니다");
            assertEquals(webtoon.getCategory(), webtoons.get(0).getCategory(), "category 가 다릅니다");
            assertEquals(webtoon.getAuthor(), webtoons.get(0).getAuthor(), "author 가 다릅니다");
            assertEquals(webtoon.getDay(), webtoons.get(0).getDay(), "day 가 다릅니다");
        }
    }

    @Nested
    @DisplayName("웹툰 상세 조회")
    class detailedWebtoon{
        Webtoon webtoon;
        Long webtoonId = 1L;
        @BeforeEach
        void setup(){
            webtoon = new Webtoon();
            ReflectionTestUtils.setField(webtoon, "id", webtoonId);
            ReflectionTestUtils.setField(webtoon, "title", "title");
            ReflectionTestUtils.setField(webtoon, "description", "description");
            ReflectionTestUtils.setField(webtoon, "category", Category.COMEDY);
            ReflectionTestUtils.setField(webtoon, "author", "test");
            ReflectionTestUtils.setField(webtoon, "day", Day.FRI);
        }

        @Test
        @DisplayName("웹툰 상세 조회 성공")
        void successDetail(){
            // given
            given(webtoonRepository.findById(webtoonId)).willReturn(Optional.of(webtoon));

            // when
            WebtoonResponseDto responseDto = webtoonService.readWebtoon(webtoonId);

            // then
            assertEquals(webtoon.getTitle(), responseDto.getTitle(), "title 이 다릅니다");
            assertEquals(webtoon.getDescription(), responseDto.getDescription(), "description 이 다릅니다");
            assertEquals(webtoon.getCategory(), responseDto.getCategory(), "category 가 다릅니다");
            assertEquals(webtoon.getAuthor(), responseDto.getAuthor(), "author 가 다릅니다");
            assertEquals(webtoon.getDay(), responseDto.getDay(), "day 가 다릅니다");
        }
    }

    @Nested
    @DisplayName("웹툰 수정")
    class updateWebtoon{
        Webtoon webtoon;
        Long webtoonId = 1L;
        WebtoonRequestDto requestDto;
        @BeforeEach
        void setup(){
            webtoon = new Webtoon();
            ReflectionTestUtils.setField(webtoon, "id", webtoonId);
            ReflectionTestUtils.setField(webtoon, "title", "title");
            ReflectionTestUtils.setField(webtoon, "description", "description");
            ReflectionTestUtils.setField(webtoon, "category", Category.COMEDY);
            ReflectionTestUtils.setField(webtoon, "author", "test");
            ReflectionTestUtils.setField(webtoon, "day", Day.FRI);

            requestDto = new WebtoonRequestDto();
            ReflectionTestUtils.setField(requestDto, "title", "수정된 title");
            ReflectionTestUtils.setField(requestDto, "description", "수정된 description");
            ReflectionTestUtils.setField(requestDto, "category", Category.SF);
            ReflectionTestUtils.setField(requestDto, "author", "test2");
            ReflectionTestUtils.setField(requestDto, "day", Day.MON);
        }

        @Test
        @DisplayName("웹툰 수정 성공")
        void successUpdate(){
            // given
            Long webtoonId = 1L;
            given(webtoonRepository.findById(webtoonId)).willReturn(Optional.of(webtoon));

            // when
            WebtoonResponseDto responseDto = webtoonService.updateWebtoon(user, webtoonId, requestDto);

            // then
            assertEquals(requestDto.getTitle(), responseDto.getTitle(), "title 이 다릅니다");
            assertEquals(requestDto.getDescription(), responseDto.getDescription(), "description 이 다릅니다");
            assertEquals(requestDto.getCategory(), responseDto.getCategory(), "category 가 다릅니다");
            assertEquals(requestDto.getAuthor(), responseDto.getAuthor(), "author 가 다릅니다");
            assertEquals(requestDto.getDay(), responseDto.getDay(), "day 가 다릅니다");
        }
    }

    @Nested
    @DisplayName("웹툰 삭제")
    class deleteWebtoon{
        Webtoon webtoon;
        @BeforeEach
        void setup(){
            webtoon = new Webtoon();
            ReflectionTestUtils.setField(webtoon, "id", 1L);
            ReflectionTestUtils.setField(webtoon, "title", "title");
            ReflectionTestUtils.setField(webtoon, "description", "description");
            ReflectionTestUtils.setField(webtoon, "category", Category.COMEDY);
            ReflectionTestUtils.setField(webtoon, "author", "author");
            ReflectionTestUtils.setField(webtoon, "day", Day.FRI);
        }

        @Test
        @DisplayName("웹툰 삭제 성공")
        void successDelete(){
            // given
            Long webtoonId = 1L;
            given(webtoonRepository.findById(webtoonId)).willReturn(Optional.of(webtoon));

            // when
            WebtoonResponseDto responseDto = webtoonService.deleteWebtoon(user, webtoonId);

            // then
            assertEquals(responseDto.getTitle(), webtoon.getTitle(), "title 이 다릅니다");
            assertEquals(responseDto.getDescription(), webtoon.getDescription(), "description 이 다릅니다");
            assertEquals(responseDto.getCategory(), webtoon.getCategory(), "category 가 다릅니다");
            assertEquals(responseDto.getAuthor(), webtoon.getAuthor(), "author 가 다릅니다");
            assertEquals(responseDto.getDay(), webtoon.getDay(), "day 가 다릅니다");
        }
    }
}