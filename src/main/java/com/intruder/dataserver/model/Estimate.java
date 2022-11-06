package com.intruder.dataserver.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "references_estimatemodel")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Estimate {

    /**
     * ID записи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    /**
     * № п/п
     */
    @Column
    private int subItemNumber;
    /**
     * Шифр, номера нормативов и коды ресурсов
     */
    @Column
    private String codeDocument;
    /**
     * тип сборника
     */
    @Column
    private String typeDocument;
    /**
     * дополнение
     */
    @Column
    private String addition;
    /**
     * номер сборника коэффициентов
     */
    @Column
    private int numberDocument;
    /**
     * период сборника коэффициентов
     */
    @Column
    private String dateDocument;
    /**
     * ID СПГЗ
     */
    @Column
    private int idSubChapter;

    /**
     * наименование работ и затрат
     */
    @Column
    private String nameWorksAndCosts;
    /**
     * ед.изм.
     */
    @Column
    private String unit;
    /**
     * количество единиц
     */
    @Column
    private double countUnits;
    /**
     * статья затрат
     */
    @Column
    private String costItem;
    /**
     * цена за единицу измерения, руб
     */
    @Column
    private double pricePerUnit;
    /**
     * поправочный коэффициент
     */
    @Column
    private double correctionCoefficient;
    /**
     * коэффициент зимних удорожаний
     */
    @Column
    private double winterCoefficient;
    /**
     * Затраты в базисном уровне цен, руб
     */
    @Column
    private double basicCosts;
    /**
     * коэффициенты пересчета нормы НР и СП
     */
    @Column
    private double conversionCoefficient;
    /**
     * всего затрат в текущем уровне цен, руб
     */
    @Column
    private double totalCostsAtTheCurrentPriceLevel;
    /**
     * Итого всего затрат по наименованию работ, руб
     */
    @Column
    private double totalCostsForTheWorkName;
    /**
     * Итого по подразделу, руб
     */
    @Column
    private double totalBySubChapter;
    /**
     * Итого по разделу, руб
     */
    @Column
    private double totalByChapter;
    /**
     * Итого по всем разделам
     */
    @Column
    private double total;
    /**
     * НДС, руб
     */
    @Column
    private double nds;
    /**
     * Всего, руб
     */
    @Column
    private double finalSum;
    /**
     * Итоговая сумма с коэффициентами финансирования, руб
     */
    @Column
    private double finalSumWithCoefficient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Estimate estimate = (Estimate) o;
        return id != null && Objects.equals(id, estimate.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
