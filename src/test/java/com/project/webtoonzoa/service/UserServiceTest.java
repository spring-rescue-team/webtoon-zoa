package com.project.webtoonzoa.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.project.webtoonzoa.dto.SignUpRequestDto;
import com.project.webtoonzoa.dto.UserInfoRequestDto;
import com.project.webtoonzoa.dto.UserInfoResponseDto;
import com.project.webtoonzoa.dto.UserPasswordRequestDto;
import com.project.webtoonzoa.entity.Enum.UserRoleEnum;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.global.exception.PasswordNotConfirmException;
import com.project.webtoonzoa.global.exception.PasswordNotEqualException;
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
            UserRoleEnum role = UserRoleEnum.USER;

            ReflectionTestUtils.setField(user, "id", 1L);
            ReflectionTestUtils.setField(user, "email", email);
            ReflectionTestUtils.setField(user, "nickname", nicknameUser);
            ReflectionTestUtils.setField(user, "role", role);
        }

        @Test
        @DisplayName("회원 닉네임 수정 성공")
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

        @Nested
        @DisplayName("회원 비밀번호 수정")
        class updateUserPassword {

            UserPasswordRequestDto userPasswordRequestDto;

            @BeforeEach
            void setUp2() {
                userPasswordRequestDto = new UserPasswordRequestDto();
                String password = "songS1234!!";
                String changePassword = "songS1234!!!";
                String changePasswordConfirm = "songS1234!!!";
                ReflectionTestUtils.setField(userPasswordRequestDto, "password", password);
                ReflectionTestUtils.setField(userPasswordRequestDto, "changePassword",
                    changePassword);
                ReflectionTestUtils.setField(userPasswordRequestDto, "changePasswordConfirm",
                    changePasswordConfirm);

                given(userRepository.findById(1L)).willReturn(Optional.of(user));
            }

            @Test
            @DisplayName("회원 비밀번호 변경 성공")
            void 회원_비밀번호_변경_성공() {
                //given
                given(passwordEncoder.matches(userPasswordRequestDto.getPassword(),
                    user.getPassword())).willReturn(true);
                //when
                userService.updatePassword(userPasswordRequestDto, user);
            }

            @Test
            @DisplayName("회원 비밀번호 변경 실패( 회원 비밀번호와 request 비밀번호 불일치 )")
            void 회원_비밀번호_변경_실패_1() {
                //given
                given(passwordEncoder.matches(userPasswordRequestDto.getPassword(),
                    user.getPassword())).willReturn(false);
                //when + then
                assertThrows(PasswordNotEqualException.class, () -> {
                    userService.updatePassword(userPasswordRequestDto, user);
                });
            }

            @Test
            @DisplayName("회원 비밀번호 변경 실패( confirm 비밀번호 불일치 )")
            void 회원_비밀번호_변경_실패_2() {
                //given
                String changePassword = "songS1234!!";
                String changePasswordConfirm = "songS1234!!!";
                ReflectionTestUtils.setField(userPasswordRequestDto, "changePassword",
                    changePassword);
                ReflectionTestUtils.setField(userPasswordRequestDto, "changePasswordConfirm",
                    changePasswordConfirm);
                given(passwordEncoder.matches(userPasswordRequestDto.getPassword(),
                    user.getPassword())).willReturn(true);
                //when + then
                assertThrows(PasswordNotConfirmException.class, () -> {
                    userService.updatePassword(userPasswordRequestDto, user);
                });
            }
        }

    }
}
