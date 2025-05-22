package edu.chobotar.lab5.repository;

import edu.chobotar.lab5.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
  @author Harsteel
  @project Lab5
  @class UserRepository
  @version 1.0.0
  @since 19.04.2025 - 23.02
*/
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Пошук за ім'ям користувача
    Optional<User> findByUsername(String username);

    // Пошук за електронною поштою
    Optional<User> findByEmail(String email);

    // Пошук за префіксом номера телефону
    List<User> findByPhoneNumberStartingWith(String prefix);

    public boolean existsByEmail(String email);
}
