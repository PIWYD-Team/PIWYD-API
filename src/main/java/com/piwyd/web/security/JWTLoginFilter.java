package com.piwyd.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
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

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);

    private UserRepository userRepository;

    private UserEntity userEntity;

    public JWTLoginFilter(String url, AuthenticationManager authManager, UserRepository userRepository) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
        this.userRepository = userRepository;
        this.userEntity = null;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException, IOException, ServletException {

        Credential credential =new ObjectMapper()
                .readValue(req.getInputStream(), Credential.class);

        this.userEntity = userRepository.findByEmail(credential.getUsername());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);

        boolean result = false;

        if(userEntity != null) {
            result = passwordEncoder.matches(credential.getPassword(), userEntity.getPassword());
        }

        if (!result) {
            logger.info("email or password incorrect");
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "L'adresse email ou le mot de passe est incorrect.");
            return null;
        }

        return new UsernamePasswordAuthenticationToken(
                credential.getUsername(),
                credential.getPassword(),
                Collections.emptyList());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication auth) throws IOException, ServletException {

    }
}