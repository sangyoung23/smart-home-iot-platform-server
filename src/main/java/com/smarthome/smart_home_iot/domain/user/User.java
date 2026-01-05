package com.smarthome.smart_home_iot.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_user_info")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;
}
