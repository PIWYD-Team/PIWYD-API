package com.piwyd.user;

import org.springframework.stereotype.Component;

@Component
public class UserAdapter {

    public UserDto userToDto(UserDao userDao) {
        return UserDto.builder()
                .idUser(userDao.getIdUser())
                .nameUser(userDao.getNameUser())
                .build();
    }

    public UserDao userToDao(UserDto userDto) {
        return UserDao.builder()
                .idUser(userDto.getIdUser())
                .nameUser(userDto.getNameUser())
                .build();
    }
}
