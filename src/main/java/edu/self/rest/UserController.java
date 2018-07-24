package edu.self.rest;

import edu.self.model.Book;
import edu.self.model.User;
import edu.self.model.UserBook;
import edu.self.repositories.BookRepository;
import edu.self.repositories.CredentialRepository;
import edu.self.repositories.UserBookRepository;
import edu.self.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

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

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBookRepository userBookRepository;

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
        user.getSelected().addAll(userPatch.getSelected());
        user.getHidden().addAll(userPatch.getHidden());
        userPatch.getBooks().forEach(userBook -> {
            Book book = bookRepository.save(new Book(userBook.getContent()));
            userBook.setId(book.getId());
        });
        user.getBooks().addAll(userPatch.getBooks());
        userRepository.save(user);
    }

    @PatchMapping("/{userId}/remove")
    public void removeUserData(@PathVariable("userId") String userId, @RequestBody User userRemove) {
        userRepository.findById(userId).ifPresent(user -> {
            user.getSelected().removeAll(userRemove.getSelected());
            user.getHidden().removeAll(userRemove.getHidden());
            Set<String> bookNamesToRemove = userRemove.getBooks().stream()
                    .map(UserBook::getName)
                    .collect(toSet());
            Set<UserBook> booksToRemove = user.getBooks().stream()
                    .filter(book -> bookNamesToRemove.contains(book.getName()))
                    .collect(toSet());
            booksToRemove.stream()
                    .map(UserBook::getId)
                    .forEach(bookRepository::deleteById);
            user.getBooks().retainAll(booksToRemove);
            userRepository.save(user);
        });
    }

    @GetMapping("/{userId}/books/{bookName}")
    public Optional<String> getBook(@PathVariable("userId") String userId, @PathVariable("bookName") String bookName) {
        return userBookRepository.findBookId(userId, bookName)
                .flatMap(bookRepository::findById)
                .map(Book::getContent);
    }
}
