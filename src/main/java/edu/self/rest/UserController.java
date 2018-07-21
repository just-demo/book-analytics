package edu.self.rest;

import edu.self.model.User;
import edu.self.repositories.CredentialRepository;
import edu.self.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.springframework.web.cors.CorsConfiguration.ALL;

@CrossOrigin(ALL)
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

    @GetMapping("/{userId}")
    public Optional<User> findUser(@PathVariable("userId") String userId) {
        return userRepository.findById(userId);
    }

    @PatchMapping("/{userId}")
    public User patchUser(@PathVariable("userId") String userId, @RequestBody User userPatch) {
        // TODO: configure mapKeyDotReplacement or do not use user inputs as keys
        User user = userRepository.findById(userId).orElseGet(() -> new User(userId));
        merge(userPatch, user);
        return userRepository.save(user);
    }

    private void merge(User source, User target) {
        source.getSelected().forEach((word, translations) ->
                target.getSelected().computeIfAbsent(word, ignored -> new HashSet<>()).addAll(translations));
        target.getHidden().addAll(source.getHidden());
        target.getBooks().putAll(source.getBooks());
    }

    @PutMapping("/{userId}/selected/{word}/{translation}")
    public void putSelected(
            @PathVariable("userId") String userId,
            @PathVariable("word") String word,
            @PathVariable("translation") String translation
    ) {
        User user = getUser(userId);
        user.getSelected().computeIfAbsent(word, ignored -> new HashSet<>()).add(translation);
        userRepository.save(user);
    }

    @DeleteMapping("/{userId}/selected/{word}/{translation}")
    public void removeSelected(
            @PathVariable("userId") String userId,
            @PathVariable("word") String word,
            @PathVariable("translation") String translation
    ) {
        User user = getUser(userId);
        ofNullable(user.getSelected().get(word))
                .ifPresent(translations -> translations.remove(translation));
        userRepository.save(user);
    }

    @PutMapping("/{userId}/hidden/{word}")
    public void putHidden(
            @PathVariable("userId") String userId,
            @PathVariable("word") String word
    ) {
        User user = getUser(userId);
        user.getHidden().add(word);
        userRepository.save(user);
    }

    @DeleteMapping("/{userId}/hidden/{word}")
    public void removeHidden(
            @PathVariable("userId") String userId,
            @PathVariable("word") String word
    ) {
        User user = getUser(userId);
        user.getHidden().remove(word);
        userRepository.save(user);
    }

    private User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
    }
}
