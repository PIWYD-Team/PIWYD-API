package com.piwyd.web.security;

import com.piwyd.user.UserAdapter;
import com.piwyd.user.UserDto;
import com.piwyd.user.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.emptyList;

@Service
public class TokenAuthenticationService {

    private static final long EXPIRATIONTIME = 86_400_000; // one day in millisecond
    private static final String SECRET = "ThomasVincentRobin";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";
    private static final String USER_TOKEN_KEY = "user";
    private static final String AUTH_STATUS_TOKEN_KEY = "authenticationStatus";

    @Autowired
    private HttpSession httpSession;

    @Autowired
	private UserAdapter userAdapter;

    private static byte[] getSecretKey() {
        byte[] secret;

        try {
            secret = SECRET.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            secret = SECRET.getBytes();
        }

        return secret;
    }

    /**
     * Add the token in the header response
     * @param res
     * @param userDto
     */
    public void addAuthentication(HttpServletResponse res, UserDto userDto, AuthState authenticationStatus) {
        userDto.setPassword("");

        String JWT = Jwts.builder()
                .claim(USER_TOKEN_KEY, userDto)
				.claim(AUTH_STATUS_TOKEN_KEY, authenticationStatus.ordinal())
                .setSubject(userDto.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, getSecretKey())
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
    }

    /**
     * Get the user from a request
     * @param request
     * @return
     */
    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token
            Jws<Claims> claimsJws = Jwts.parser()
					.require(AUTH_STATUS_TOKEN_KEY, AuthState.FULL_AUTH.ordinal())
                    .setSigningKey(getSecretKey())
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""));

            String subject = claimsJws
					.getBody()
                    .getSubject();

			UserDto userDto = getUser(claimsJws);
			httpSession.setAttribute("userId", Long.parseLong(userDto.getId()));
			httpSession.setAttribute("userPrivateKey", userDto.getPrivateKey());

            return subject != null ?
                    new UsernamePasswordAuthenticationToken(subject, null, emptyList()) :
                    null;
        }
        return null;
    }

    public UserDto getUserFromToken(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);

		if (token == null) {
			throw new AuthenticationCredentialsNotFoundException("Première étape de login non complétée");
		}

		Jws<Claims> claimsJws = Jwts.parser()
				.require(AUTH_STATUS_TOKEN_KEY, AuthState.FIRST_STEP_AUTH.ordinal())
				.setSigningKey(getSecretKey())
				.parseClaimsJws(token.replace(TOKEN_PREFIX, ""));

		return getUser(claimsJws);
	}

	private UserDto getUser(final Jws<Claims> claimsJws) {
		// parse the token
		Map map = (Map) claimsJws
				.getBody()
				.get(USER_TOKEN_KEY);

		return userAdapter.mapToDto(map);
	}
}
