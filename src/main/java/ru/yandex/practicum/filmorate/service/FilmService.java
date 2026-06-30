package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectDataException;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage films;
    private final UserStorage users;

    private enum ValidationKey { USER, FILM }

    public FilmService(FilmStorage films, UserStorage users) {
        this.films = films;
        this.users = users;
    }

    public void addLike(Long filmId, Long userId) {
        validateId(filmId, ValidationKey.FILM);
        validateId(userId, ValidationKey.USER);

        Set<Long> likes = films.getFilm(filmId).getLikes();

        if (likes.contains(userId)) {
            throw new IncorrectDataException("пользователь " + userId + " уже отметил фильм " + filmId);
        }

        likes.add(userId);
        log.info("пользователь {} отметил фильм {}", userId, filmId);
    }

    public void removeLike(Long filmId,Long userId) {
        validateId(filmId, ValidationKey.FILM);
        validateId(userId, ValidationKey.USER);

        Set<Long> likes = films.getFilm(filmId).getLikes();

        if (!likes.contains(userId)) {
            throw new IncorrectDataException("пользователем " + userId + " фильм " + filmId + " не отмечался");
        }

        likes.remove(userId);
        log.info("пользователь {} удалил лайк к фильму {}", userId, filmId);
    }

    public List<Film> getPopular(int count) {
        return films.getFilms().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateId(Long id, ValidationKey key) {
        if (key.equals(ValidationKey.FILM) && films.getFilm(id) == null) {
            throw new IncorrectIdException("некорректный id фильма: " + id);
        } else if (key.equals(ValidationKey.USER) && users.getUser(id) == null) {
            throw new IncorrectIdException("некорректный id пользователя: " + id);
        }
    }
}