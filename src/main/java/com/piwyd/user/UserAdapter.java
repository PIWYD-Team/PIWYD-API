package com.piwyd.user;

import org.springframework.stereotype.Component;

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
}
