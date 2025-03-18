package ru.kuz.education.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kuz.education.auth.model.MyUser;
import ru.kuz.education.auth.config.MyUserDetails;
import ru.kuz.education.auth.repository.MyAuthRepository;

import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(MyUserDetailsService.class); // Логгер

    @Autowired
    private MyAuthRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername => {}", username);
        Optional<MyUser> user = repository.findByName(username);
        log.info(user.isPresent() ? "user found ": "user not found");
        return user.map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " не найден!"));
    }
}
