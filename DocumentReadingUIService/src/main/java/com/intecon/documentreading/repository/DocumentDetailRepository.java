package com.intecon.documentreading.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.intecon.documentreading.model.DocumentDetail;

@Repository
public interface DocumentDetailRepository extends MongoRepository<DocumentDetail, Long> {
	
	public List<DocumentDetail> findAll();
	public List<DocumentDetail> findByDOCID(String DOCID);
	public void deleteByDOCID(String DOCID);
}
