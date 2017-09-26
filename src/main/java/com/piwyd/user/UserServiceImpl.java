package com.piwyd.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAdapter userAdapter;

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> listUsers = userRepository.findAll()
                .stream()
                .map(u -> userAdapter.userToDto(u))
                .collect(Collectors.toList());
        return listUsers;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        UserDao newUser = userAdapter.userToDao(userDto);
        newUser = userRepository.save(newUser);
        return userAdapter.userToDto(newUser);
    }

    @Override
    public UserDto getUserById(int id) {
        UserDao userDao = userRepository.findByIdUser(id);
        UserDto userDto = userAdapter.userToDto(userDao);
        return userDto;
    }

    @Override
    public void removeUser(int idUser) {
        UserDao userToDelete = userRepository.findByIdUser(idUser);
        userRepository.delete(userToDelete);
    }

    @Override
    public UserDto modifyUser(int id, UserDto userDto) {
        UserDao userToUpdate = userRepository.findByIdUser(id);
        if (userDto.getNameUser() != null &&
                userDto.getNameUser().length() > 1)
            userToUpdate.setNameUser(userDto.getNameUser());
        return userAdapter.userToDto(userRepository.save(userToUpdate));
    }
}
