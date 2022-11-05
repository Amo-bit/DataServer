package com.intruder.dataserver.service;

import com.intruder.dataserver.model.SampleTz;

public interface SampleTzService {
    /**
     * Создает новый шаблон
     * @param sampleTZ- шаблон для создания
     */
    void create(SampleTz sampleTZ);
}
