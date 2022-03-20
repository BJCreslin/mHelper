package ru.mhelper.models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @CreationTimestamp
    @Column(name = "created")
    private Date created;

    @UpdateTimestamp
    @Column(name = "updated")
    private Date updated;

    @Column(name = "status", columnDefinition = "varchar(255) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private BaseStatus status;

    @Lob
    @Column(length = 1000)
    private String comment;

    protected BaseEntity() {
    }
}
