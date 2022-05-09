package com.intecon.coreapi.events;

import com.intecon.coreapi.enums.DocumentStatus;

public class DocumentPreparedEvent {
	
	
	public final String prepareId;
	public final String documentId;
	public final DocumentStatus docStatus;
	public final String docPath;

	public DocumentPreparedEvent(String prepareId, String documentId, DocumentStatus docStatus, String docPath) {
		this.prepareId = prepareId;
		this.documentId = documentId;
		this.docStatus = docStatus;
		this.docPath = docPath;
	}
}
