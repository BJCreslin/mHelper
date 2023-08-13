package ru.mhelper.models.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.mhelper.models.BaseEntity;
import ru.mhelper.models.BaseStatus;
import ru.mhelper.models.user_procurement.UserProcurementLinks;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    public static final String POSTFIX_TELEGRAM_EMAIL = "@t.me";

    public static final String PREFIX_TELEGRAM_NAME = "tlgrm";

    public static final String TELEGRAM_DB_PASSWORD = "$2a$12$skadH6bO.Oz7fIqnSfXxIO.ffv7XdXeOngnOy.q8aiGJmxPRaW7/."; //""

    public static final String TELEGRAM_PASSWORD = "";

    public static final String ADDED_TO_USER = "Procurement {} has been added to User {}";

    public static final String DELETED_FROM_USER = "Procurement {} has been deleted from User {}";
    public static final String CREATED_BY_TELEGRAM_MESSAGE = "Created by Telegram";

    @NotBlank(message = "Name is mandatory")
    @Size(max = 30)
    @Column(name = "username", unique = true)
    private String username;

    @Size(max = 50)
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @Size(max = 120)
    @Column(name = "password")
    private String password;

    @Column(name = "tg_user", unique = true)
    private Long telegramUserId;

    @Column(name = "tg_statement", columnDefinition = "varchar(255) default 'NO STATEMENT'")
    @Enumerated(EnumType.STRING)
    private TelegramStateType telegramStateType;

    @NotNull
    @Column(name = "enabled")
    private boolean enabled;

    @Lob
    @Column(length = 10000, name = "comment")
    private String comment;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @Builder.Default
    private Set<UserProcurementLinks> userProcurementLinkses = new LinkedHashSet<>();

    public User(long chatId) {
        this.telegramUserId = chatId;
    }

    public User(String userName, String email, String password) {
        this.username = userName;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password, Long telegramUserId, TelegramStateType telegramStateType, boolean enabled, String comment, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.telegramUserId = telegramUserId;
        this.telegramStateType = telegramStateType;
        this.enabled = enabled;
        this.comment = comment;
        this.roles = roles;
    }

    public User(
            Long telegramUserId,
            String username,
            String password,
            String email,
            boolean enabled,
            BaseStatus status,
            Set<Role> roles
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.telegramUserId = telegramUserId;
        this.enabled = enabled;
        this.comment = CREATED_BY_TELEGRAM_MESSAGE;
        this.setStatus(status);
        this.roles = roles;
    }

    public Set<UserProcurementLinks> getUserProcurementLinkses() {
        return userProcurementLinkses;
    }

    public void setUserProcurementLinkses(Set<UserProcurementLinks> userProcurementLinkses) {
        this.userProcurementLinkses = userProcurementLinkses;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(Long telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public TelegramStateType getTelegramStateType() {
        return telegramStateType;
    }

    public void setTelegramStateType(TelegramStateType telegramStateType) {
        this.telegramStateType = telegramStateType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        if (!Objects.equals(username, user.username)) {
            return false;
        }
        if (!Objects.equals(email, user.email)) {
            return false;
        }
        return Objects.equals(telegramUserId, user.telegramUserId);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (telegramUserId != null ? telegramUserId.hashCode() : 0);
        return result;
    }

}
