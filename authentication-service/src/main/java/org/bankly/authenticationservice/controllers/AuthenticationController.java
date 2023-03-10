package org.bankly.authenticationservice.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bankly.authenticationservice.entities.Users;
import org.bankly.authenticationservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationController {
    private UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public ResponseEntity signUpOwner(@Validated @RequestBody Users user){
        try {
            Users user1 = userService.signUp(user);
            if(user1 != null){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(user1);
            }else {
                return ResponseEntity.badRequest().body("Donnés non valid");
            }
        }catch (IllegalStateException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }

    }



    @GetMapping("/refreshToken")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String authToken = request.getHeader("Authorization");
        System.out.println(authToken);
        if(authToken !=null && authToken.startsWith("Bearer ")){
            try {
                String refreshToken = authToken.substring(7);
                Algorithm algorithm = Algorithm.HMAC256("monSecret926600");
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                String email = decodedJWT.getSubject();
                Users user = userService.loadUserByEmail(email);
                String jwtAccessToken = JWT.create().withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
                        .withClaim("user_id",user.getId())
                        .withClaim("user_name",user.getName())
                        .sign(algorithm);
                Map<String,String> idToken = new HashMap<>();
                idToken.put("access-token",jwtAccessToken);
                idToken.put("refresh-token",refreshToken);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(),idToken);
            }
            catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }else{
            return ResponseEntity.status(400).body("Refresh token est obligatoire!!");
        }
        return null;
    }
}
