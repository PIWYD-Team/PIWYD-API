package com.piwyd.user;

import com.piwyd.user.face.KairosAPIException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto addUser(UserDto userDto) throws EmailAddressAlreadyExistsException, KairosAPIException;

    UserDto getUserById(Long id) throws UserNotFoundException;

    UserDto getUserByEmail(String email);

    void removeUser(Long id);

    UserDto modifyUser(Long id, UserDto userDto);
}
