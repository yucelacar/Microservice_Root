package com.intecon.sagas;

import java.util.UUID;

import javax.inject.Inject;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;

import com.intecon.coreapi.commands.ExtractDocumentCommand;
import com.intecon.coreapi.commands.UpdateExtractDocumentStatusCommand;
import com.intecon.coreapi.enums.DocumentStatus;
import com.intecon.coreapi.events.DocumentExtractedEvent;
import com.intecon.coreapi.events.DocumentPreparedEvent;
import com.intecon.coreapi.events.UpdatedExtractDocumentStatusEvent;



@Saga
public class DocumentExtractSaga {
	@Inject
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "prepareId")    
    public void handle(DocumentPreparedEvent documentPreparedEvent){
        String extarctId = UUID.randomUUID().toString();
		System.out.println("Document Prepared.");
		
        //associate Saga
        SagaLifecycle.associateWith("extractId", extarctId);
        
        //send the commands
		System.out.println("Send Extract Document Command -->");
        commandGateway.send(new ExtractDocumentCommand(extarctId, documentPreparedEvent.prepareId, documentPreparedEvent.documentId, DocumentStatus.PREPARED, documentPreparedEvent.docPath));
    }
    

    @SagaEventHandler(associationProperty = "extractId")
    public void handle(DocumentExtractedEvent documentExtractedEvent){

		System.out.println("Document Extracted.");
        try {
            //send the commands
    		System.out.println("Send Update Extract Document Status Command -->");
        	commandGateway.send(new UpdateExtractDocumentStatusCommand(documentExtractedEvent.prepareId, documentExtractedEvent.documentId, DocumentStatus.READ, documentExtractedEvent.docPath));	
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
    

    @SagaEventHandler(associationProperty = "prepareId")
    public void handle(UpdatedExtractDocumentStatusEvent UpdatedExtractDocumentStatusEvent){
		System.out.println("Extract Document Status Updated.");
    	System.out.println("Lifecycle End....");
        SagaLifecycle.end();
    }
    
    
}
