package edu.self.repositories;

import edu.self.model.Credential;
import org.springframework.data.repository.CrudRepository;

public interface CredentialRepository extends CrudRepository<Credential, String> {
    boolean existsByUsernameAndPassword(String username, String password); //TODO: remove
}
