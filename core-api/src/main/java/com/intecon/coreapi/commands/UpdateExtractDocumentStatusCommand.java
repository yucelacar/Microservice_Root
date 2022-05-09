package com.intecon.coreapi.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.intecon.coreapi.enums.DocumentStatus;

public class UpdateExtractDocumentStatusCommand {

	@TargetAggregateIdentifier
	public String prepareId;
	public String documentId;
	public DocumentStatus docStatus;
	public String docPath;
	
	
	public UpdateExtractDocumentStatusCommand(String prepareId, String documentId, DocumentStatus docStatus, String docPath) {
		this.documentId = documentId;
		this.prepareId = prepareId;
		this.docStatus = docStatus;
		this.docPath = docPath;
	}
}
