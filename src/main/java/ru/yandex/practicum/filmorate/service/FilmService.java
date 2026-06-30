package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Set;

@Slf4j
@Service
public class FilmService {

    private final InMemoryFilmStorage films;
    private final InMemoryUserStorage users;

    public FilmService(InMemoryFilmStorage films, InMemoryUserStorage users) {
        this.films = films;
        this.users = users;
    }

    public void addLike(Long filmId, Long userId) {
        validateId(filmId, "film");
        validateId(userId, "user");

        Film film = films.getFilm(filmId);
        Set<Long> likes = film.getLikes();

        if (likes.contains(userId)) {
            log.info("пользователь {} уже отметил фильм {}", userId, filmId);
            return;
        }

        likes.add(userId);
        log.info("пользователь {} отметил фильм {}", userId, filmId);
    }

    public void removeLike(Long filmId,Long userId) {
        validateId(filmId, "film");
        validateId(userId, "user");

        Film film = films.getFilm(filmId);
        Set<Long> likes = film.getLikes();

        if (!likes.contains(userId)) {
            log.info("пользователем {} фильм {} не отмечался", userId, filmId);
            return;
        }

        likes.remove(userId);
        log.info("пользователь {} удалил лайк к фильму {}", userId, filmId);
    }

    public void validateId(Long id, String key) {
        if (key.equals("film") && films.getFilm(id) == null) {
            log.info("фильм {} не найден", id);
            throw new IncorrectIdException("некорректный id фильма");
        } else if (key.equals("user") && users.getUser(id) == null) {
            log.info("пользователь {} не найден", id);
            throw new IncorrectIdException("некорректный id пользователя");
        }

    }
}
