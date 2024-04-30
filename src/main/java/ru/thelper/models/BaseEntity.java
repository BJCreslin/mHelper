package ru.thelper.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.Contract;

import java.util.Date;
import java.util.Objects;


@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class BaseEntity {
    public static final String STATUS_FIELD = "status";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long id;

    @CreationTimestamp
    @Column(name = "created")
    public Date created;

    @UpdateTimestamp
    @Column(name = "updated")
    public Date updated;

    @Column(name = STATUS_FIELD, columnDefinition = "varchar(255) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    public BaseStatus status;

    @Lob
    @Column(length = 1000)
    public String comment;

    @Override
    @Contract(value = "null -> false", pure = true)
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
