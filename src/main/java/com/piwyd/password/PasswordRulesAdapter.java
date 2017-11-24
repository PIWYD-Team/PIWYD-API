package com.piwyd.password;

import org.springframework.stereotype.Component;

@Component
public class PasswordRulesAdapter {

	public PasswordRulesDto entityToDto(PasswordRulesEntity entity) {
		return PasswordRulesDto.builder()
				.id(entity.getId())
				.requireLowercase(entity.getRequireLowercase())
				.requireUppercase(entity.getRequireUppercase())
				.requireNumbers(entity.getRequireNumbers())
				.requireSpecialCharacters(entity.getRequireSpecialCharacters())
				.minLength(entity.getMinLength())
				.expirationTime(entity.getExpirationTime())
				.build();
	}

	public PasswordRulesEntity dtoToEntity(PasswordRulesDto dto) {
		return PasswordRulesEntity.builder()
				.id(dto.getId())
				.requireLowercase(dto.getRequireLowercase())
				.requireUppercase(dto.getRequireUppercase())
				.requireNumbers(dto.getRequireNumbers())
				.requireSpecialCharacters(dto.getRequireSpecialCharacters())
				.minLength(dto.getMinLength())
				.expirationTime(dto.getExpirationTime())
				.build();
	}
}
