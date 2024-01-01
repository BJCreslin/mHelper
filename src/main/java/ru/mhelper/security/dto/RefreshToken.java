package ru.mhelper.security.dto;

import java.time.LocalDateTime;

/**
 * Этот класс представляет refresh токен, который будет использоваться для обновления access токена после истечения его срока действия.
 */
public record RefreshToken(String token, LocalDateTime expiryDate) {
}
