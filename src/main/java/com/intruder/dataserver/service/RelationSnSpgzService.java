package com.intruder.dataserver.service;

import com.intruder.dataserver.model.RelationSnSpgz;
import com.intruder.dataserver.model.SampleTz;

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
}
