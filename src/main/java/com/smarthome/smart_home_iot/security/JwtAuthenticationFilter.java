package com.smarthome.smart_home_iot.security;

import com.smarthome.smart_home_iot.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // JWT 생성/검 유틸리티
    private final JwtUtil jwtUtil;

    // DB에서 사용자 정보 조회
    private final UserRepository userRepository;

    /**
     * HTTP 요청이 올 때마다 실행되는 필터
     * JWT 토큰을 검사하고 인증 객체를 SecurityContext에 세팅
    */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {

        // HTTP 헤더에서 Authorization 값 가져오기
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // "Bearer "로 시작하면 JWT 토큰으로 간주
        if(header != null && header.startsWith("Bearer ")) {
            // "Bearer " 접두어 제거하고 실제 토큰 가져오기
            token = header.substring(7);

            // JWT 토큰 검증 (유효성 체크)
            if(jwtUtil.validateToken(token)) {
                // 토큰에서 username 추출
                username = jwtUtil.getUsernameFromToken(token);
            }
        }

        // SecurityContext에 인증 객체가 없고 username이 존재하면
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // DB에서 사용자 조회
            var user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        user, null, null
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);

    }
}
