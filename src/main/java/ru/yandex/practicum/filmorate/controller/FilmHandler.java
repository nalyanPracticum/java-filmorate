package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilmHandler {

    private final HashMap<Long, Film> films = new HashMap<>();

    private Long generatedId = 0L;

    public Film create(@Valid Film film) {
        film.setId(++generatedId);
        films.put(film.getId(), film);
        return film;
    }

    public Film update(@Valid Film film) {
        if (!films.containsKey(film.getId())) {
            throw new RuntimeException("Фильм с id " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
