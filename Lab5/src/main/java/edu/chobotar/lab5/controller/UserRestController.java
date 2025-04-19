package edu.chobotar.lab5.controller;

import edu.chobotar.lab5.model.User;
import edu.chobotar.lab5.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
  @author Harsteel
  @project Lab5
  @class UserRestController
  @version 1.0.0
  @since 19.04.2025 - 23.31
*/
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @GetMapping
    public List<User> showAll() {
        return userService.getAll();
    }

    @GetMapping("{id}")
    public User showOneById(@PathVariable String id) {
        return userService.getById(id);
    }

    @PostMapping
    public User insert(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("{id}")
    public User edit(@PathVariable String id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        userService.deleteById(id);
    }
}
