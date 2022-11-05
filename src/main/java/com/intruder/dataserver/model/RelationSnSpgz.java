package com.intruder.dataserver.model;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "references_conformitymodel")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class RelationSnSpgz {

    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    /**
     * Шифр работы
     */
    @Column
    private String codeWork;
    /**
     * Наименование работы
     */
    @Column
    private String nameWork;
    /**
     * СПГЗ
     */
    @Column
    private String spgz;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RelationSnSpgz that = (RelationSnSpgz) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
