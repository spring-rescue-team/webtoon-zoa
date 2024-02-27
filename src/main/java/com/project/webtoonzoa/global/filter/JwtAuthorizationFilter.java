package com.project.webtoonzoa.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webtoonzoa.entity.RefreshToken;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.global.response.CommonResponse;
import com.project.webtoonzoa.global.util.JwtUtil;
import com.project.webtoonzoa.global.util.UserDetailsImpl;
import com.project.webtoonzoa.global.util.UserDetailsServiceImpl;
import com.project.webtoonzoa.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
        FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (StringUtils.hasText(tokenValue)) {
            int tokenStatus =  jwtUtil.validateToken(tokenValue);
            // 0 이면 정상적인 토큰
            // 1 이면 기간만 만료된 토큰
            // 2 이면 비장상적인 토큰
            if (tokenStatus == 1) {
                try{
                    Claims info = jwtUtil.getUserInfoFromExpiredToken(tokenValue);
                    UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(info.getSubject());
                    User user = userDetails.getUser();
                    RefreshToken refreshToken = refreshTokenRepository.findByUserId(userDetails.getUser().getId());
                    if (refreshToken != null && jwtUtil.validateToken(refreshToken.getRefreshToken()) == 0){
                        String newAccessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole());
                        res.addHeader(JwtUtil.AUTHORIZATION_HEADER,newAccessToken);
                    }else{
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                        String jsonResponse = new ObjectMapper().writeValueAsString(
                            CommonResponse.<Void>builder()
                                .message("Access Token과 Refresh Token이 모두 만료되었습니다.").build());

                        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        res.setCharacterEncoding("UTF-8");
                        res.getWriter().write(jsonResponse);
                        refreshTokenRepository.delete(refreshToken);
                        return;
                    }
                }catch(Exception e){
                    logger.error(e.getMessage());
                    logger.error("JWT 인가 오류");
                    return;
                }
            }
            else if(tokenStatus == 2){
                String jsonResponse = new ObjectMapper().writeValueAsString(CommonResponse.<Void>builder()
                    .message("검증 되지 않은 토큰이거나 토큰이 존재 하지 않습니다.")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build()
                );
                res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                res.setCharacterEncoding("UTF-8");
                res.setStatus(HttpStatus.BAD_REQUEST.value());
                res.getWriter().write(jsonResponse);
                log.error("Token Error");
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error("jwt 인가 부분 에러 : token status RED ERR");
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, null);
    }
}
