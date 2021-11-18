package ru.zhelper.zhelper.models;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created")
    private Date created;

    @LastModifiedDate
    @Column(name = "updated")
    private Date updated;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private BaseStatus status;

    @Lob
    @Column(length = 10000)
    @Type(type = "org.hibernate.type.TextType")
    private String comment;
}
