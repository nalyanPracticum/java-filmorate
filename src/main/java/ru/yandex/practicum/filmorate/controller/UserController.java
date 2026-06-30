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

    private final UserStorage userStorage;
    private final UserService service;

    @Autowired
    public UserController(UserStorage userStorage, UserService service) {
        this.userStorage = userStorage;
        this.service = service;
        log.info("UserController инициализирован");
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        log.info("Создание пользователя {}", user);
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody final User user) {
        log.info("Обновление пользователя {}", user);
        return userStorage.update(user);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получение списка пользователей");
        return userStorage.getUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Добавление друга {} пользователю {}", friendId, id);
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
