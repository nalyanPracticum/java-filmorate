package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    @NotBlank (message = "Электронная почта не может быть пустой")
    @Email (message = "Электронная почта должна соответствовать формату электронного адреса")
    private String email;

    @NotBlank (message = "Логин не может быть пустым")
    private String login;

    @AssertTrue(message = "Логин не должен содержать пробелы")
    public boolean isLoginValid() {
        return !login.contains(" ");
    }

    private String name;

    @NotNull(message = "Дата рождения не может быть пустой")
    @PastOrPresent (message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
