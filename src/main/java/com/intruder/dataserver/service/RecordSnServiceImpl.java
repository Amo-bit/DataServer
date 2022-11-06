package com.intruder.dataserver.service;

import com.intruder.dataserver.model.RecordSn;
import com.intruder.dataserver.repository.RecordSnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordSnServiceImpl implements RecordSnService {

    @Autowired
    private final RecordSnRepository recordRepository;

    public RecordSnServiceImpl(RecordSnRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public void create(RecordSn record) {
        recordRepository.save(record);
    }

    @Override
    public void saveAll(List<RecordSn> recordSnList) {
        recordRepository.saveAllAndFlush(recordSnList);
    }
}
