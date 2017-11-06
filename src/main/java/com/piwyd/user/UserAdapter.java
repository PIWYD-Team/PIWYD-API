package com.piwyd.user;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserAdapter {

    public UserDto userToDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .build();
    }

    public UserEntity userToDao(UserDto userDto) {
        return UserEntity.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
    }

    public static UserEntity mapToDao(Map<String, Object> map) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(((Integer) map.get("id")).longValue());
        userEntity.setEmail((String) map.get("email"));
        userEntity.setPassword((String) map.get("password"));
        userEntity.setName((String) map.get("name"));

        return userEntity;
    }
}
