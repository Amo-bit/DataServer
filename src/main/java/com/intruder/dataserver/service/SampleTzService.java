package com.intruder.dataserver.service;

import com.intruder.dataserver.model.SampleTz;

import java.util.List;

public interface SampleTzService {
    /**
     * Создает отношение
     * @param sampleTZ- отношение для создания
     */
    void create(SampleTz sampleTZ);

    /**
     * Создает новый список отношений
     * @param sampleTzList- список отношений для сохранения
     */
    void saveAll(List<SampleTz> sampleTzList);

    /**
     * Поиск шаблона тз по id СПГЗ
     * @param idSpgz - id СПГЗ для поиска
     * @return
     */
    SampleTz findAllByIdSpgz(long idSpgz);
}
