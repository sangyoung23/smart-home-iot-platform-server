package com.smarthome.smart_home_iot.service;

import com.smarthome.smart_home_iot.domain.User;
import com.smarthome.smart_home_iot.dto.AuthRequest;
import com.smarthome.smart_home_iot.dto.AuthResponse;
import com.smarthome.smart_home_iot.repository.jpa.UserRepository;
import com.smarthome.smart_home_iot.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // 회원가입
    public void register(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // 비밀번호 암호화
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    // 로그인
    public AuthResponse login(AuthRequest request) {
        // Spring Security AuthenticationManager로 인증
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token);
    }
}
