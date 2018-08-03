package edu.self.repositories;

import java.util.Optional;

public interface UserBookRepository {
    Optional<String> findBookId(String username, String bookName);

    Iterable<String> findBookIds(String username);
}
