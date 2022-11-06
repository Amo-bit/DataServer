package com.intruder.dataserver.service;

import com.intruder.dataserver.model.Estimate;

import java.util.List;

public interface EstimateService {
    /**
     * Создает новую запись сметы
     * @param estimate - запись сметы для создания
     */
    void create(Estimate estimate);

    /**
     * Записывает все записи из списка
     * @param estimateList  - записи работ сметы для сохранения
     */
    void saveAll(List<Estimate> estimateList);

}
