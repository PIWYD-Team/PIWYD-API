package com.piwyd.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piwyd.user.UserDto;
import com.piwyd.user.face.FaceService;
import com.piwyd.user.face.KairosAPIException;
import com.piwyd.web.security.domain.LoginFaceParams;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTLoginFaceFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger logger = LoggerFactory.getLogger(JWTLoginFaceFilter.class);

	private UserDto userDto;

	private FaceService faceService;

	private TokenAuthenticationService tokenAuthenticationService;

	public JWTLoginFaceFilter(String url, AuthenticationManager authManager, FaceService faceService, TokenAuthenticationService tokenAuthenticationService) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);

		this.faceService = faceService;
		this.tokenAuthenticationService = tokenAuthenticationService;
		this.userDto = null;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
		userDto = tokenAuthenticationService.getUserFromToken(httpServletRequest);
		final LoginFaceParams params = new ObjectMapper().readValue(httpServletRequest.getInputStream(), LoginFaceParams.class);

		if(userDto == null) {
			httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Première étape de login non complétée");
			return null;
		}

		try {
			String body = faceService.verifyUserFace(params.getPicture(), Long.parseLong(userDto.getId()));
			double confidence = getUserConfidence(body);

			if (0.6 < confidence) {
				return new UsernamePasswordAuthenticationToken(
						userDto.getEmail(),
						"",
						Collections.emptyList());
			} else {
				logger.info("Kairos API call, confidence rate : " + confidence);
				httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Ta face n'a pas été reconnue, trou du cul ! (tu noteras la petite rime ^^");
			}
		} catch (KairosAPIException e) {
			httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}

		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
											FilterChain chain, Authentication auth) throws IOException, ServletException {
		// Generate token when successful login
		tokenAuthenticationService.addAuthentication(res, userDto, AuthState.FULL_AUTH);
	}

	private double getUserConfidence(String jsonResult) {
		return new JSONObject(jsonResult)
				.getJSONArray("images")
				.getJSONObject(0)
				.getJSONObject("transaction")
				.getDouble("confidence");
	}
}
