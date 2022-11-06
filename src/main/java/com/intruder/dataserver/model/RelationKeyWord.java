package com.intruder.dataserver.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "references_relationkeywordmodel")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class RelationKeyWord {

    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    /**
     * Ключевые слова
     */
    @Column
    private String keyWord;
    /**
     * СПГЗ
     */
    @Column
    private String spgz;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RelationKeyWord that = (RelationKeyWord) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
