package com.piwyd.web.rs;

import com.piwyd.password.PasswordRulesDto;
import com.piwyd.password.PasswordRulesService;
import com.piwyd.password.PasswordRulesValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/passwordRules")
public class PasswordRulesController {

	@Autowired
	private PasswordRulesService passwordRulesService;

	@RequestMapping
	@ResponseStatus(HttpStatus.OK)
	public PasswordRulesDto getPasswordRules() {
		return passwordRulesService.getPasswordRules();
	}

	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public PasswordRulesDto updatePasswordRules(@RequestBody @Valid PasswordRulesDto passwordRulesDto, BindingResult bindingResult) throws PasswordRulesValidationException {
		if (bindingResult.hasErrors())
			throw new PasswordRulesValidationException();

		return passwordRulesService.updatePasswordRules(passwordRulesDto);
	}
}
