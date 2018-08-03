package edu.self.repositories;

import edu.self.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>, UserBookRepository {
}
