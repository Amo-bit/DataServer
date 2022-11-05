package com.intruder.dataserver.service;

import com.intruder.dataserver.model.Record;

public interface RecordService {
    /**
     * Создает новой записи
     * @param record - запись для создания
     */
    void create(Record record);
}
