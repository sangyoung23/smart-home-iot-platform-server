package com.smarthome.smart_home_iot.dto.user;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class UserDto {
    private String username;

    private String password;
}
