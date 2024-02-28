package com.project.webtoonzoa.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webtoonzoa.dto.user.LoginRequestDto;
import com.project.webtoonzoa.entity.Enum.UserRoleEnum;
import com.project.webtoonzoa.entity.RefreshToken;
import com.project.webtoonzoa.global.response.CommonResponse;
import com.project.webtoonzoa.global.util.JwtUtil;
import com.project.webtoonzoa.global.util.UserDetailsImpl;
import com.project.webtoonzoa.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(),
                LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                    requestDto.getEmail(),
                    requestDto.getPassword(),
                    null
                )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        UserDetailsImpl user = (UserDetailsImpl) authResult.getPrincipal();
        String username = user.getUsername();
        UserRoleEnum role = user.getUser().getRole();

        String accessToken = jwtUtil.createAccessToken(username, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);

        if (validateRefreshToken(response, user)) {
            return;
        }

        String refreshToken = jwtUtil.createRefreshToken(username, role).substring(7);

        RefreshToken refreshTokenObj = new RefreshToken(user.getUser(), refreshToken);
        refreshTokenRepository.save(refreshTokenObj);

        // JSON으로 변환
        String jsonResponse = new ObjectMapper().writeValueAsString(CommonResponse.<Void>builder()
            .message("로그인에 성공하였습니다.")
            .status(HttpStatus.OK.value())
            .build()
        );

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    private boolean validateRefreshToken(HttpServletResponse response, UserDetailsImpl user)
        throws IOException {
        if (refreshTokenRepository.findByUserId(user.getUser().getId()) != null) {
            String jsonResponse = new ObjectMapper().writeValueAsString(
                CommonResponse.<Void>builder()
                    .message("이미 로그인이 되어있어 refresh token이 존재하는 상태입니다. Access token을 사용하여 진행해주세요!")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build()
            );

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
            return true;
        }
        return false;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {
        log.error("로그인 실패");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(CommonResponse.<Void>builder()
            .message("이메일이나 비밀번호가 틀렸습니다.")
            .status(HttpStatus.UNAUTHORIZED.value())
            .build()
        );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
