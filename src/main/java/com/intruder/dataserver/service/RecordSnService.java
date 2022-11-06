package com.intruder.dataserver.service;

import com.intruder.dataserver.model.RecordSn;
import com.intruder.dataserver.model.RelationSnSpgz;

import java.util.List;

public interface RecordSnService {
    /**
     * Создает новой записи
     * @param record - запись для создания
     */
    void create(RecordSn record);

    /**
     * Записывает все записи из списка
     * @param recordSnList  - записи работ для сохранения
     */
    void saveAll(List<RecordSn> recordSnList);

}
