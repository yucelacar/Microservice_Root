package com.intecon.coreapi.events;

import com.intecon.coreapi.enums.DocumentStatus;

public class UpdatedExtractDocumentStatusEvent {
	
	public final String prepareId;
	public final String documentId;
	public final DocumentStatus docStatus;
	public final String docPath;
	
	public UpdatedExtractDocumentStatusEvent(String prepareId, String documentId, DocumentStatus docStatus, String docPath) {
		
		this.documentId = documentId;
		this.prepareId = prepareId;
		this.docStatus = docStatus;
		this.docPath = docPath;
	}
}
