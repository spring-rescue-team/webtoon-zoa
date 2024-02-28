package com.project.webtoonzoa.service;

import com.project.webtoonzoa.dto.user.SignUpRequestDto;
import com.project.webtoonzoa.dto.user.UserBannedResponseDto;
import com.project.webtoonzoa.dto.user.UserInfoRequestDto;
import com.project.webtoonzoa.dto.user.UserInfoResponseDto;
import com.project.webtoonzoa.dto.user.UserPasswordRequestDto;
import com.project.webtoonzoa.dto.user.UserResponseDto;
import com.project.webtoonzoa.entity.Enum.UserRoleEnum;
import com.project.webtoonzoa.entity.RefreshToken;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.entity.UserRecentPassword;
import com.project.webtoonzoa.global.exception.EmailExistenceException;
import com.project.webtoonzoa.global.exception.IsNotAdminUser;
import com.project.webtoonzoa.global.exception.PasswordIsRecentPasswordException;
import com.project.webtoonzoa.global.exception.PasswordNotConfirmException;
import com.project.webtoonzoa.global.exception.PasswordNotEqualException;
import com.project.webtoonzoa.global.exception.UserNotExistence;
import com.project.webtoonzoa.global.util.JwtUtil;
import com.project.webtoonzoa.repository.RefreshTokenRepository;
import com.project.webtoonzoa.repository.UserRecentPasswordRepository;
import com.project.webtoonzoa.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRecentPasswordRepository userRecentPasswordRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ImageService imageService;

    @Value("${admin.token}")
    private String ADMIN_TOKEN;

    @Transactional
    public Long createUser(SignUpRequestDto userRequestDto, MultipartFile imageFile)
        throws IOException {
        String email = userRequestDto.getEmail();
        validateEmail(email);
        String password = passwordEncoder.encode(userRequestDto.getPassword());
        String nickname = userRequestDto.getNickname();
        UserRoleEnum role = UserRoleEnum.USER;
        role = validateAdminToken(userRequestDto, role);
        String imageUrl = imageService.createImageName(imageFile);
        User user = User.builder()
            .email(email)
            .nickname(nickname)
            .password(password)
            .imagePath(imageUrl)
            .role(role)
            .build();
        User saveUser = userRepository.save(user);
        saveRecentPassword(saveUser, password);
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

    @Transactional
    public void logoutUser(HttpServletResponse response, User user) {
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, null);
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId());
        refreshTokenRepository.delete(refreshToken);
    }

    public List<UserResponseDto> getUsers(User user) {
        validateAdmin(user);
        List<User> users = userRepository.findAll();
        return users.stream().map(UserResponseDto::new).toList();
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
        List<UserRecentPassword> recentPasswords = userRecentPasswordRepository.findAllByUserIdOrderByCreatedAtDesc(
            user.getId());

        validatePasswordByRecentPassword(requestDto.getChangePassword(), recentPasswords);

        validateConfirmPassword(requestDto);

        String updatedPassword = passwordEncoder.encode(requestDto.getChangePassword());

        saveRecentPassword(user, updatedPassword);
        savedUser.updatePassword(updatedPassword);

        removeRecentPasswordMoreThanThreeSize(recentPasswords);
    }

    private void removeRecentPasswordMoreThanThreeSize(List<UserRecentPassword> recentPasswords) {
        if (recentPasswords.size() >= 3) {
            UserRecentPassword userRecentPassword = recentPasswords.get(recentPasswords.size() - 1);
            userRecentPasswordRepository.delete(userRecentPassword);
        }
    }

    private void saveRecentPassword(User user, String password) {
        UserRecentPassword userRecentPassword = new UserRecentPassword();
        userRecentPassword.setUser(user);
        userRecentPassword.savePassword(password);
        userRecentPasswordRepository.save(userRecentPassword);
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

    private void validatePasswordByRecentPassword(String password,
        List<UserRecentPassword> userRecentPasswords) {
        for (UserRecentPassword userRecentPassword : userRecentPasswords) {
            if (passwordEncoder.matches(password, userRecentPassword.getPassword())) {
                throw new PasswordIsRecentPasswordException("최근에 사용한적이 있는 비밀번호입니다.");
            }
        }
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

    private void validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailExistenceException("중복된 이메일이 존재합니다!");
        }
        ;
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotExistence("존재하지 않는 회원입니다."));
    }

    private void validateAdmin(User user) {
        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IsNotAdminUser("관리자가 권한이 없습니다.");
        }
    }
}
