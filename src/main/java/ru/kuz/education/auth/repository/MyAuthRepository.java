package ru.kuz.education.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kuz.education.auth.model.MyUser;


import java.util.Optional;

@Repository
public interface MyAuthRepository extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findByName(String username);

}
