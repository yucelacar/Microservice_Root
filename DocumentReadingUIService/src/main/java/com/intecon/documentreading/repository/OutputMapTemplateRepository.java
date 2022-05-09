package com.intecon.documentreading.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.intecon.documentreading.model.OutputMapTemplate;


@Repository
public interface OutputMapTemplateRepository extends MongoRepository<OutputMapTemplate,String> {

	public List<OutputMapTemplate> findAll();
	public OutputMapTemplate findBySourceClass(String sourceClass);
}
