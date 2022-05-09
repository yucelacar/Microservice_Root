package com.intecon.invoiceextractoservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.intecon.invoiceextractoservice.entity.DocumentExtractionDetail;


@Repository
public interface DocumentExtractionDetailRepository extends MongoRepository<DocumentExtractionDetail, String>{

	public DocumentExtractionDetail findByDocumentId(String documentId);
}
