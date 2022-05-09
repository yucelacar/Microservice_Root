package com.intecon.documentreading.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.intecon.documentreading.dto.ExtractDocumentDTO;
import com.intecon.documentreading.request.ExtractDocumentRequest;
import com.intecon.documentreading.service.DocumentExtractService;



@RestController
@RequestMapping("/documentExtraction")
public class DocumentExtractController {


	@Autowired DocumentExtractService documentExtractService;

	@PostMapping("/extractDocument")
	public String extractDocument(@RequestBody ExtractDocumentRequest req) {
		ExtractDocumentDTO extractDocumentDTO = new ExtractDocumentDTO();
		extractDocumentDTO.setDocumentId(req.getDocumentId());
		extractDocumentDTO.setDocumentType(req.getDocumentType());

		System.out.println("Extract Document Request for " + req.getDocumentId());
		documentExtractService.prepareDocument(extractDocumentDTO);
		return "File Porcessing";
	}

	@PostMapping("/extractDocumentWithTemplate")
	public String extractDocumentWithTemplate(@RequestBody ExtractDocumentRequest req) {
		ExtractDocumentDTO extractDocumentDTO = new ExtractDocumentDTO();
		extractDocumentDTO.setDocumentId(req.getDocumentId());
		extractDocumentDTO.setDocumentType(req.getDocumentType());

		System.out.println("Extract Document With Template Request for " + req.getDocumentId());
		return documentExtractService.extractDocumentWithTemplate(extractDocumentDTO);
	}
	

	@PostMapping("/getExtractedDocument")
	public String getExtractedDocument(@RequestBody String documentId) {
		return documentExtractService.getExtractedDocument(documentId);
	}	
}