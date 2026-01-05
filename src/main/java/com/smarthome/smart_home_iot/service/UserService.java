package com.smarthome.smart_home_iot.service;

import com.smarthome.smart_home_iot.domain.user.User;
import com.smarthome.smart_home_iot.dto.user.AuthRequest;
import com.smarthome.smart_home_iot.dto.user.AuthResponse;
import com.smarthome.smart_home_iot.repository.jpa.UserRepository;
import com.smarthome.smart_home_iot.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // 회원가입
    public void register(User user) {
        log.info("회원가입 시도 - 사용자명={}", user.getUsername());

        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("회원가입 실패 - 이미 존재하는 사용자, 사용자명={}", user.getUsername());
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        log.info("회원가입 성공 - 사용자명={}", user.getUsername());
    }

    // 로그인
    public AuthResponse login(AuthRequest request) {
        log.info("로그인 시도 - 사용자명={}", request.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("로그인 실패 - 사용자 정보 없음, 사용자명={}", request.getUsername());
                    return new RuntimeException("사용자를 찾을 수 없습니다.");
                });

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        log.info("로그인 성공 - 사용자명={}", user.getUsername());

        return new AuthResponse(token);
    }
}
