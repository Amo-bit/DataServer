package com.intruder.dataserver.repository;

import com.intruder.dataserver.model.SampleTz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleTzRepository extends JpaRepository<SampleTz, Integer> {
}
