package com.piwyd.web.security;

import com.piwyd.user.UserEntity;
import com.piwyd.user.face.FaceService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	private static final Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);

	private UserEntity userEntity;

	private FaceService faceService;

	public JWTLoginFaceFilter(String url, AuthenticationManager authManager, FaceService faceService) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);

		this.faceService = faceService;
		this.userEntity = null;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
		userEntity = TokenAuthenticationService.getUserFromToken(httpServletRequest);
		String base64File = getFileData(httpServletRequest);

		if(userEntity == null) {
			httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Première étape de login non complétée");
			return null;
		}

		ResponseEntity<String> responseEntity = faceService.verifyUserFace(base64File, userEntity.getId());

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			String body = responseEntity.getBody();
			double confidence = getUserConfidence(body);

			if (0.6 < confidence) {
				return new UsernamePasswordAuthenticationToken(
						userEntity.getEmail(),
						"",
						Collections.emptyList());
			} else {
				logger.info("Kairos API call, confidence rate : ", confidence);
				httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Ta face n'a pas été reconnue, trou du cul ! (tu noteras la petite rime ^^");
			}
		} else {
			logger.warn("Kairos API call, response status : ", responseEntity.getStatusCode());
			httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Une erreur technique est survenue");
		}

		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
											FilterChain chain, Authentication auth) throws IOException, ServletException {
		// Generate token when successful login
		TokenAuthenticationService.addAuthentication(res, userEntity, AuthState.FULL_AUTH);
	}

	private String getFileData(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject(request.getInputStream());
		return jsonObject.getString("file");
	}

	private double getUserConfidence(String jsonResult) {
		JSONArray images = new JSONArray(jsonResult);
		JSONObject transaction = images.getJSONObject(0);

		return transaction.getDouble("confidence");
	}
}
