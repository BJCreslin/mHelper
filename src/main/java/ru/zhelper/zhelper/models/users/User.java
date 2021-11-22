package ru.zhelper.zhelper.models.users;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import ru.zhelper.zhelper.models.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
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
public class User extends BaseEntity {
    @NotBlank(message = "Name is mandatory")
    @Size(max = 30)
    private String username;

    @Size(max = 50)
    @Email
    @Null
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
    }
}
