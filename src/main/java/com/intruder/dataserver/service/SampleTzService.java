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
}
