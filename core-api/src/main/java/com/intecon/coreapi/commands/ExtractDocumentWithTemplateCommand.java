package com.intecon.coreapi.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.intecon.coreapi.enums.DocumentStatus;

public class ExtractDocumentWithTemplateCommand {

	@TargetAggregateIdentifier
	public String extractId;
	public String prepareId;
	public String documentId;
	public DocumentStatus docStatus;
	public String docPath;
	public final String template;
	
	
	public ExtractDocumentWithTemplateCommand(String extractId, String prepareId, String documentId, DocumentStatus docStatus, String docPath, String template) {
		this.extractId = extractId;
		this.documentId = documentId;
		this.prepareId = prepareId;
		this.docStatus = docStatus;
		this.docPath = docPath;
		this.template = template;
	}
}
