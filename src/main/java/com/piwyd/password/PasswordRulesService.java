package com.piwyd.password;

import com.piwyd.user.UserEntity;

public interface PasswordRulesService {

	PasswordRulesDto getPasswordRules();

	PasswordRulesDto updatePasswordRules(PasswordRulesDto passwordRulesDto);

	boolean isPasswordValid(String password, UserEntity userEntity);
}
