package com.intruder.dataserver.service;

import com.intruder.dataserver.model.RelationSnSpgz;

import java.util.List;

public interface RelationSnSpgzService {
    /**
     * Создает новую запись СПГЗ
     * @param relationSnSpgz - запись СПГЗ для создания
     */
    void create(RelationSnSpgz relationSnSpgz);

    /**
     * Создает новый список шаблонов
     * @param relationSnSPGZList- список шаблонов для сохранения
     */
    void saveAll(List<RelationSnSpgz> relationSnSPGZList);

    /**
     * Поиск соотношения по шифру работ
     * @param codeWork - шифр работ для поиска
     */
    RelationSnSpgz findAllByCodeWork(String codeWork);

    /**
     * Поиск соотношения по наименованию работ
     * @param nameWork - шифр работ для поиска
     */
    List<RelationSnSpgz> findALLByNameWorkContains(String nameWork);

    /**
     * Поиск id СПГЗ по шифру работ
     * @param codeWork - шифр работ для поиска
     * @return
     */
    int findIdSpgzByCodeWork(String codeWork);
}
