package com.intruder.dataserver.service;

import com.intruder.dataserver.model.RecordSpgz;

public interface RecordSpgzService {
    /**
     * Создает новую запись СПГЗ
     * @param recordSPGZ - запись СПГЗ для создания
     */
    void create(RecordSpgz recordSPGZ);
}
