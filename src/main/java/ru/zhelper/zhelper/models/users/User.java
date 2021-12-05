package ru.zhelper.zhelper.models.users;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import ru.zhelper.zhelper.models.BaseEntity;
import ru.zhelper.zhelper.models.BaseStatus;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "telegramUserId")
        })
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
    public static final String POSTFIX_TELEGRAM_EMAIL = "@t.me";
    public static final String TELEGRAM_DB_PASSWORD = "$2a$12$skadH6bO.Oz7fIqnSfXxIO.ffv7XdXeOngnOy.q8aiGJmxPRaW7/."; //""
    public static final String TELEGRAM_PASSWORD = "";
    @NotBlank(message = "Name is mandatory")
    @Size(max = 30)
    private String username;

    @Size(max = 50)
    @Email
    private String email;

    @Size(max = 120)
    private String password;

    private String telegramUserId;

    @NotNull
    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Lob
    @Column(length = 10000)
    @Type(type = "org.hibernate.type.TextType")
    private String comment;

    public User(String userName, String email, String password) {
        this.username = userName;
        this.email = email;
        this.password = password;
        this.enabled = true;
        setStatus(BaseStatus.ACTIVE);
    }

    public User(String username, String email, String password, String telegramUserId, boolean enabled, Set<Role> roles, String comment) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.telegramUserId = telegramUserId;
        this.enabled = enabled;
        this.roles = roles;
        this.comment = comment;
    }

    public static User createNewTelegramUser(String telegramId) {
        return User.builder()
                .telegramUserId(telegramId)
                .username(telegramId)
                .password(TELEGRAM_DB_PASSWORD)
                .email(telegramId + POSTFIX_TELEGRAM_EMAIL)
                .enabled(true).build();
    }
}
