package com.piwyd.user;

import com.piwyd.user.face.FaceService;
import com.piwyd.user.face.KairosAPIException;
import com.piwyd.web.security.JWTLoginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    FaceService faceService;

    @Override
	@Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<UserDto> listUsers = userRepository.findAll()
                .stream()
                .map(u -> userAdapter.userToDto(u))
                .collect(Collectors.toList());
        return listUsers;
    }

    @Override
	@Transactional(rollbackFor = Exception.class)
    public UserDto addUser(UserDto userDto) throws EmailAddressAlreadyExistsException, KairosAPIException {
        if (checkUserAlreadyExist(userDto))
            throw new EmailAddressAlreadyExistsException(userDto.getEmail());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
        UserEntity newUser = userAdapter.userToDao(userDto);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

        newUser = userRepository.save(newUser);

		faceService.registerNewFace(userDto.getPicture(), newUser.getId());

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
}
