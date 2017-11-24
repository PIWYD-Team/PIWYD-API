package com.piwyd.password;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRulesDto {

	private Long id;

	private Boolean requireLowercase;

	private Boolean requireUppercase;

	private Boolean requireNumbers;

	private Boolean requireSpecialCharacters;

	private Integer minLength;

	private Integer expirationTime;
}
