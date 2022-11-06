package com.intruder.dataserver.service;

import com.intruder.dataserver.model.Estimate;
import com.intruder.dataserver.repository.EstimateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstimateServiceImpl implements EstimateService {

    @Autowired
    private final EstimateRepository estimateRepository;

    public EstimateServiceImpl(EstimateRepository estimateRepository) {
        this.estimateRepository = estimateRepository;
    }

    @Override
    public void create(Estimate estimate) {
        estimateRepository.save(estimate);
    }

    @Override
    public void saveAll(List<Estimate> estimateList) {
        estimateRepository.saveAllAndFlush(estimateList);
    }

}
