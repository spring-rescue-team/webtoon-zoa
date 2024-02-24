package com.project.webtoonzoa.service;

import com.project.webtoonzoa.dto.SignUpRequestDto;
import com.project.webtoonzoa.dto.UserInfoRequestDto;
import com.project.webtoonzoa.dto.UserInfoResponseDto;
import com.project.webtoonzoa.entity.Enum.UserRoleEnum;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.global.util.JwtUtil;
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
        User savedUser = getUser(user);
        savedUser.updatedNickname(nickname);
        return new UserInfoResponseDto(user.getId(), nickname);
    }

    private User getUser(User user) {
        return userRepository.findById(user.getId())
            .orElseThrow(() -> new NullPointerException("존재하지 않는 회원입니다."));
    }
}
