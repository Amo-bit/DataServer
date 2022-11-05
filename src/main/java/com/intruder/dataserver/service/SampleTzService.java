package com.intruder.dataserver.service;

import com.intruder.dataserver.model.SampleTz;

import java.util.List;

public interface SampleTzService {
    /**
     * Создает новый шаблон
     * @param sampleTZ- шаблон для создания
     */
    void create(SampleTz sampleTZ);

    /**
     * Создает новый список шаблонов
     * @param sampleTzList- список шаблонов для сохранения
     */
    void saveAll(List<SampleTz> sampleTzList);
}
