package com.intruder.dataserver.repository;

import com.intruder.dataserver.model.RelationKeyWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationKeyWordRepository extends JpaRepository<RelationKeyWord, Integer> {
}
