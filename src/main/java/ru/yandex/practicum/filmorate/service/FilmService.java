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

    private static final int DEFAULT_COUNT_FILM = 10;

    public FilmService(FilmStorage films, UserStorage users) {
        this.films = films;
        this.users = users;
    }

    public Film create(Film film) {
        return films.create(film);
    }

    public Film update(Film film) {
        return films.update(film);
    }

    public Film getFilm(Long id) {
        return films.getFilm(id);
    }

    public List<Film> getFilms() {
        return films.getFilms();
    }

    public void addLike(Long filmId, Long userId) {

        Set<Long> likes = films.getFilm(filmId).getLikes();

        if (users.getUser(userId) == null) {
            throw new IncorrectIdException("пользователь: " + userId + "не найден");
        }
        if (likes.contains(userId)) {
            throw new IncorrectDataException("пользователь " + userId + " уже отметил фильм " + filmId);
        }

        likes.add(userId);
        log.info("пользователь {} отметил фильм {}", userId, filmId);
    }

    public void removeLike(Long filmId,Long userId) {

        Set<Long> likes = films.getFilm(filmId).getLikes();

        if (users.getUser(userId) == null) {
            throw new IncorrectIdException("пользователь: " + userId + "не найден");
        }
        if (!likes.contains(userId)) {
            throw new IncorrectDataException("пользователем " + userId + " фильм " + filmId + " не отмечался");
        }

        likes.remove(userId);
        log.info("пользователь {} удалил лайк к фильму {}", userId, filmId);
    }

    public List<Film> getPopular(Integer count) {
        int finalCount = ((count == null) || (count < 0)) ? DEFAULT_COUNT_FILM : count;

        return films.getFilms().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(finalCount)
                .collect(Collectors.toList());
    }
}