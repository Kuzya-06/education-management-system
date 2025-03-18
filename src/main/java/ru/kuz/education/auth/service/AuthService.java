package ru.kuz.education.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kuz.education.auth.model.MyUser;
import ru.kuz.education.auth.repository.MyAuthRepository;


@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class); // Логгер

    private MyAuthRepository repository;
    private PasswordEncoder encoder;

    public AuthService(MyAuthRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Transactional
    public void registerUser(MyUser user) {
        log.info("Begin addUser(). user: " + user);
        if (repository.findByName(user.getName()).isPresent()) {
            log.info("Логин уже занят! user: " + user.getName());
            throw new RuntimeException("Логин уже занят!");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
        log.info("Register user: " + user);
    }
}
