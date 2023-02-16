package org.bankly.authenticationservice.security.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bankly.authenticationservice.handler.CustomAuthenticationFailureHandler;
import org.bankly.authenticationservice.security.models.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private CustomAuthenticationFailureHandler failureHandler;
    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, CustomAuthenticationFailureHandler failureHandler){
        this.authenticationManager = authenticationManager;
        this.failureHandler=failureHandler;
        super.setAuthenticationFailureHandler(failureHandler);
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
            try {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(email,password);
                return authenticationManager.authenticate(authenticationToken);
            }catch (BadCredentialsException e){
                throw new BadCredentialsException(e.getMessage());
            }catch (DisabledException e){
                throw new DisabledException(e.getMessage());
            }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successAuthentication");
        MyUser user = (MyUser) authResult.getPrincipal();

        // Set the signing algorithm for JWT
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretKey = "monSecret926600".getBytes();
        Key signingKey = new SecretKeySpec(secretKey, signatureAlgorithm.getJcaName());

        // Build JWT
        String jwtAccessToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .claim("roles", user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList()))
                .claim("user_id", user.getId())
                .claim("user_name", user.getName())
                .signWith(signatureAlgorithm, signingKey)
                .compact();

        response.setHeader("Authorization", jwtAccessToken);

        String jwtRefreshToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(signatureAlgorithm, signingKey)
                .compact();

        Map<String, String> idToken = new HashMap<>();
        idToken.put("accessToken", jwtAccessToken);
        idToken.put("refreshToken", jwtRefreshToken);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), idToken);
    }
}
