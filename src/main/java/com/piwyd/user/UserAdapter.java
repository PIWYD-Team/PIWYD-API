package com.piwyd.user;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserAdapter {

    public UserDto userToDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId().toString())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
				.privateKey(userEntity.getPrivateKey())
				.lastTimePasswordUpdated(userEntity.getLastTimePasswordUpdated())
                .build();
    }

    public UserEntity userToDao(UserDto userDto) {
        return UserEntity.builder()
                .id(Long.parseLong(userDto.getId()))
                .email(userDto.getEmail())
                .password(userDto.getPassword())
				.privateKey(userDto.getPrivateKey())
				.lastTimePasswordUpdated(userDto.getLastTimePasswordUpdated())
                .build();
    }

    public UserDto mapToDto(Map<String, Object> map) {
        UserDto userDto = new UserDto();
		userDto.setId((String) map.get("id"));
		userDto.setEmail((String) map.get("email"));
		userDto.setPassword((String) map.get("password"));
		userDto.setPrivateKey((String) map.get("privateKey"));
		// userDto.setLastTimePasswordUpdated((String) map.get("lastTimePasswordUpdated"));

        return userDto;
    }
}
