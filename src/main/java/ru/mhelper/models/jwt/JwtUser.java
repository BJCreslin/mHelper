package ru.mhelper.models.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.mhelper.models.users.User;

import java.util.Collection;
import java.util.stream.Collectors;

public class JwtUser implements UserDetails {

    private static final long serialVersionUID = -4603089111149819076L;

    private final Long id;
    private final String username;
    private final String email;
    private final Long telegramUserId;
    @JsonIgnore
    private final String password;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUser(Long id, String username, String email, Long telegramUserId, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.telegramUserId = telegramUserId;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public static UserDetails build(User user) {
        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getTelegramUserId(),
                user.getPassword(),
                user.isEnabled(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Long getTelegramUserId() {
        return telegramUserId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
