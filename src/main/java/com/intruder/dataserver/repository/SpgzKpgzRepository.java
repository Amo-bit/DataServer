package com.intruder.dataserver.repository;

import com.intruder.dataserver.model.SpgzKpgz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpgzKpgzRepository extends JpaRepository<SpgzKpgz, Integer> {
}
