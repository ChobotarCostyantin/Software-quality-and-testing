package edu.chobotar.lab5.service;

import edu.chobotar.lab5.model.User;
import edu.chobotar.lab5.repository.UserRepository;
import edu.chobotar.lab5.request.UserCreateRequest;
import edu.chobotar.lab5.request.UserUpdateRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/*
  @author Harsteel
  @project Lab5
  @class UserService
  @version 1.0.0
  @since 19.04.2025 - 23.06
*/
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private List<User> users = new ArrayList<>();
    {
        users.add(new User("Олег Максимчук", "Tom_oleg", "maksimchuk@gmail.com", "0951234567", "Мопс"));
        users.add(new User("2", "Валерій Адамко", "Roxaan", "adamko@gmail.com", "0957654321", "Бандеромобіль"));
        users.add(new User("Михайло Скорейко", "Tesey", "skoreyko@gmail.com", "0951237654", "Бомбардіро Крокоділо"));
        users.add(new User("4", "В'ячеслав Москалюк", "Ikaut", "moskaliuk@gmail.com", "0985358765", "Швайн"));
    }

    @PostConstruct
    void init()
    {
        userRepository.deleteAll();
        userRepository.saveAll(users);
    }

    //CRUD
    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty() || user.get() == null) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        return user.get();
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User create(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("User with email " + request.email() + " already exists");
        }
        User user = mapToUser(request);
        user.setCreateDate(LocalDateTime.now());
        user.setUpdateDate(new ArrayList<LocalDateTime>());
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    private User mapToUser(UserCreateRequest request) {
        return new User(request.name(), request.username(), request.email(), request.phoneNumber(), request.gender());
    }

    public User update(UserUpdateRequest request) {
        User userPersisted = userRepository.findById(request.id()).orElse(null);
        if (userPersisted != null) {
            List<LocalDateTime> updateDates = userPersisted.getUpdateDate();
            updateDates.add(LocalDateTime.now());
            User userToUpdate =
                    User.builder()
                            .id(request.id())
                            .name(request.name())
                            .username(request.username())
                            .email(request.email())
                            .phoneNumber(request.phoneNumber())
                            .gender(request.gender())
                            .createDate(userPersisted.getCreateDate())
                            .updateDate(updateDates)
                            .build();
            return userRepository.save(userToUpdate);
        }
        return null;
    }
}
