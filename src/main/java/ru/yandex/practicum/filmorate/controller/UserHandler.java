package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserHandler {

    private final HashMap<Long, User> users = new HashMap<>();

    private Long generatedId = 0L;

    public User create(@Valid User user) {
        user.setId(++generatedId);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User update(@Valid User user) {
        if (!users.containsKey(user.getId())) {
            throw new RuntimeException("Пользователь с id " + user.getId() + " не найден");
        }
        users.put(user.getId(), user);
        return user;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
