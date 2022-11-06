package com.intruder.dataserver.service;

import com.intruder.dataserver.model.RelationKeyWord;
import com.intruder.dataserver.model.RelationSnSpgz;

import java.util.List;

public interface RelationKeyWordService {

    /**
     * Сохраняет список cooношений ключевых слов и СПГ
     * @param relationKeyWordList- список шаблонов для сохранения
     */
    void saveAll(List<RelationKeyWord> relationKeyWordList);

    /**
     * выгрузка всех соотношений ключевых слов и СПГЗ
     */
    List<RelationKeyWord> findALL();

}
