package org.bankly.authenticationservice.security;

import lombok.RequiredArgsConstructor;
import org.bankly.authenticationservice.entities.Users;
import org.bankly.authenticationservice.handler.CustomAuthenticationFailureHandler;
import org.bankly.authenticationservice.security.filters.JwtAuthenticationFilter;
import org.bankly.authenticationservice.security.filters.JwtAuthorizationFilter;
import org.bankly.authenticationservice.security.models.MyUser;
import org.bankly.authenticationservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.util.ArrayList;
import java.util.Collection;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    @Autowired
    CustomAuthenticationFailureHandler failureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            try {
                Users userByEmail= userService.loadUserByEmail(username);
                    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

                    MyUser user = new MyUser(userByEmail.getEmail(), userByEmail.getPassword(),authorities,userByEmail.getId(),userByEmail.getName());
                    return user;
            }catch (IllegalStateException e){
                throw new UsernameNotFoundException(e.getMessage());
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().antMatchers("/refreshToken/**").permitAll();
        http.authorizeHttpRequests().antMatchers("/login").permitAll();
        http.authorizeHttpRequests().antMatchers("/signUp").permitAll();
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean(),failureHandler));
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
