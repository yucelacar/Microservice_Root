package com.intecon.coreapi.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.intecon.coreapi.enums.DocumentStatus;


public class PrepareTemplateCommand {

	@TargetAggregateIdentifier
	public String templatePrepareId;
	public String documentId;
	public DocumentStatus docStatus;
	
	
	public PrepareTemplateCommand(String templatePrepareId, String documentId, DocumentStatus docStatus) {
		this.templatePrepareId = templatePrepareId;
		this.documentId = documentId;
		this.docStatus = docStatus;
	}
	
	
	
}
