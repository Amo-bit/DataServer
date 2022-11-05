package com.intruder.dataserver.service;

import com.intruder.dataserver.model.RelationSnSpgz;
import com.intruder.dataserver.repository.RelationSnSpgzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationSnSpgzServiceImpl implements RelationSnSpgzService {

    @Autowired
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
}
