package com.intecon.coreapi.events;

import com.intecon.coreapi.enums.DocumentStatus;

public class DocumentExtractionFailedEvent {

	public final String extractId;
	public final String prepareId;
	public final String documentId;
	public final DocumentStatus docStatus;
	public final String docPath;
	
	
	public DocumentExtractionFailedEvent(String extractId, String prepareId, String documentId, DocumentStatus docStatus, String docPath) {
		this.extractId = extractId;
		this.documentId = documentId;
		this.prepareId = prepareId;
		this.docStatus = docStatus;
		this.docPath = docPath;
	}
}
