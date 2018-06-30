package edu.self.repositories;

import edu.self.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
//    boolean existsByUsernameAndPassword(String id, String password);
//    Iterable<User> findAllByUserId(String userId);
//
//    Optional<User> findByUserIdAndText(String userId, String text);
    //    @Override
//    Optional<Selection> findById(String id);
//
//    @Override
//    void delete(Selection deleted);
}
