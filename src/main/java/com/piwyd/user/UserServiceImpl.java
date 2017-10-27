package com.piwyd.user;

import com.piwyd.user.face.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAdapter userAdapter;

    @Autowired
    private FaceService faceService;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> userAdapter.userToDto(u))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto addUser(UserDto userDto) throws EmailAddressAlreadyExistsException {
        if (checkUserAlreadyExist(userDto))
            throw new EmailAddressAlreadyExistsException(userDto.getEmail());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
        UserEntity newUser = userAdapter.userToDao(userDto);
        newUser.setId(UUID.randomUUID().getMostSignificantBits());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

        String privateKey = generatePrivateKey();
        newUser.setPrivateKey(privateKey);

        newUser = userRepository.save(newUser);

        ResponseEntity responseEntity = faceService.registerNewFace(userDto.getFile(), newUser.getId());

        return userAdapter.userToDto(newUser);
    }

    @Override
    public UserDto getUserById(Long id) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findById(id);

        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        return userAdapter.userToDto(userEntity);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userByEmail = userRepository.findByEmail(email);
        return userAdapter.userToDto(userByEmail);
    }

    @Override
    public void removeUser(Long idUser) {
        UserEntity userToDelete = userRepository.findById(idUser);
        userRepository.delete(userToDelete);
    }

    @Override
    public UserDto modifyUser(Long id, UserDto userDto) {
        UserEntity userToUpdate = userRepository.findById(id);
        if (userDto.getEmail() != null &&
                userDto.getEmail().length() > 1)
            userToUpdate.setEmail(userDto.getEmail());
        return userAdapter.userToDto(userRepository.save(userToUpdate));
    }

    private boolean checkUserAlreadyExist(UserDto userDto) {
        UserEntity userByEmail = userRepository.findByEmail(userDto.getEmail());
        if (userByEmail != null)
            return true;
        return false;
    }

    private String generatePrivateKey() {
        Long aLong = new Random().nextLong();
        return Long.toString(aLong, 16);
    }
}
