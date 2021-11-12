package ru.zhelper.zhelper.services.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.zhelper.zhelper.models.users.User;
import ru.zhelper.zhelper.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {
    public static final String USER_NOT_FOUND_WITH_NAME = "User not found with name:";
    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_WITH_NAME + username));
        return UserDetailsImpl.build(user);
    }
}
