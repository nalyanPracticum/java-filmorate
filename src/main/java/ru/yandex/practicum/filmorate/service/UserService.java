package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final InMemoryUserStorage users;

    public UserService(InMemoryUserStorage users) {
        this.users = users;
    }

    public void addFriend(Long userId, Long friendId) {
        log.info("Запущен сервис добавления в друзья пользователем {} друга {}", userId, friendId);

        validateUserId(userId);
        validateUserId(friendId);

        User user = users.getUser(userId);
        User friend = users.getUser(friendId);

        Set<Long> userFriendsList = user.getFriends();
        Set<Long> friendFriendsList = friend.getFriends();

        if (!userFriendsList.contains(friendId)) {
            userFriendsList.add(friendId);
            friendFriendsList.add(userId);

            user.setFriends(userFriendsList);
            log.info("друг {} добавлен в список друзей пользователя {}", friendId, userId);
            friend.setFriends(friendFriendsList);
            log.info("пользователь {} добавлен в список друзей друга {}", userId, friendId);

        } else {
            log.info("Друг с id {} уже в списке друзей", friend.getId());
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        log.info("Запущен сервис удаления из друзей пользователем {} друга {}", userId, friendId);

        validateUserId(userId);
        validateUserId(friendId);

        User user = users.getUser(userId);
        User friend = users.getUser(friendId);

        Set<Long> userFriendsList = user.getFriends();
        Set<Long> friendFriendsList = friend.getFriends();

        if (userFriendsList.contains(friendId)) {
            userFriendsList.remove(friendId);
            friendFriendsList.remove(userId);

            user.setFriends(userFriendsList);
            log.info("Успешно удалён друг {} из списка пользователя {}", friendId, userId);
            friend.setFriends(friendFriendsList);
            log.info("Успешно удалён пользователь {} из списка друга {}", userId, friendId);

        } else {
            log.info("Друга {} нет в списке друзей пользователя {}", friendId, userId);
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

        User user = users.getUser(userId);
        User other = users.getUser(otherId);

        Set<Long> userListFriends = user.getFriends();
        Set<Long> otherListFriends = other.getFriends();

        return userListFriends.stream()
                .filter(otherListFriends::contains)
                .map(users::getUser)
                .toList();
    }

    public void validateUserId(Long id) {
        if(users.getUser(id) == null) {
            log.info("пользователь {} не найден", id);
            throw new IncorrectIdException("некорректный id пользователя");
        }
    }
}
