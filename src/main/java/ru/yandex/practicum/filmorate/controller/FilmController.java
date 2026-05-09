package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmHandler handler = new FilmHandler();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создается фильм {}", film);
        return handler.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма {}", film);
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return handler.update(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получение всех фильмов");
        return handler.getFilms();
    }
}
