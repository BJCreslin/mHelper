package ru.mhelper.security.dto;

import java.time.LocalDateTime;

/**
 * Этот класс представляет access токен, который будет выдан пользователю при успешной аутентификации.
 *
 * @param token
 * @param refreshToken
 * @param expiryDate
 */
public record AccessToken(String token, String refreshToken, LocalDateTime expiryDate) {
}
