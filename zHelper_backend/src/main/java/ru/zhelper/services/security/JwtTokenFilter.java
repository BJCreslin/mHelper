package ru.zhelper.services.security;

import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ru.zhelper.exceptions.JwtAuthenticationException;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends GenericFilterBean {
    public static final String LOGGING_WITH_TOKEN_NAME_S = "Logging with token name: %s";
    public static final String LOGGING_WITH_TOKEN = "Logging with token: %s";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(LOGGING_WITH_TOKEN, token));
        }
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);

                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format(LOGGING_WITH_TOKEN_NAME_S, jwtTokenProvider.getUsername(token)));
                    }
                }
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, JwtAuthenticationException.JWT_IS_INVALID);
            throw new JwtAuthenticationException(JwtAuthenticationException.JWT_IS_INVALID);
        }
        filterChain.doFilter(req, res);
    }
}
