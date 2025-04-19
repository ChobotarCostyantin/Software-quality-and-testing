package edu.chobotar.lab5.repository;

import edu.chobotar.lab5.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/*
  @author Harsteel
  @project Lab5
  @class UserRepository
  @version 1.0.0
  @since 19.04.2025 - 23.02
*/
@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
