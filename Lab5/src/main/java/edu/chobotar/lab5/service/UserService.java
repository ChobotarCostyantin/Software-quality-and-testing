package edu.chobotar.lab5.service;

import edu.chobotar.lab5.model.User;
import edu.chobotar.lab5.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        users.add(new User("1", "Олег Максимчук", "Tom_oleg", "maksimchuk@gmail.com", "0951234567", "Мопс"));
        users.add(new User("2", "Валерій Адамко", "Roxaan", "adamko@gmail.com", "0957654321", "Бандеромобіль"));
        users.add(new User("3", "Михайло Скорейко", "Tesey", "skoreyko@gmail.com", "0951237654", "Бомбардіро Крокоділо"));
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
        return userRepository.findById(id).orElse(null);
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(String id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }
}
