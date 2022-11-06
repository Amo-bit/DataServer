package com.intruder.dataserver.repository;

import com.intruder.dataserver.model.SampleTz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleTzRepository extends JpaRepository<SampleTz, Integer> {
    SampleTz findAllByIdSpgz(long idSpgz);
    List<SampleTz> findAllBySpgzContains(String spgz);
}
