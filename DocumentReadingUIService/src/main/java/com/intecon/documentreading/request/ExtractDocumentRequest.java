package com.intecon.documentreading.request;

public class ExtractDocumentRequest {
	String documentId;
	String documentType;
	
	
	public ExtractDocumentRequest(String documentId, String documentType) {
		this.documentId = documentId;
		this.documentType = documentType;
	}
	
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	
}
