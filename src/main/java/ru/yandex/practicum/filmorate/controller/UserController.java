package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@ComponentScan
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserStorage userStorage, UserService service) {
        this.service = service;
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        return service.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody final User user) {
        return service.update(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) { return service.getUser(id); }

    @GetMapping
    public List<User> getUsers() {
        return service.getUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        service.addFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return service.getFriends(id);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        service.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        return service.getCommonFriends(userId, otherId);
    }
}
