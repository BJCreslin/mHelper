package ru.mhelper.services.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mhelper.repository.UserRepository;
import ru.mhelper.models.jwt.JwtUser;
import ru.mhelper.models.users.User;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUserDetailsService.class);
    public static final String USER_NOT_FOUND_WITH_NAME = "User not found with name:";
    public static final String USER_LOADED = "User with name %s loaded";
    private final UserRepository repository;

    public JwtUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_WITH_NAME + username));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format(USER_LOADED, username));
        }
        return JwtUser.build(user);
    }
}
