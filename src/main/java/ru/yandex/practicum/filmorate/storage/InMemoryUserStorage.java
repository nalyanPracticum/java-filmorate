package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();

    private Long generatedId = 0L;

    @Override
    public User create(@Valid User user) {
        user.setId(++generatedId);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Создан пользователь {}", user);
        return user;
    }

    @Override
    public User update(@Valid User user) {
        Long userId = user.getId();
        if (!users.containsKey(userId)) {
            throw new IncorrectIdException("Пользователь с id " + userId + " не найден");
        }
        users.put(userId, user);
        return user;
    }

    @Override
    public User getUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new IncorrectIdException("пользователь " + id + " не найден");
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
