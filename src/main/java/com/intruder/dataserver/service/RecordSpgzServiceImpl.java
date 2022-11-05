package com.intruder.dataserver.service;

import com.intruder.dataserver.model.RecordSpgz;
import com.intruder.dataserver.repository.RecordSpgzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordSpgzServiceImpl implements RecordSpgzService {

    @Autowired
    private final RecordSpgzRepository recordSpgzRepository;

    public RecordSpgzServiceImpl(RecordSpgzRepository recordSpgzRepository) {
        this.recordSpgzRepository = recordSpgzRepository;
    }

    @Override
    public void create(RecordSpgz recordSPGZ) {
    }
}
