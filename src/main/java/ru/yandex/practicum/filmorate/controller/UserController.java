package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private UserHandler handler = new UserHandler();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Создание пользователя {}", user);
        return handler.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя {}", user);
        return handler.update(user);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получение списка пользователей");
        return handler.getUsers();
    }
}
