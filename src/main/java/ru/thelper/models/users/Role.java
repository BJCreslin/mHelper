package ru.thelper.models.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.thelper.models.BaseEntity;

import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(name = "rolename")
    private String name;

    public Role() {
        super();
    }

    public Role(String name) {
        super();
        this.name = name;
    }

    public Role(ERole eRole) {
        super();
        this.name = eRole.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Role role = (Role) o;

        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
