package edu.self.rest;

import edu.self.dto.ChangePasswordRequest;
import edu.self.model.Credential;
import edu.self.repositories.CredentialRepository;
import edu.self.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.web.cors.CorsConfiguration.ALL;

@CrossOrigin(ALL)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping
    public Map<String, String> register(@RequestBody Credential credential) {
        if (credentialRepository.existsById(credential.getUsername())) {
            throw new DataIntegrityViolationException("Such user already exists");
        }
        // Building response before password is encoded
        Map<String, String> response = buildAuthResponse(credential);
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        credentialRepository.save(credential);
        return response;
    }

    @PostMapping
    public Map<String, String> login(@RequestBody Credential credential) {
        if (!validatePassword(credential.getUsername(), credential.getPassword())) {
            throw new IllegalArgumentException("Login failed");
        }
        return buildAuthResponse(credential);
    }

    @PostMapping("/{userId}/password")
    public Map<String, String> changePassword(@PathVariable("userId") String userId, @RequestBody ChangePasswordRequest changePassword) {
        if (!validatePassword(userId, changePassword.getOldPassword())) {
            throw new IllegalArgumentException("Old password in incorrect");
        }

        Credential credential = credentialRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));
        credential.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        credentialRepository.save(credential);
        return buildAuthResponse(userId, changePassword.getNewPassword());
    }

    private boolean validatePassword(String userId, String password) {
        return credentialRepository.findById(userId)
                .filter(actual -> passwordEncoder.matches(password, actual.getPassword()))
                .isPresent();
    }


    private Map<String, String> buildAuthResponse(Credential credential) {
        return buildAuthResponse(credential.getUsername(), credential.getPassword());
    }

    private Map<String, String> buildAuthResponse(String username, String password) {
        return singletonMap(
                "Authorization",
                "Basic " + encodeBase64String((username + ":" + password).getBytes())
        );
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleConflict() {
        // No-op
    }
}
