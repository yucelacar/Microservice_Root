package com.intecon.invoiceextractoservice.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.intecon.coreapi.enums.DocumentStatus;

@Entity
@Document(collection = "DocumentExtractionDetail")
public class DocumentExtractionDetail {

	@Id
	String _id;
	
	private String documentId;
	private DocumentStatus docStatus;
	private String docPath;
	private String outputPath;
	

	public DocumentExtractionDetail(String _id, String documentId, DocumentStatus docStatus, String docPath, String outputPath) {
		this._id = _id;
		this.documentId = documentId;
		this.docStatus = docStatus;
		this.docPath = docPath;
		this.outputPath = outputPath;
	}
	

	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public DocumentStatus getDocStatus() {
		return docStatus;
	}
	public void setDocStatus(DocumentStatus docStatus) {
		this.docStatus = docStatus;
	}
	public String getDocPath() {
		return docPath;
	}
	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}
	public String getOutputPath() {
		return outputPath;
	}
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}	
}
