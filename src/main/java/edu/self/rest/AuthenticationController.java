package edu.self.rest;

import edu.self.model.Credential;
import edu.self.repositories.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping
    public Map<String, String> register(@RequestBody Credential credential) {
        if (credentialRepository.existsById(credential.getUsername())) {
            throw new IllegalArgumentException("Such user already exists");
        }
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        credentialRepository.save(credential);
        return buildAuthResponse(credential);
    }

    @PostMapping
    public Map<String, String> login(@RequestBody Credential credential) {
        boolean loginSuccessful = credentialRepository.findById(credential.getUsername())
                .filter(actual -> passwordEncoder.matches(credential.getPassword(), actual.getPassword()))
                .isPresent();
        if (!loginSuccessful) {
            throw new IllegalArgumentException("Login failed");
        }
        return buildAuthResponse(credential);
    }

    private Map<String, String> buildAuthResponse(Credential credential) {
        return singletonMap(
                "Authorization",
                "Basic " + encodeBase64String((credential.getUsername() + ":" + credential.getPassword()).getBytes())
        );
    }
}
