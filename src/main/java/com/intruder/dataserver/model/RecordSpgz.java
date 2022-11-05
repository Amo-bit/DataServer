package com.intruder.dataserver.model;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "references_spgzmodel")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class RecordSpgz {

    /**
     * ID СПГЗ
     */
    @Id
    @Column
    private Long id;
    /**
     * СПГЗ
     */
    @Column
    private String spgz;
    /**
     * КПГЗ
     */
    @Column
    private String kpgz;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RecordSpgz that = (RecordSpgz) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
