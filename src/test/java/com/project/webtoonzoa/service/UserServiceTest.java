package com.project.webtoonzoa.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.webtoonzoa.dto.user.SignUpRequestDto;
import com.project.webtoonzoa.dto.user.UserInfoRequestDto;
import com.project.webtoonzoa.dto.user.UserInfoResponseDto;
import com.project.webtoonzoa.dto.user.UserPasswordRequestDto;
import com.project.webtoonzoa.dto.user.UserResponseDto;
import com.project.webtoonzoa.entity.Enum.UserRoleEnum;
import com.project.webtoonzoa.entity.RefreshToken;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.global.exception.EmailExistenceException;
import com.project.webtoonzoa.global.exception.IsNotAdminUser;
import com.project.webtoonzoa.global.exception.PasswordNotConfirmException;
import com.project.webtoonzoa.global.exception.PasswordNotEqualException;
import com.project.webtoonzoa.repository.RefreshTokenRepository;
import com.project.webtoonzoa.repository.UserRecentPasswordRepository;
import com.project.webtoonzoa.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    ImageService imageService;

    @Mock
    UserRecentPasswordRepository userRecentPasswordRepository;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Mock
    HttpServletResponse httpServletResponse;

    @InjectMocks
    UserService userService;

    @Nested
    @DisplayName("회원가입")
    class createUser {

        User user;
        SignUpRequestDto signUpRequestDto;
        MultipartFile multipartFile;

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
        void 회원가입_성공() throws IOException {
            //given
            given(userRepository.save(any(User.class))).willReturn(user);
            //when
            Long id = userService.createUser(signUpRequestDto,multipartFile);
            //then
            assertEquals(user.getId(), id, "id가 같지 않습니다.");

        }

        @Test
        @DisplayName("회원가입 실패")
        void 회원가입_실패() {
            //given
            given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
            //when + then
            assertThrows(EmailExistenceException.class, () -> {
                userService.createUser(signUpRequestDto,multipartFile);
            });
        }


    }

    @Nested
    @DisplayName("회원 전체 조회")
    class getUsers {

        User adminUser;

        User user1;
        User user2;
        List<User> users;
        @BeforeEach
        public void setUp() {
            adminUser = new User();
            ReflectionTestUtils.setField(adminUser,"role",UserRoleEnum.ADMIN);
            user1 = new User();
            ReflectionTestUtils.setField(user1,"role",UserRoleEnum.USER);
            user2 = new User();
            users = new ArrayList<>();
            users.add(user1);
            users.add(user2);
        }

        @Test
        @DisplayName("회원 전체 조회 성공")
        public void 회원_전체조회_성공() throws Exception {
            //given
            given(userRepository.findAll()).willReturn(users);
            //when
            List<UserResponseDto> responseDtoList = userService.getUsers(adminUser);
            //then
            assertEquals(users.size(), responseDtoList.size());
        }

        @Test
        @DisplayName("회원 전체 조회 실패")
        public void 회원_전체조회_실패() throws Exception {
            //when + then
            assertThrows(IsNotAdminUser.class, () -> {
                userService.getUsers(user1);
            });
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
                //then
                then(userRecentPasswordRepository).should()
                    .findAllByUserIdOrderByCreatedAtDesc(user.getId());
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

    @Nested
    @DisplayName("로그아웃")
    class logout {

        @Test
        @DisplayName("로그아웃 성공")
        public void 로그아웃() throws Exception {
            //given
            User user = new User();
            RefreshToken refreshToken = new RefreshToken();
            given(refreshTokenRepository.findByUserId(user.getId())).willReturn(refreshToken);
            //when
            userService.logoutUser(httpServletResponse, user);
            //then
            verify(refreshTokenRepository, times(1)).delete(refreshToken);
        }
    }
}
