package edu.self.rest;

import edu.self.dto.Ignorable;
import edu.self.dto.Translation;
import edu.self.model.Credential;
import edu.self.model.User;
import edu.self.repositories.CredentialRepository;
import edu.self.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @GetMapping
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public void createUser(@RequestBody Credential credential) {
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        credentialRepository.save(credential);
    }

    @GetMapping("/{userId}")
    public Optional<User> findUser(@PathVariable("userId") String userId) {
        return userRepository.findById(userId);
    }

    @GetMapping("/{userId}/translations")
    public Map<String, String> getTranslations(@PathVariable("userId") String userId) {
        // TODO: try to fetch this data as userRepository.findTranslationsByUserId
        return userRepository.findById(userId)
                .map(User::getTranslations)
                .orElse(emptyMap());
    }

    @PutMapping("/{userId}/translations")
    public Translation setTranslation(@PathVariable("userId") String userId, @RequestBody Translation translation) {
        User user = getUser(userId);
        user.getTranslations().put(translation.getText(), translation.getTranslation());
        userRepository.save(user);
        return translation;
    }

    @PutMapping("/ignorable")
    public void setIgnorable(@PathVariable("userId") String userId, @RequestBody Ignorable ignorable) {
        User user = getUser(userId);
        if (ignorable.isIgnored()) {
            user.getIgnored().add(ignorable.getText());
        } else {
            user.getIgnored().remove(ignorable.getText());
        }
        userRepository.save(user);
    }

    private User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
    }
}
