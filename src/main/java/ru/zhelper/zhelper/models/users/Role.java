package ru.zhelper.zhelper.models.users;

import lombok.Data;
import ru.zhelper.zhelper.models.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role() {
    }

    public Role(ERole name) {
        this.name = name;
    }
}
