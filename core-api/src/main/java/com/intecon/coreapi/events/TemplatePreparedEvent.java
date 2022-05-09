package com.intecon.coreapi.events;

import com.intecon.coreapi.enums.DocumentStatus;

public class TemplatePreparedEvent {
	
	
	public final String templatePrepareId;
	public final String documentId;
	public final DocumentStatus docStatus;
	public final String docPath;
	public final String template;

	public TemplatePreparedEvent(String templatePrepareId, String documentId, DocumentStatus docStatus, String docPath, String template) {
		this.templatePrepareId = templatePrepareId;
		this.documentId = documentId;
		this.docStatus = docStatus;
		this.docPath = docPath;
		this.template = template;
	}
}
