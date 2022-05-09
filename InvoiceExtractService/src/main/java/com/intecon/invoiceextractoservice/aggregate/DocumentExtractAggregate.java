package com.intecon.invoiceextractoservice.aggregate;

import java.io.File;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;

import com.intecon.coreapi.commands.ExtractDocumentCommand;
import com.intecon.coreapi.commands.UpdateExtractDocumentStatusCommand;
import com.intecon.coreapi.enums.DocumentStatus;
import com.intecon.coreapi.events.DocumentExtractedEvent;
import com.intecon.coreapi.events.DocumentExtractionFailedEvent;
import com.intecon.coreapi.events.UpdatedExtractDocumentStatusEvent;
import com.intecon.invoiceextractoservice.entity.DocumentExtractionDetail;
import com.intecon.invoiceextractoservice.repository.DocumentExtractionDetailRepository;
import com.intecon.invoiceextractoservice.service.FileStorageService;
import com.intecon.invoiceextractoservice.service.InvoiceExtractionService;

@Aggregate
public class DocumentExtractAggregate {

	@AggregateIdentifier
	private String extractId;
	private String prepareId;
    private String documentId;
	public DocumentStatus docStatus;
	private String docPath;
    
	
    public DocumentExtractAggregate(){
    }

    @CommandHandler
    public DocumentExtractAggregate(ExtractDocumentCommand extractDocumentCommand, @Autowired DocumentExtractionDetailRepository documentExtractionDetailRepository, @Autowired InvoiceExtractionService invoiceExtractionService){
		System.out.println("Extract Document Command Handled.");
    	if(extractDocumentCommand.documentId == null || extractDocumentCommand.documentId.trim().length() == 0) {
        	System.out.println("Document Extraction Failed");
        	documentExtractionDetailRepository.save(new DocumentExtractionDetail(null, extractDocumentCommand.documentId, DocumentStatus.FAILED, extractDocumentCommand.docPath, null));
    		AggregateLifecycle.apply(new DocumentExtractionFailedEvent(extractDocumentCommand.extractId, extractDocumentCommand.prepareId, extractDocumentCommand.documentId, extractDocumentCommand.docStatus, extractDocumentCommand.docPath));
    	}else {
    		System.out.println("Document Extracting from = " + extractDocumentCommand.docPath);
    		try {
    			String outputPath = invoiceExtractionService.extractWithFastApi(new File(extractDocumentCommand.docPath), extractDocumentCommand.docPath.replace(".png", ".json"));
            	documentExtractionDetailRepository.save(new DocumentExtractionDetail(null, extractDocumentCommand.documentId, DocumentStatus.READ, extractDocumentCommand.docPath, outputPath));
            	AggregateLifecycle.apply(new DocumentExtractedEvent(extractDocumentCommand.extractId, extractDocumentCommand.prepareId, extractDocumentCommand.documentId, extractDocumentCommand.docStatus, extractDocumentCommand.docPath));
    		} catch (Exception e) {
				e.printStackTrace();
				AggregateLifecycle.apply(new DocumentExtractionFailedEvent(extractDocumentCommand.extractId, extractDocumentCommand.prepareId, extractDocumentCommand.documentId, extractDocumentCommand.docStatus, extractDocumentCommand.docPath));
			}
    	}
    }

    @EventSourcingHandler
    protected void on(DocumentExtractedEvent extractDocumentEvent){
        this.extractId = extractDocumentEvent.extractId;
        this.prepareId = extractDocumentEvent.prepareId;
        this.documentId = extractDocumentEvent.documentId;
        this.docStatus = extractDocumentEvent.docStatus;
		this.docPath = extractDocumentEvent.docPath;
    }
    
    @EventSourcingHandler
    protected void on(DocumentExtractionFailedEvent extractDocumentEvent){
        this.extractId = extractDocumentEvent.extractId;
        this.prepareId = extractDocumentEvent.prepareId;
        this.documentId = extractDocumentEvent.documentId;
        this.docStatus = extractDocumentEvent.docStatus;
		this.docPath = extractDocumentEvent.docPath;
    }
}
