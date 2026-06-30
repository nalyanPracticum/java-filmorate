package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();

    private Long generatedId = 0L;

    @Override
    public Film create(@Valid Film film) {
        film.setId(++generatedId);
        films.put(film.getId(), film);
        log.info("создан фильм {}", film);
        return film;
    }

    @Override
    public Film update(@Valid Film film) {
        if (!films.containsKey(film.getId())) {
            throw new IncorrectIdException("Фильм с id " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        log.info("обновлены данные о фильме {}", film);
        return film;
    }

    @Override
    public Film getFilm(Long id) {
        return films.get(id);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
