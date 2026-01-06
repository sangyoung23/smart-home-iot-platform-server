package com.smarthome.smart_home_iot.security;

import com.smarthome.smart_home_iot.repository.jpa.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 1️⃣ Authorization 헤더 확인
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);

            try {
                // 2️⃣ 토큰에서 username 추출
                username = jwtUtil.getUsernameFromToken(token);

                // 3️⃣ 토큰 유효성 검증
                if (!jwtUtil.validateToken(token)) {
                    username = null;
                }
            } catch (Exception e) {
                log.warn("JWT 처리 중 예외 발생: {}", e.getMessage());
                username = null;
            }
        }

        // 4️⃣ SecurityContext에 인증 객체 없으면 설정
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            String finalUsername = username;
            userRepository.findByUsername(username).ifPresent(user -> {

                // 🔥 권한 설정 (필수)
                List<SimpleGrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_USER"));

                List.of(new SimpleGrantedAuthority(user.getRole()));

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                authorities
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);

                log.debug("JWT 인증 성공 - username: {}", finalUsername);
            });
        }

        filterChain.doFilter(request, response);
    }
}
