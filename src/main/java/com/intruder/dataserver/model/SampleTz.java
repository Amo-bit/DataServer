package com.intruder.dataserver.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "references_tzmodel")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SampleTz {

    /**
     * ID шаблона тз
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    /**
     * Наименование шаблона
     */
    @Column(name = "name")
    private String nameTz;
    /**
     * КПГЗ
     */
    @Column
    private String kpgz;
    /**
     * ID СПГЗ
     */
    @Column
    private long idSpgz;
    /**
     * СПГЗ
     */
    @Column
    private String spgz;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SampleTz sampleTZ = (SampleTz) o;
        return id != null && Objects.equals(id, sampleTZ.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
