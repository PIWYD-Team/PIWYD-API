package com.piwyd.user;

import org.springframework.stereotype.Component;

@Component
public class UserAdapter {

    public UserDto userToDto(UserDao userDao) {
        return UserDto.builder()
                .id(userDao.getIdUser())
                .email(userDao.getEmailUser())
                .password(userDao.getPasswordUser())
                .build();
    }

    public UserDao userToDao(UserDto userDto) {
        return UserDao.builder()
                .idUser(userDto.getId())
                .emailUser(userDto.getEmail())
                .passwordUser(userDto.getPassword())
                .build();
    }
}
