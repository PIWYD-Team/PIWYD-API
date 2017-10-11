package com.piwyd.user;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    @Transactional(readOnly = true)
    List<UserDto> getAllUsers();

    UserDto addUser(UserDto userDto) throws EmailAddressAlreadyExistsException;

    UserDto getUserById(Long id) throws UserNotFoundException;

    UserDto getUserByEmail(String email);

    void removeUser(Long id);

    UserDto modifyUser(Long id, UserDto userDto);
}
