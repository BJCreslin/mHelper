package ru.mhelper.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mhelper.models.users.User;
import ru.mhelper.security.dto.AccessToken;
import ru.mhelper.security.dto.RefreshToken;
import ru.mhelper.security.properties.JwtProperties;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtProperties jwtProperties;

    @Override
    public AccessToken generateAccessToken(User user) {
        return null;
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
