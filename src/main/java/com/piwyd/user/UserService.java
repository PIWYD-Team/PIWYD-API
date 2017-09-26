package com.piwyd.user;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    @Transactional(readOnly = true)
    List<UserDto> getAllUsers();

    UserDto addUser(UserDto userDto);

    UserDto getUserById(int id);

    void removeUser(int id);

    UserDto modifyUser(int id, UserDto userDto);
}
