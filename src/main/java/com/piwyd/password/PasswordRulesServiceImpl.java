package com.piwyd.password;

import com.piwyd.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PasswordRulesServiceImpl implements PasswordRulesService {

	@Autowired
	private PasswordRulesRepository passwordRulesRepository;

	@Autowired
	private PasswordRulesAdapter passwordRulesAdapter;

	@Override
	@Transactional(readOnly = true)
	public PasswordRulesDto getPasswordRules() {
		PasswordRulesEntity entity = passwordRulesRepository.findAll().get(0);

		return passwordRulesAdapter.entityToDto(entity);
	}

	@Override
	@Transactional
	public PasswordRulesDto updatePasswordRules(final PasswordRulesDto passwordRulesDto) {
		PasswordRulesEntity entity = passwordRulesAdapter.dtoToEntity(passwordRulesDto);
		entity = passwordRulesRepository.save(entity);

		return passwordRulesAdapter.entityToDto(entity);
	}

	@Override
	public boolean isPasswordValid(final String password, final UserEntity userEntity) {
		PasswordRulesDto passwordRules = getPasswordRules();
		String passwordRulesRegex = getPasswordRulesRegex(passwordRules);

		Pattern pattern = Pattern.compile(passwordRulesRegex);
		Matcher matcher = pattern.matcher(password);

		LocalDate today = LocalDate.now();
		LocalDate expiredDay = LocalDate.ofEpochDay(userEntity.getLastTimePasswordUpdated().getTime()).plusDays(passwordRules.getExpirationTime());

		return matcher.find() && expiredDay.isAfter(today);
	}

	private String getPasswordRulesRegex(final PasswordRulesDto passwordRulesDto) {
		String regex = "^";

		if (passwordRulesDto.getRequireLowercase()) {
			regex += "(?=.*[a-z])";
		}

		if (passwordRulesDto.getRequireUppercase()) {
			regex += "(?=.*[A-Z])";
		}

		if (passwordRulesDto.getRequireNumbers()) {
			regex += "(?=.*[0-9])";
		}

		if (passwordRulesDto.getRequireSpecialCharacters()) {
			regex += "(?=.*[@#$%^&+=])";
		}

		regex += ".{" + passwordRulesDto.getMinLength() + ",}$";

		return regex;
	}
}
