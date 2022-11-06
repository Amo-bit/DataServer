package com.intruder.dataserver.service;

import com.intruder.dataserver.model.SpgzKpgz;

import java.util.List;

public interface SpgzKpgzService {

    /**
     * Создает новый список СПГЗ и КПГЗ
     * @param spgzKpgzList- список отношений для сохранения
     */
    void saveAll(List<SpgzKpgz> spgzKpgzList);

}
