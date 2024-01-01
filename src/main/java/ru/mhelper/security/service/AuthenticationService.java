package ru.mhelper.security.service;

import ru.mhelper.models.users.User;
import ru.mhelper.security.dto.AccessToken;
import ru.mhelper.security.dto.RefreshToken;

/**
 * класс отвечает за генерацию и проверку access и refresh токенов. Он будет содержать методы для создания новых токенов, проверки их валидности и обновления access токена с использованием refresh токена.
 */
public interface AuthenticationService {

    /**
     * Генерация нового access токена для указанного пользователя
     * Установка срока действия токена и других необходимых данных
     * Возвращение созданного access токена
     */
    AccessToken generateAccessToken(User user);

    /**
     * Генерация нового access токена для указанного пользователя
     * с использованием refresh токена
     * Установка срока действия токена и других необходимых данных
     * Возвращение созданного access токена
     */
    AccessToken generateAccessToken(User user, RefreshToken refreshToken);

    /**
     * Генерация нового refresh токена для указанного пользователя
     * Установка срока действия токена и других необходимых данных
     * Возвращение созданного refresh токена
     */
    RefreshToken generateRefreshToken(User user);

    /**
     * Проверка валидности access
     * Возвращение true, если токен валиден, иначе false
     */
    boolean validateAccessToken(String token);

    /**
     * Обновление access токена с использованием refresh токена
     * Возвращение нового access токена
     */
    AccessToken refreshAccessToken(String refreshToken);
}
