package ru.mhelper.services.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.mhelper.exceptions.JwtAuthenticationException;
import ru.mhelper.models.users.Role;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class JwtTokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    public static final String JWT_FOR_USER_HAVE_BEEN_CREATED = "JWT for user {} have been created.";
    public static final String BEARER_PREFIX = "Bearer_";
    public static final String LOAD_USER_DETAILS_FOR_TOKEN = "Load userDetails {} for token.";
    public static final String RESOLVE_JWT = "Resolve JWT:{}";
    public static final String VALIDATE_JWT = "Validate JWT:{}";

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

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String userName, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("role", getRoleNames(roles));
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(JWT_FOR_USER_HAVE_BEEN_CREATED, userName);
        }
        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secret)//
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
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
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
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(VALIDATE_JWT, token);
            }
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
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
