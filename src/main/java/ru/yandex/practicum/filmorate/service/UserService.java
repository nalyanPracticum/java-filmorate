package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectDataException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final UserStorage users;

    public UserService(UserStorage users) {
        this.users = users;
    }

    public void addFriend(Long userId, Long friendId) {
        log.info("Запущен сервис добавления в друзья пользователем {} друга {}", userId, friendId);

        validateUserId(userId);
        validateUserId(friendId);

        Set<Long> userFriendsList = users.getUser(userId).getFriends();
        Set<Long> friendFriendsList = users.getUser(friendId).getFriends();

        if (!userFriendsList.contains(friendId)) {

            userFriendsList.add(friendId);
            log.info("друг {} добавлен в список друзей пользователя {}", friendId, userId);

            friendFriendsList.add(userId);
            log.info("пользователь {} добавлен в список друзей друга {}", userId, friendId);

        } else {
            throw new IncorrectDataException("Друг " + friendId + " уже в списке друзей пользователя" + userId);
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        log.info("Запущен сервис удаления из друзей пользователем {} друга {}", userId, friendId);

        validateUserId(userId);
        validateUserId(friendId);

        Set<Long> userFriendsList = users.getUser(userId).getFriends();
        Set<Long> friendFriendsList = users.getUser(friendId).getFriends();

        if (userFriendsList.contains(friendId)) {

            userFriendsList.remove(friendId);
            log.info("Успешно удалён друг {} из списка пользователя {}", friendId, userId);

            friendFriendsList.remove(userId);
            log.info("Успешно удалён пользователь {} из списка друга {}", userId, friendId);

        } else {
            throw new IncorrectDataException("Друга " + friendId + " нет в списке друзей пользователя " + userId);
        }
    }

    public List<User> getFriends(Long userId) {
        log.info("Запущен сервис получения списка друзей пользователя {}", userId);

        validateUserId(userId);
        Set<Long> friendsId = users.getUser(userId).getFriends();
        return friendsId.stream()
                .map(users::getUser)
                .toList();
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        log.info("Запущен сервис получения списка общих друзей пользователя {} и {}", userId, otherId);
        validateUserId(userId);
        validateUserId(otherId);

        Set<Long> userListFriends = users.getUser(userId).getFriends();
        Set<Long> otherListFriends = users.getUser(otherId).getFriends();

        return userListFriends.stream()
                .filter(otherListFriends::contains)
                .map(users::getUser)
                .toList();
    }

    public void validateUserId(Long id) {
        if (users.getUser(id) == null) {
            throw new IncorrectIdException("некорректный id пользователя: " + id);
        }
    }
}
