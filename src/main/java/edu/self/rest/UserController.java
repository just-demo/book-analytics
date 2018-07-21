package edu.self.rest;

import edu.self.model.Book;
import edu.self.model.User;
import edu.self.repositories.CredentialRepository;
import edu.self.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
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
    public void addUserData(@PathVariable("userId") String userId, @RequestBody User userPatch) {
        User user = userRepository.findById(userId).orElseGet(() -> new User(userId));
        add(user, userPatch);
        userRepository.save(user);
    }

    @PatchMapping("/{userId}/remove")
    public void removeUserData(@PathVariable("userId") String userId, @RequestBody User userRemove) {
        User user = userRepository.findById(userId).orElseGet(() -> new User(userId));
        remove(user, userRemove);
        userRepository.save(user);
    }

    @GetMapping("/{userId}/books/{bookId}")
    public Optional<Book> getBook(@PathVariable("userId") String userId, @PathVariable("bookId") String bookId) {
        // TODO: fetch book from a separate collection
        return getUser(userId).getBooks().stream().filter(book -> book.getName().equals(bookId)).findAny();
    }

    private User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
    }

    private void add(User user, User toAdd) {
        user.getSelected().addAll(toAdd.getSelected());
        user.getHidden().addAll(toAdd.getHidden());
        user.getBooks().addAll(toAdd.getBooks());
    }

    private void remove(User user, User toRemove) {
        user.getSelected().removeAll(toRemove.getSelected());
        user.getHidden().removeAll(toRemove.getHidden());
        // TODO: fix this...
        Set<String> booksToRemove = toRemove.getBooks().stream()
                .map(Book::getName)
                .collect(toSet());
        user.getBooks().removeIf(book -> booksToRemove.contains(book.getName()));
    }
}
