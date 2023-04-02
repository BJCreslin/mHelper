package ru.mhelper.services.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.mhelper.exceptions.JwtAuthenticationException;

import java.io.IOException;

import static ru.mhelper.services.security.JwtTokenProvider.BEARER_PREFIX;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    public static final String LOGGING_WITH_TOKEN_NAME_S = "Logging with token name: %s";
    public static final String LOGGING_WITH_TOKEN = "Logging with token: %s";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(LOGGING_WITH_TOKEN, token));
        }
        try {
            if (token != null && token.startsWith(BEARER_PREFIX)) {
                token = token.substring(0, 7);
                if (jwtTokenProvider.validateToken(token)) {
                    Authentication auth = jwtTokenProvider.getAuthentication(token);
                    if (auth != null) {
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        if (logger.isDebugEnabled()) {
                            logger.debug(String.format(LOGGING_WITH_TOKEN_NAME_S, jwtTokenProvider.getUsername(token)));
                        }
                    }
                }
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            throw new JwtAuthenticationException(JwtAuthenticationException.JWT_IS_INVALID);
        }
        filterChain.doFilter(request, response);
    }
}
