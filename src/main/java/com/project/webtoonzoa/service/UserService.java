package com.project.webtoonzoa.service;

import com.project.webtoonzoa.dto.UserRequestDto;
import com.project.webtoonzoa.entity.Enum.UserRoleEnum;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.global.util.JwtUtil;
import com.project.webtoonzoa.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public Long createUser(UserRequestDto userRequestDto) {
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

    private UserRoleEnum validateAdminToken(UserRequestDto userRequestDto, UserRoleEnum role) {
        if(userRequestDto.getAdminToken() != null){
            if (userRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                role = UserRoleEnum.ADMIN;
            }
        }
        return role;
    }

    public void logoutUser(HttpServletResponse response) {
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, null);
    }
}
