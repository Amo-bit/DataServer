package com.intruder.dataserver.service;

import com.intruder.dataserver.model.SpgzKpgz;
import com.intruder.dataserver.repository.SpgzKpgzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpgzKpgzServiceImpl implements SpgzKpgzService {

    @Autowired
    private final SpgzKpgzRepository spgzKpgzRepository;


    public SpgzKpgzServiceImpl(SpgzKpgzRepository spgzKpgzRepository) {
        this.spgzKpgzRepository = spgzKpgzRepository;
    }

    @Override
    public void saveAll(List<SpgzKpgz> spgzKpgzList) {
        spgzKpgzRepository.saveAllAndFlush(spgzKpgzList);
    }
}
