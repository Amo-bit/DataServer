package com.intruder.dataserver.repository;

import com.intruder.dataserver.model.RelationSnSpgz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationSnSpgzRepository extends JpaRepository<RelationSnSpgz, Integer> {
}
