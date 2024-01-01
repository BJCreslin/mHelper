package ru.mhelper.security.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.mhelper.models.users.User;
import ru.mhelper.security.dto.AccessToken;
import ru.mhelper.security.dto.RefreshToken;
import ru.mhelper.security.properties.JwtProperties;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtProperties jwtProperties;

    private Key jwtKey;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        jwtKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public AccessToken generateAccessToken(User user) {
        LocalDateTime expiryDateTime = LocalDateTime.now().plusSeconds(jwtProperties.getExpired());
        // Создание JWT токена
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(jwtKey)
                .compact();

        return new AccessToken(token, generateRefreshToken(user).token(), expiryDateTime);
    }

    @Override
    public AccessToken generateAccessToken(User user, RefreshToken refreshToken) {
        LocalDateTime expiryDateTime = LocalDateTime.now().plusSeconds(jwtProperties.getExpired());
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(jwtKey)
                .compact();

        return new AccessToken(token, refreshToken.token(), expiryDateTime);
    }

    @Override
    public RefreshToken generateRefreshToken(User user) {
        return null;
    }

    @Override
    public boolean validateAccessToken(String token) {
        return false;
    }

    @Override
    public AccessToken refreshAccessToken(String refreshToken) {
        return null;
    }
}
