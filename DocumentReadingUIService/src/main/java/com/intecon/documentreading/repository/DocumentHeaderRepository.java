package com.intecon.documentreading.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.intecon.documentreading.model.DocumentHeader;

@Repository
public interface DocumentHeaderRepository extends MongoRepository<DocumentHeader, String>{
	
	public List<DocumentHeader> findAll();
	public List<DocumentHeader> findByCOMPID(String COMPID);
	//public List<DocumentHeader> findBySENDERNAMEIgnoreCaseIn(String string);
	//public List<DocumentHeader> findByCRTTSTRegexAndSENDERNAMERegex(String DOCID, String SENDERNAME);
	public List<DocumentHeader> findByCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseInOrCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseInOrCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseIn(String COMPID1, String SENDERNAME1, String STATUS1, String COMPID2, String SENDERNAME2, String STATUS2, String COMPID3, String SENDERNAME3, String STATUS3);
	public DocumentHeader findByDOCID(long DOCID);
	public List<DocumentHeader> findByCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseInOrCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseInOrCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseInOrCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseInOrCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseIn(String COMPID1, String SENDERNAME1, String STATUS1, String COMPID2, String SENDERNAME2, String STATUS2, String COMPID3, String SENDERNAME3, String STATUS3, String COMPID4, String SENDERNAME4, String STATUS4, String COMPID5, String SENDERNAME5, String STATUS5);
}
