package com.piwyd.web.rs;

import com.piwyd.user.*;
import com.piwyd.user.face.KairosAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers() {
        List<UserDto> allUsers = userService.getAllUsers();
        return allUsers;
    }

    @GetMapping("/{userId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable("userId") Long id) throws UserNotFoundException {
        UserDto userById = userService.getUserById(id);
        return userById;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid  UserDto userDto, BindingResult bindingResult) throws EmailAddressAlreadyExistsException, KairosAPIException {
        if (bindingResult.hasErrors())
            throw new UserValidationException();

        UserDto newUser = userService.addUser(userDto);
        return newUser;
    }

    @DeleteMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") Long id) throws UserNotFoundException {
        userService.removeUser(id);
    }

    @PutMapping("/{userId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto updateUser(@PathVariable("userId") Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.modifyUser(id, userDto);
        return updatedUser;
    }
}
