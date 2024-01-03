package ru.mhelper.security.jwt_old;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.mhelper.exceptions.JwtAuthenticationException;
import ru.mhelper.helpers.DateTimeHelper;
import ru.mhelper.models.dto.JwtResponse;
import ru.mhelper.security.properties.JwtProperties;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    public static final String JWT_FOR_USER_HAVE_BEEN_CREATED = "JWT for user {} have been created.";
    public static final String LOAD_USER_DETAILS_FOR_TOKEN = "Load userDetails {} for token.";
    public static final String RESOLVE_JWT = "Resolve JWT:{}";
    public static final String VALIDATE_JWT = "Validate JWT:{}";
    public static final String ROLES_CLAIMS = "role";

    private final UserDetailsService userDetailsService;

    private final JwtProperties jwtProperties;

    private Key codeSecret;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        codeSecret = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String userName, Set<String> roles) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put(ROLES_CLAIMS, roles);
        Date now = DateTimeHelper.getCurrentDate();
        Date validity = getAccessTokenExpired(now);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(JWT_FOR_USER_HAVE_BEEN_CREATED, userName);
        }
        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(codeSecret)//
                .compact();
    }

    @NotNull
    private Date getAccessTokenExpired(Date now) {
        return new Date(now.getTime() + jwtProperties.getExpired());
    }

    public String createRefreshToken(String userName) {
        Claims claims = Jwts.claims().setSubject(userName);
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getRefresh());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(JWT_FOR_USER_HAVE_BEEN_CREATED, userName);
        }
        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(codeSecret)//
                .compact();
    }

    public JwtResponse refreshTokens(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new JwtAuthenticationException(JwtAuthenticationException.JWT_IS_INVALID);
        }
        String userName = getUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        Set<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return JwtResponse.builder()
                .userName(userName)
                .roles(roles)
                .accessToken(createAccessToken(userName, roles))
                .refreshToken(userName)
                .build();

    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(LOAD_USER_DETAILS_FOR_TOKEN, userDetails.getUsername());
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(codeSecret).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(jwtProperties.getHeader());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(RESOLVE_JWT, bearerToken);
        }
        if (bearerToken != null && bearerToken.startsWith(jwtProperties.getBearerPrefix())) {
            return bearerToken.substring(jwtProperties.getBearerPrefix().length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        if (token == null) {
            throw new JwtAuthenticationException(JwtAuthenticationException.JWT_IS_INVALID);
        }
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(VALIDATE_JWT, token);
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(codeSecret).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException(JwtAuthenticationException.JWT_IS_INVALID);
        }
    }
}
