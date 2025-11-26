package com.smarthome.smart_home_iot.repository;

import com.smarthome.smart_home_iot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 이름 찾기
    Optional<User> findByUsername(String username);
    // username이 DB에 존재하는지 여부만 반환 (회원가입 중복 체크용)
    boolean existsByUsername(String username);
}