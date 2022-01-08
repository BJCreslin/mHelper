package ru.zhelper.zhelper.models.users;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhelper.zhelper.models.BaseEntity;
import ru.zhelper.zhelper.models.BaseStatus;
import ru.zhelper.zhelper.models.procurements.Procurement;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
    public static final String POSTFIX_TELEGRAM_EMAIL = "@t.me";
    public static final String PREFIX_TELEGRAM_NAME = "tlgrm";
    public static final String TELEGRAM_DB_PASSWORD = "$2a$12$skadH6bO.Oz7fIqnSfXxIO.ffv7XdXeOngnOy.q8aiGJmxPRaW7/."; //""
    public static final String TELEGRAM_PASSWORD = "";

    @NotBlank(message = "Name is mandatory")
    @Size(max = 30)
    @Column(name = "name", unique = true)
    private String username;

    @Size(max = 50)
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @Size(max = 120)
    @Column(name = "password")
    private String password;

    @Column(name = "telegram_user", unique = true)
    private Long telegramUserId;

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
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_procurements",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "procurement_id"))
    private Set<Procurement> procurements = new HashSet<>();

    public User(String userName, String email, String password) {
        this.username = userName;
        this.email = email;
        this.password = password;
        this.enabled = true;
        setStatus(BaseStatus.ACTIVE);
    }

    public User(String username, String email, String password, Long telegramUserId, boolean enabled, String comment, Set<Role> roles, Set<Procurement> procurements) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.telegramUserId = telegramUserId;
        this.enabled = enabled;
        this.comment = comment;
        this.roles = roles;
        this.procurements = procurements;
    }

    public static User createNewTelegramUser(Long telegramId) {
        return User.builder()
                .telegramUserId(telegramId)
                .username(PREFIX_TELEGRAM_NAME + telegramId)
                .password(TELEGRAM_DB_PASSWORD)
                .email(telegramId + POSTFIX_TELEGRAM_EMAIL)
                .enabled(true).build();
    }
}
