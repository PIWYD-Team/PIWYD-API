package com.piwyd.user;

import org.springframework.stereotype.Component;

@Component
public class UserAdapter {

    public UserDto userToDto(UserDao userDao) {
        return UserDto.builder()
                .id(userDao.getId())
                .email(userDao.getEmail())
                .password(userDao.getPassword())
                .build();
    }

    public UserDao userToDao(UserDto userDto) {
        return UserDao.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
    }
}
