package com.intruder.dataserver.service;

import com.intruder.dataserver.model.SampleTz;
import com.intruder.dataserver.repository.SampleTzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleTzServiceImpl implements SampleTzService {

    @Autowired
    private final SampleTzRepository sampleTzRepository;

    public SampleTzServiceImpl(SampleTzRepository sampleTzRepository) {
        this.sampleTzRepository = sampleTzRepository;
    }

    @Override
    public void create(SampleTz sampleTZ) {
        sampleTzRepository.save(sampleTZ);
    }
}