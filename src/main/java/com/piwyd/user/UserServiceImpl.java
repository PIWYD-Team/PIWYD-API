package com.piwyd.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAdapter userAdapter;

    @Autowired


    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> listUsers = userRepository.findAll()
                .stream()
                .map(u -> userAdapter.userToDto(u))
                .collect(Collectors.toList());
        return listUsers;
    }

    @Override
    public UserDto addUser(UserDto userDto) throws EmailAddressAlreadyExistsException {

        if (checkUserAlreadyExist(userDto))
            throw new EmailAddressAlreadyExistsException(userDto.getEmail());

        UUID userId = UUID.randomUUID();
        userDto.setId(userId.getMostSignificantBits());

        UserDao newUser = userAdapter.userToDao(userDto);
        newUser = userRepository.save(newUser);
        return userAdapter.userToDto(newUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        UserDao userDao = userRepository.findById(id);
        UserDto userDto = userAdapter.userToDto(userDao);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserDao userByEmail = userRepository.findByEmail(email);
        return userAdapter.userToDto(userByEmail);
    }

    @Override
    public void removeUser(Long idUser) {
        UserDao userToDelete = userRepository.findById(idUser);
        userRepository.delete(userToDelete);
    }

    @Override
    public UserDto modifyUser(Long id, UserDto userDto) {
        UserDao userToUpdate = userRepository.findById(id);
        if (userDto.getEmail() != null &&
                userDto.getEmail().length() > 1)
            userToUpdate.setEmail(userDto.getEmail());
        return userAdapter.userToDto(userRepository.save(userToUpdate));
    }

    private boolean checkUserAlreadyExist(UserDto userDto) {
        UserDao userByEmail = userRepository.findByEmail(userDto.getEmail());
        if (userByEmail != null)
            return true;
        return false;
    }
}
