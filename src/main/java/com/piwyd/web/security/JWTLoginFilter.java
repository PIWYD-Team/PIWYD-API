package com.piwyd.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piwyd.password.PasswordRulesService;
import com.piwyd.user.UserAdapter;
import com.piwyd.user.UserEntity;
import com.piwyd.user.UserRepository;
import com.piwyd.web.security.domain.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);
	private final boolean withNewPassword;

    private UserRepository userRepository;
    private TokenAuthenticationService tokenAuthenticationService;
    private UserAdapter userAdapter;
    private PasswordRulesService passwordRulesService;

    private UserEntity userEntity;

    public JWTLoginFilter(String url, AuthenticationManager authManager, UserRepository userRepository,
						  TokenAuthenticationService tokenAuthenticationService, UserAdapter userAdapter, PasswordRulesService passwordRulesService, boolean withNewPassword) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
        this.userRepository = userRepository;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userAdapter = userAdapter;
        this.userEntity = null;
        this.withNewPassword = withNewPassword;
        this.passwordRulesService = passwordRulesService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException, IOException, ServletException {

		boolean result = false;
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
        Credential credential = new ObjectMapper().readValue(req.getInputStream(), Credential.class);

        userEntity = userRepository.findByEmail(credential.getUsername());

        if (userEntity != null) {
            result = passwordEncoder.matches(credential.getPassword(), userEntity.getPassword());
        }

        if (!result) {
            logger.info("email or password incorrect");
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "L'adresse email ou le mot de passe est incorrect.");
            return null;
        }

		if (withNewPassword) {
			result = passwordRulesService.isPasswordValid(credential.getNewPassword(), userEntity);

			if (result) {
				// Save the new password and update the user
				userEntity.setPassword(passwordEncoder.encode(credential.getNewPassword()));
				userEntity.setLastTimePasswordUpdated(new Date());
				userEntity = userRepository.save(userEntity);
			}
		} else {
			result = passwordRulesService.isPasswordValid(credential.getPassword(), userEntity);
		}

		if (!result) {
			logger.info("password doesn't match with password rules");
			res.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Le mot de passe ne vérifie plus les règles exigées");
		}

        return new UsernamePasswordAuthenticationToken(
                credential.getUsername(),
                credential.getPassword(),
                Collections.emptyList());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication auth) throws IOException, ServletException {
		// Generate token when successful login
		tokenAuthenticationService.addAuthentication(res, userAdapter.userToDto(userEntity), AuthState.FIRST_STEP_AUTH);
    }
}