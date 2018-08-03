package edu.self.repositories;

import edu.self.model.Book;
import edu.self.model.User;
import edu.self.model.UserBook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.CollectionUtils.isEmpty;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserBookRepositoryTest {
    @Autowired
    private UserRepository instance;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    public void findBookIds() {
        User user = createUser();
        Iterable<String> expectedBookIds = Stream.generate(() -> createBook(user))
                .limit(3)
                .map(UserBook::getId)
                .collect(toSet());

        Iterable<String> bookIds = instance.findBookIds(user.getUsername());

        assertThat(bookIds).containsExactlyInAnyOrderElementsOf(expectedBookIds);
    }

    private User createUser() {
        User user = new User();
        instance.save(user);
        return user;
    }

    private UserBook createBook(User user) {
        Book book = save(new Book(randomAlphabetic(10)));
        UserBook userBook = new UserBook();
        userBook.setId(book.getId());
        userBook.setName(randomAlphabetic(10));
        if (isEmpty(user.getBooks())) {
            user.setBooks(new HashSet<>());
        }
        user.getBooks().add(userBook);
        save(user);
        return userBook;
    }

    private <T> T save(T entity) {
        mongoOperations.save(entity);
        return entity;
    }
}