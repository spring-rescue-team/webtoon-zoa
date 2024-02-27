package com.project.webtoonzoa.service;

import com.project.webtoonzoa.dto.user.SignUpRequestDto;
import com.project.webtoonzoa.dto.user.UserBannedResponseDto;
import com.project.webtoonzoa.dto.user.UserInfoRequestDto;
import com.project.webtoonzoa.dto.user.UserInfoResponseDto;
import com.project.webtoonzoa.dto.user.UserPasswordRequestDto;
import com.project.webtoonzoa.entity.Enum.UserRoleEnum;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.global.exception.IsNotAdminUser;
import com.project.webtoonzoa.global.exception.PasswordNotConfirmException;
import com.project.webtoonzoa.global.exception.PasswordNotEqualException;
import com.project.webtoonzoa.global.exception.UserNotExistence;
import com.project.webtoonzoa.global.util.JwtUtil;
import com.project.webtoonzoa.repository.UserRecentPasswordRepository;
import com.project.webtoonzoa.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRecentPasswordRepository userRecentPasswordRepository;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public Long createUser(SignUpRequestDto userRequestDto) {
        String email = userRequestDto.getEmail();
        String password = passwordEncoder.encode(userRequestDto.getPassword());
        String nickname = userRequestDto.getNickname();
        UserRoleEnum role = UserRoleEnum.USER;
        role = validateAdminToken(userRequestDto, role);
        User user = User.builder()
            .email(email)
            .nickname(nickname)
            .password(password)
            .role(role)
            .build();
        User saveUser = userRepository.save(user);
        return saveUser.getId();
    }

    private UserRoleEnum validateAdminToken(SignUpRequestDto userRequestDto, UserRoleEnum role) {
        if (userRequestDto.getAdminToken() != null) {
            if (userRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                role = UserRoleEnum.ADMIN;
            }
        }
        return role;
    }

    public void logoutUser(HttpServletResponse response) {
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, null);
    }

    @Transactional
    public UserInfoResponseDto updateUser(User user, UserInfoRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        User savedUser = getUserById(user.getId());
        savedUser.updatedNickname(nickname);
        return new UserInfoResponseDto(user.getId(), nickname);
    }

    @Transactional
    public void updatePassword(UserPasswordRequestDto requestDto, User user) {
        User savedUser = getUserById(user.getId());
        validatePasswordBySavedUser(requestDto, savedUser);
        validatePasswordByRecentPasswordTop3();
        validateConfirmPassword(requestDto);
        String updatedPassword = passwordEncoder.encode(requestDto.getChangePassword());
        savedUser.updatePassword(updatedPassword);
    }

    private void validatePasswordByRecentPasswordTop3() {
    }

    private void validatePasswordBySavedUser(UserPasswordRequestDto requestDto, User savedUser) {
        if (!passwordEncoder.matches(requestDto.getPassword(), savedUser.getPassword())) {
            throw new PasswordNotEqualException("회원의 비밀번호와 일치하지 않습니다.");
        }
    }

    private void validateConfirmPassword(UserPasswordRequestDto requestDto) {
        if (!requestDto.getChangePassword().equals(requestDto.getChangePasswordConfirm())) {
            throw new PasswordNotConfirmException("바꾸려는 비밀번호와 동일하지 않습니다.");
        }
    }

    @Transactional
    public UserBannedResponseDto banUser(Long userId, User user) {
        validateAdmin(user);
        User savedUser = getUserById(userId);
        savedUser.updateBanned();
        Long savedUserId = savedUser.getId();
        boolean banned = savedUser.isBanned();
        return new UserBannedResponseDto(savedUserId, banned);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotExistence("존재하지 않는 회원입니다."));
    }

    private static void validateAdmin(User user) {
        if(!user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new IsNotAdminUser("관리자가 아니기에 회원을 밴을 할 수 없습니다.");
        }
    }
}
