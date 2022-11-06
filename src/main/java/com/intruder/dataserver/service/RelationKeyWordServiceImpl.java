package com.intruder.dataserver.service;

import com.intruder.dataserver.model.RelationKeyWord;
import com.intruder.dataserver.model.RelationSnSpgz;
import com.intruder.dataserver.repository.RelationKeyWordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationKeyWordServiceImpl implements RelationKeyWordService {

    private final RelationKeyWordRepository relationKeyWordRepository;

    public RelationKeyWordServiceImpl(RelationKeyWordRepository relationKeyWordRepository) {
        this.relationKeyWordRepository = relationKeyWordRepository;
    }


    @Override
    public void saveAll(List<RelationKeyWord> relationKeyWordList) {
        relationKeyWordRepository.saveAllAndFlush(relationKeyWordList);
    }

    @Override
    public List<RelationKeyWord> findALL() {
        return relationKeyWordRepository.findAll();
    }
}
