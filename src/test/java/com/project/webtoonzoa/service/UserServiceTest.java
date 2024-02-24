package com.project.webtoonzoa.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.project.webtoonzoa.dto.SignUpRequestDto;
import com.project.webtoonzoa.dto.UserInfoRequestDto;
import com.project.webtoonzoa.dto.UserInfoResponseDto;
import com.project.webtoonzoa.entity.Enum.UserRoleEnum;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.repository.UserRepository;
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
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Nested
    @DisplayName("회원가입")
    class createUser {

        User user;
        SignUpRequestDto signUpRequestDto;

        @BeforeEach
        void setUp() {
            signUpRequestDto = new SignUpRequestDto();
            user = new User();
            ReflectionTestUtils.setField(signUpRequestDto, "email", "song1234@gmail.com");
            ReflectionTestUtils.setField(signUpRequestDto, "nickname", "song");
            ReflectionTestUtils.setField(signUpRequestDto, "password", "song1234");
            ReflectionTestUtils.setField(signUpRequestDto, "adminToken", "");
            ReflectionTestUtils.setField(user, "id", 1L);
            ReflectionTestUtils.setField(user, "email", signUpRequestDto.getEmail());
            ReflectionTestUtils.setField(user, "nickname", signUpRequestDto.getNickname());
            ReflectionTestUtils.setField(user, "password",
                passwordEncoder.encode(signUpRequestDto.getPassword()));
            ReflectionTestUtils.setField(user, "role", UserRoleEnum.USER);
        }

        @Test
        @DisplayName("회원가입 성공")
        void 회원가입_성공() {
            //given
            given(userRepository.save(any(User.class))).willReturn(user);
            //when
            Long id = userService.createUser(signUpRequestDto);
            //then
            assertEquals(user.getId(), id, "id가 같지 않습니다.");
        }
    }

    @Nested
    @DisplayName("회원정보 수정")
    class updateUser {

        User user;
        UserInfoRequestDto userInfoRequestDto;

        @BeforeEach
        void setUp() {
            user = new User();
            userInfoRequestDto = new UserInfoRequestDto();
            String nickname = "song seon ho";
            ReflectionTestUtils.setField(userInfoRequestDto, "nickname", nickname);

            String email = "song1234@gmail.com";
            String nicknameUser = "song";
            String password = "song1234";
            UserRoleEnum role = UserRoleEnum.USER;

            ReflectionTestUtils.setField(user, "id", 1L);
            ReflectionTestUtils.setField(user, "email", email);
            ReflectionTestUtils.setField(user, "nickname", nicknameUser);
            ReflectionTestUtils.setField(user, "password", password);
            ReflectionTestUtils.setField(user, "role", role);
        }

        @Test
        void 회원정보수정_성공() {
            //given
            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            //when
            UserInfoResponseDto userInfoResponseDto = userService.updateUser(user,
                userInfoRequestDto);
            //then
            assertEquals(userInfoRequestDto.getNickname(), userInfoResponseDto.getNickname(),
                "nickname이 같지가 않습니다.");
        }
    }
}
