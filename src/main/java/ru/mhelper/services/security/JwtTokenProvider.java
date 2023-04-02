package ru.mhelper.services.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.mhelper.exceptions.JwtAuthenticationException;
import ru.mhelper.models.users.Role;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class JwtTokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    public static final String JWT_FOR_USER_HAVE_BEEN_CREATED = "JWT for user {} have been created.";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String LOAD_USER_DETAILS_FOR_TOKEN = "Load userDetails {} for token.";
    public static final String RESOLVE_JWT = "Resolve JWT:{}";
    public static final String VALIDATE_JWT = "Validate JWT:{}";
    public static final String ROLES_CLAIMS = "role";

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;

    @Value("${jwt.header}")
    private String authorizationHeader;

    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private Key codeSecret;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        codeSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String createToken(String userName, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put(ROLES_CLAIMS, getRoleNames(roles));
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
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
        String bearerToken = req.getHeader(authorizationHeader);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(RESOLVE_JWT, bearerToken);
        }
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
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

    private List<String> getRoleNames(Set<Role> userRoles) {
        List<String> result = new ArrayList<>();
        userRoles.forEach(role -> result.add(role.getName()));
        return result;
    }
}
