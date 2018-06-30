package edu.self.rest;

import edu.self.model.Session;
import edu.self.dto.UserLogin;
import edu.self.repositories.CredentialRepository;
import edu.self.repositories.SessionRepository;
import edu.self.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static java.util.UUID.randomUUID;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

@RestController
@RequestMapping("/sessions")
public class SessionController {
    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @PostMapping
    public Session login(@RequestBody UserLogin userLogin) {
        if (!credentialRepository.existsByUsernameAndPassword(userLogin.getUsername(), md5Hex(userLogin.getPassword()))) {
            throw new IllegalArgumentException("Login failed");
        }
        return startSession(userLogin.getUsername());
        // TODO: replace with existsByUsernameAndPassword
//        return userRepository.findById(userLogin.getUsername())
//                .map(user -> user.getPassword().equals(md5Hex(userLogin.getPassword())))
//                .isPresent();
    }

    @DeleteMapping("/{sessionId}")
    public void logout(@PathVariable("sessionId") String sessionId) {
        sessionRepository.deleteById(sessionId);
    }


    private Session startSession(String username) {
        sessionRepository.deleteByUsername(username);
        String sessionId = randomUUID().toString();
        Session session = new Session(sessionId, username);
        return sessionRepository.save(session);
    }
}
