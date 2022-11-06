package com.intruder.dataserver.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "references_spgzkpgzmodel")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SpgzKpgz {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    /**
     * Наименование КПГЗ
     */
    @Column
    private String kpgz;
    /**
     * Наименование СПГЗ
     */
    @Column
    private String spgz;
    /**
     * Единицы измерения
     */
    @Column
    private String units;
    /**
     * ОКПД
     */
    @Column
    private String okpd;
    /**
     * ОКПД2
     */
    @Column
    private String okpd2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SpgzKpgz spgzKpgz = (SpgzKpgz) o;
        return id != null && Objects.equals(id, spgzKpgz.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
