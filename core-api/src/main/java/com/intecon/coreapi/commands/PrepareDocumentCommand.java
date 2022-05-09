package com.intecon.coreapi.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.intecon.coreapi.enums.DocumentStatus;


public class PrepareDocumentCommand {

	@TargetAggregateIdentifier
	public String prepareId;
	public String documentId;
	public DocumentStatus docStatus;
	
	
	public PrepareDocumentCommand(String prepareId, String documentId, DocumentStatus docStatus) {
		this.prepareId = prepareId;
		this.documentId = documentId;
		this.docStatus = docStatus;
	}
	
	
	
}
