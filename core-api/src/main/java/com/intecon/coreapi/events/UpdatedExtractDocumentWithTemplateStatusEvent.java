package com.intecon.coreapi.events;

import com.intecon.coreapi.enums.DocumentStatus;

public class UpdatedExtractDocumentWithTemplateStatusEvent {
	
	public final String prepareId;
	public final String documentId;
	public final DocumentStatus docStatus;
	public final String docPath;
	public final String template;
	
	public UpdatedExtractDocumentWithTemplateStatusEvent(String prepareId, String documentId, DocumentStatus docStatus, String docPath, String template) {
		
		this.documentId = documentId;
		this.prepareId = prepareId;
		this.docStatus = docStatus;
		this.docPath = docPath;
		this.template = template;
	}
}
