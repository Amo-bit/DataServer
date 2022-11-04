package com.intruder.dataserver.service;

import com.intruder.dataserver.model.Record;
import com.intruder.dataserver.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private final RecordRepository recordRepository;

    public RecordServiceImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public void create(Record record) {
        recordRepository.save(record);
    }
}
