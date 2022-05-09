package com.intecon.documentreading.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;
import com.intecon.coreapi.commands.PrepareDocumentCommand;
import com.intecon.coreapi.commands.UpdateExtractDocumentStatusCommand;
import com.intecon.coreapi.enums.DocumentStatus;
import com.intecon.coreapi.events.DocumentPreparedEvent;
import com.intecon.coreapi.events.UpdatedExtractDocumentStatusEvent;
import com.intecon.documentreading.model.DocumentExtractionDetail;
import com.intecon.documentreading.model.DocumentHeader;
import com.intecon.documentreading.repository.DocumentExtractionDetailRepository;
import com.intecon.documentreading.repository.DocumentHeaderRepository;

@Aggregate
public class PrepareDocumentAggregate {

	@AggregateIdentifier
	private String prepareId;
    private String documentId;
	private DocumentStatus docStatus;
	private String docPath;
    

	private String filePath = "/home/common/documents/invoices/";
	
    public PrepareDocumentAggregate(){
    }

    @CommandHandler
    public PrepareDocumentAggregate(PrepareDocumentCommand prepareDocumentCommand, @Autowired DocumentExtractionDetailRepository documentExtractionDetailRepository, @Autowired DocumentHeaderRepository documentHeaderRepository){

		try {
			System.out.println("Prepare Document Command Handled.");
			System.out.println("Document Preparing...");
			String docPath = filePath + prepareDocumentCommand.documentId + ".png";
			documentExtractionDetailRepository.save(new DocumentExtractionDetail(null, prepareDocumentCommand.documentId, prepareDocumentCommand.docStatus,docPath, docPath.replace(".png", ".json")));
			DocumentHeader dh = documentHeaderRepository.findByDOCID(Long.parseLong(prepareDocumentCommand.documentId));
			if(dh != null) {
				dh.setDOCSTATUS(prepareDocumentCommand.docStatus.toString());
				documentHeaderRepository.save(dh);
				dh = null;
			}
			AggregateLifecycle.apply(new DocumentPreparedEvent(prepareDocumentCommand.prepareId, prepareDocumentCommand.documentId, prepareDocumentCommand.docStatus, docPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @EventSourcingHandler
    protected void on(DocumentPreparedEvent documentPreparedEvent){
        this.prepareId = documentPreparedEvent.prepareId;
        this.documentId = documentPreparedEvent.documentId;
        this.docStatus = documentPreparedEvent.docStatus;
		this.docPath = documentPreparedEvent.docPath;
    }
    


    @CommandHandler
    public void on(UpdateExtractDocumentStatusCommand updateExtractDocumentStatusCommand, @Autowired DocumentExtractionDetailRepository documentExtractionDetailRepository, @Autowired DocumentHeaderRepository documentHeaderRepository){

		System.out.println("Update Extract Document Status Command Handled.");
		System.out.println("Update Extract Document Path = " + updateExtractDocumentStatusCommand.docPath);
		System.out.println("Extract Document Status Updating...");
		try {
			DocumentExtractionDetail ded = documentExtractionDetailRepository.findByDocumentId(updateExtractDocumentStatusCommand.documentId);

			if(ded != null) {
				ded.setDocStatus(updateExtractDocumentStatusCommand.docStatus);
				System.out.println("Object Id : " + ded.get_id());
				documentExtractionDetailRepository.save(ded);
			}else {
				System.out.println("Document not found!");
			}
			DocumentHeader dh = documentHeaderRepository.findByDOCID(Long.parseLong(updateExtractDocumentStatusCommand.documentId));
			if(dh != null) {
				dh.setDOCSTATUS(updateExtractDocumentStatusCommand.docStatus.toString());
				documentHeaderRepository.save(dh);
				dh = null;
			}
			AggregateLifecycle.apply(new UpdatedExtractDocumentStatusEvent(updateExtractDocumentStatusCommand.prepareId, updateExtractDocumentStatusCommand.documentId, updateExtractDocumentStatusCommand.docStatus, updateExtractDocumentStatusCommand.docPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @EventSourcingHandler
    protected void on(UpdatedExtractDocumentStatusEvent updatedExtractDocumentStatusEvent){
        this.prepareId = updatedExtractDocumentStatusEvent.prepareId;
        this.documentId = updatedExtractDocumentStatusEvent.documentId;
        this.docStatus = updatedExtractDocumentStatusEvent.docStatus;
        this.docPath = updatedExtractDocumentStatusEvent.docPath;
    }
    
   
    
}