package com.intruder.dataserver.service;

import com.intruder.dataserver.model.RelationSnSpgz;
import com.intruder.dataserver.repository.RelationSnSpgzRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationSnSpgzServiceImpl implements RelationSnSpgzService {

    private final RelationSnSpgzRepository relationSnSpgzRepository;

    public RelationSnSpgzServiceImpl(RelationSnSpgzRepository relationSnSpgzRepository) {
        this.relationSnSpgzRepository = relationSnSpgzRepository;
    }

    @Override
    public void create(RelationSnSpgz relationSnSPGZ) {
    }

    @Override
    public void saveAll(List<RelationSnSpgz> relationSnSPGZList) {
        relationSnSpgzRepository.saveAll(relationSnSPGZList);
    }

    @Override
    public RelationSnSpgz findAllByCodeWork(String codeWork) {
        return relationSnSpgzRepository.findALLByCodeWork(codeWork);
    }

    @Override
    public List<RelationSnSpgz> findALLByNameWorkContains(String nameWork) {
        return relationSnSpgzRepository.findALLByNameWorkContains(nameWork);
    }

    @Override
    public int findIdSpgzByCodeWork(String codeWork) {
        return relationSnSpgzRepository.findIdSpgzByCodeWork(codeWork);
    }
}
