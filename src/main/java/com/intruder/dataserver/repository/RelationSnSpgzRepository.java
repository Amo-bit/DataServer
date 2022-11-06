package com.intruder.dataserver.repository;

import com.intruder.dataserver.model.RelationSnSpgz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationSnSpgzRepository extends JpaRepository<RelationSnSpgz, Integer> {
    RelationSnSpgz findALLByCodeWork(String codeName);
    List<RelationSnSpgz> findALLByNameWorkContains(String codeName);
    int findIdSpgzByCodeWork(String codeWork);
}
