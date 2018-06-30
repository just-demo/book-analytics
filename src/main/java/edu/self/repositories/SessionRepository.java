package edu.self.repositories;

import edu.self.model.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, String> {
    void deleteByUsername(String username);

    Optional<String> findUsernameById(String sessionId);

//    Iterable<User> findAllByUserId(String userId);
//
//    Optional<User> findByUserIdAndText(String userId, String text);
    //    @Override
//    Optional<Selection> findById(String id);
//
//    @Override
//    void delete(Selection deleted);
}
