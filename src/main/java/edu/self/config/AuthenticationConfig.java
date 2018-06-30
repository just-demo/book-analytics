package edu.self.config;

import edu.self.repositories.CredentialRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService); //TODO: isn't defining userDetailsService bean enought?
    }

    @Bean //TODO: why can't use default
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return username -> {
            System.out.println(username);
            return credentialRepository.findById(username)
                    .map(credential -> User.builder()
                            .username(credential.getUsername())
                            .password(credential.getPassword())
                            .authorities("USER")
//                            .passwordEncoder(passwordEncoder::encode)
                            .build())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        };
    }


    public static void main(String[] args) {
        //true Authorization: Basic dGVzdDpwd2Q=
        //false Authorization: Basic dGVzdDpwd2Qy
        String up = "test:pwd2";
        System.out.println(Base64.encodeBase64String(up.getBytes()));
    }
}
