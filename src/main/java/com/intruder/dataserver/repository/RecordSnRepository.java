package com.intruder.dataserver.repository;

import com.intruder.dataserver.model.RecordSn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordSnRepository extends JpaRepository<RecordSn, Integer> {
}
