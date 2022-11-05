package com.intruder.dataserver.repository;

import com.intruder.dataserver.model.RecordSpgz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordSpgzRepository extends JpaRepository<RecordSpgz, Integer> {
}
