package com.intecon.outputmap.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.intecon.outputmap.model.OutputMapTemplate;


@Repository
public interface OutputMapTemplateRepository extends MongoRepository<OutputMapTemplate,Integer> {

	public List<OutputMapTemplate> findAll();
	public OutputMapTemplate findBySourceClass(String sourceClass);
}
