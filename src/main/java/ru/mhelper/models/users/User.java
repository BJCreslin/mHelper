package ru.mhelper.models.users;

import lombok.Builder;
import lombok.ToString;
import ru.mhelper.models.BaseEntity;
import ru.mhelper.models.procurements.Procurement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@ToString
@Table(name = "users")
@Builder
public class User extends BaseEntity {

    public static final String POSTFIX_TELEGRAM_EMAIL = "@t.me";
    public static final String PREFIX_TELEGRAM_NAME = "tlgrm";
    public static final String TELEGRAM_DB_PASSWORD = "$2a$12$skadH6bO.Oz7fIqnSfXxIO.ffv7XdXeOngnOy.q8aiGJmxPRaW7/."; //""
    public static final String TELEGRAM_PASSWORD = "";

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
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_procurements",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "procurement_id"))
    private List<Procurement> procurements = new ArrayList<>();

    public User() {
    }

    public User(String userName, String email, String password) {
        this.username = userName;
        this.email = email;
        this.password = password;
        this.enabled = true;
        this.procurements = new ArrayList<>();
    }

    public User(String username, String email, String password, Long telegramUserId, TelegramStateType telegramStateType, boolean enabled, String comment, Set<Role> roles, List<Procurement> procurements) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.telegramUserId = telegramUserId;
        this.telegramStateType = telegramStateType;
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

    public List<Procurement> getProcurements() {
        return procurements;
    }

    public void setProcurements(List<Procurement> procurements) {
        this.procurements = procurements;
    }

    public void addProcurement(Procurement procurement) {
        if (Objects.nonNull(procurement)) {
            this.procurements.add(procurement);
            procurement.getUsers().add(this);
        }
    }

    public void removeProcurement(Procurement procurement) {
        if (Objects.nonNull(procurement)) {
            this.procurements.remove(procurement);
            procurement.getUsers().remove(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        if (!Objects.equals(username, user.username)) return false;
        if (!Objects.equals(email, user.email)) return false;
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
