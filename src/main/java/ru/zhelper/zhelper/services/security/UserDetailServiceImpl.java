package ru.zhelper.zhelper.services.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.zhelper.zhelper.models.users.User;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailServiceImpl implements UserDetails {
    private static final long serialVersionUID = -4603089111149819076L;

    private Long id;
    private String username;
    private String email;
    private String telegramUserId;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;


    public UserDetailServiceImpl(Long id, String username, String email, String telegramUserId, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.telegramUserId = telegramUserId;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetails build(User user) {
        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().getName()))
                .collect(Collectors.toList());
        return new UserDetailServiceImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getTelegramUserId(),
                user.getPassword(),
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

    public String getTelegramUserId() {
        return telegramUserId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
