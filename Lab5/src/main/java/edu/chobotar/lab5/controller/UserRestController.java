package edu.chobotar.lab5.controller;

import edu.chobotar.lab5.model.User;
import edu.chobotar.lab5.request.UserCreateRequest;
import edu.chobotar.lab5.request.UserUpdateRequest;
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

    //============== request =====================
    @PostMapping("/dto")
    public User insert(@RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @PutMapping()
    public User edit(@RequestBody User user) {
        return userService.update(user);
    }

    //============== request =====================
    @PutMapping("/dto")
    public User edit(@RequestBody UserUpdateRequest request) {
        return userService.update(request);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        userService.deleteById(id);
    }
}
