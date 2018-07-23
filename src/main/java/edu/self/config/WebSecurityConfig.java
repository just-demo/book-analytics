package edu.self.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.http.HttpMethod.OPTIONS;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .csrf().disable()
                .requestMatchers()
                .antMatchers("/auth/**", "/users/**")
                .and()
                .authorizeRequests()
                .antMatchers(OPTIONS).permitAll()
                .antMatchers("/auth").permitAll()
                .antMatchers("/auth/{userId}/**").access("#userId == principal.username")
                .antMatchers("/users/{userId}/**").access("#userId == principal.username")
                .anyRequest().denyAll()
        ;
    }
}