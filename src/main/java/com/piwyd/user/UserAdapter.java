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
				.admin(userEntity.getAdmin())
                .build();
    }

    public UserEntity userToDao(UserDto userDto) {
    	Long id = userDto.getId() != null ? Long.parseLong(userDto.getId()) : null;

        return UserEntity.builder()
                .id(id)
                .email(userDto.getEmail())
                .password(userDto.getPassword())
				.privateKey(userDto.getPrivateKey())
				.lastTimePasswordUpdated(userDto.getLastTimePasswordUpdated())
				.admin(userDto.isAdmin())
                .build();
    }

    public UserDto mapToDto(Map<String, Object> map) {
        UserDto userDto = new UserDto();
		userDto.setId((String) map.get("id"));
		userDto.setEmail((String) map.get("email"));
		userDto.setPassword((String) map.get("password"));
		userDto.setPrivateKey((String) map.get("privateKey"));
		userDto.setAdmin((boolean) map.get("admin"));
		// userDto.setLastTimePasswordUpdated((String) map.get("lastTimePasswordUpdated"));

        return userDto;
    }
}
