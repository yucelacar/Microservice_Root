package com.intecon.documentreading.service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.intecon.coreapi.commands.PrepareDocumentCommand;
import com.intecon.coreapi.enums.DocumentStatus;
import com.intecon.documentreading.dto.ExtractDocumentDTO;
import com.intecon.documentreading.dto.ExtractDocumentWithTemplateDTO;
import com.intecon.documentreading.model.DocumentDetail;
import com.intecon.documentreading.model.DocumentExtractionDetail;
import com.intecon.documentreading.model.EInvReq.EditInvoiceRequest;
import com.intecon.documentreading.model.EInvReq.GenericDataItem;
import com.intecon.documentreading.model.ocr.OcrModel;
import com.intecon.documentreading.repository.DocumentDetailRepository;
import com.intecon.documentreading.repository.DocumentExtractionDetailRepository;
import com.intecon.documentreading.utils.Utils;

@Service
public class DocumentExtractService {
	
	private final CommandGateway commandGateway;
	
	@Autowired DocumentExtractionDetailRepository documentExtractionDetailRepository;
	@Autowired DocumentDetailRepository documentDetailRepository;
	@Autowired Utils utils;
	@Value("${file.common.invoice.path}")
	private String filePath;
	
    public DocumentExtractService(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }
  
	public CompletableFuture<String> prepareDocument(ExtractDocumentDTO extractDocumentDTO) {
		try {
			System.out.println("Send Prepare Document Command -->");
			return commandGateway.send(new PrepareDocumentCommand(UUID.randomUUID().toString(),
	        		extractDocumentDTO.getDocumentId(), DocumentStatus.INPROGRESS));
		} catch (Exception e) {
			e.printStackTrace();
	        return null;
		}
	}

	public String getExtractedDocument(String documentId) {
		DocumentExtractionDetail ded = documentExtractionDetailRepository.findByDocumentId(documentId);
		if(ded != null && ded.getDocStatus() != null && ded.getOutputPath() != null ) {
			if( ded.getDocStatus().equals(DocumentStatus.READ)) {
				try {
					return Files.readString(Path.of(ded.getOutputPath()) , StandardCharsets.UTF_8);
				} catch (Exception e) {
					return "Unexpected error! Detail : " + e.getMessage();
				}
			}else {
				return "Document not processed yet. Current status: " + ded.getDocStatus().toString();
			}
		}else {
			return "Document not found!";
		}
	}

	public String extractDocumentWithTemplate(ExtractDocumentDTO extractDocumentDTO) {
		OcrModel ocrModel;
		String response = "";
		try {
			List<DocumentDetail> documentDetailList = documentDetailRepository.findByDOCID(extractDocumentDTO.getDocumentId());
			ocrModel = utils.convertDocumentDetailToOcrModel(documentDetailList);
			ExtractDocumentWithTemplateDTO extractDocumentWithTemplateDTO = new ExtractDocumentWithTemplateDTO();
			extractDocumentWithTemplateDTO.setDocumentPath(extractDocumentDTO.getDocumentId() + ".pdf");
			extractDocumentWithTemplateDTO.setDocumentTemplate(Utils.convertObjectToString(ocrModel));
			//response = Utils.convertObjectToString(extractDocumentWithTemplateDTO);
			EditInvoiceRequest einvreq = getExtractedDocumentFromOcrService(extractDocumentWithTemplateDTO);
			GenericDataItem gdi = null;
			for(DocumentDetail dd: documentDetailList) {
				switch (dd.getCLASSNAME()) {
				case "SUPP_NAME":
					dd.setCLASSVALUE(einvreq.getCustomer_name());
					break;
				case "SUPPLIER_ADDRESS":
					dd.setCLASSVALUE(einvreq.getCust_street() + " "  + einvreq.getCust_city());
					break;
				case "SUPPLIER_VAT_NUMBER":
					dd.setCLASSVALUE(einvreq.getCustomer_taxn());
					break;
				case "INVOICE_DATE":
					dd.setCLASSVALUE(einvreq.getIssue_date());
					break;
				case "INVOICE_SUMMARY_VAT":
					dd.setCLASSVALUE(String.valueOf(einvreq.getTotal_kdv()));
					break;
				case "INVOICE_SUMMARY_SUBTOTAL":
					dd.setCLASSVALUE(String.valueOf(einvreq.getTotal_amnt()));
					break;
				case "INVOICE_SUMMARY_TOTAL":
					dd.setCLASSVALUE(String.valueOf(einvreq.getTotal_final()));
					break;
				case "INVOICE_LINE_ITEM_DESCRIPTION":
					gdi = null;
					if(einvreq.getLine_list() != null && einvreq.getLine_list().size() > Integer.parseInt(dd.getCLASSSEQ()) ) {
						gdi = einvreq.getLine_list().get(Integer.parseInt(dd.getCLASSSEQ()));
						dd.setCLASSVALUE(gdi.getItem());
					}
					gdi = null;
					break;
				case "INVOICE_LINE_ITEM_NO":
					gdi = null;
					if(einvreq.getLine_list() != null && einvreq.getLine_list().size() > Integer.parseInt(dd.getCLASSSEQ()) ) {
						gdi = einvreq.getLine_list().get(Integer.parseInt(dd.getCLASSSEQ()));
						dd.setCLASSVALUE(String.valueOf(gdi.getId()));
						//TODO: Degistirilecek....!!!
					}
					gdi = null;
					break;
				case "INVOICE_LINE_QUANTITY":
					gdi = null;
					if(einvreq.getLine_list() != null && einvreq.getLine_list().size() > Integer.parseInt(dd.getCLASSSEQ()) ) {
						gdi = einvreq.getLine_list().get(Integer.parseInt(dd.getCLASSSEQ()));
						dd.setCLASSVALUE(String.valueOf(gdi.getQty()));
					}
					gdi = null;
					break;
				case "INVOICE_LINE_UNITPRICE":
					gdi = null;
					if(einvreq.getLine_list() != null && einvreq.getLine_list().size() > Integer.parseInt(dd.getCLASSSEQ()) ) {
						gdi = einvreq.getLine_list().get(Integer.parseInt(dd.getCLASSSEQ()));
						dd.setCLASSVALUE(String.valueOf(gdi.getPrice()));
					}
					gdi = null;
					break;
				case "INVOICE_LINE_VAT_RATE":
					gdi = null;
					if(einvreq.getLine_list() != null && einvreq.getLine_list().size() > Integer.parseInt(dd.getCLASSSEQ()) ) {
						gdi = einvreq.getLine_list().get(Integer.parseInt(dd.getCLASSSEQ()));
						dd.setCLASSVALUE(String.valueOf(gdi.getTaxp()));
					}
					gdi = null;
					break;
				case "INVOICE_LINE_SUBTOTAL":
					gdi = null;
					if(einvreq.getLine_list() != null && einvreq.getLine_list().size() > Integer.parseInt(dd.getCLASSSEQ()) ) {
						gdi = einvreq.getLine_list().get(Integer.parseInt(dd.getCLASSSEQ()));
						dd.setCLASSVALUE(String.valueOf(gdi.getAmnt()));
					}
					gdi = null;
					break;
				default:
					break;
				}
			}
			response = Utils.convertObjectToString(documentDetailList);
		} catch (Exception e) {
			e.printStackTrace();
			response = "Extraction Failed!";
		}finally {
			ocrModel = null;
		}
		return response;
	}

	public EditInvoiceRequest getExtractedDocumentFromOcrService(ExtractDocumentWithTemplateDTO extractDocumentWithTemplateDTO) throws Exception {
		EditInvoiceRequest response = null;
		try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://api-gateway:9090/documentParser/parseDocumentByTemplate";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> requestEntity = new HttpEntity<>(extractDocumentWithTemplateDTO, headers);
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            System.out.println("Response: " + resp.getBody());
            if(resp != null && resp.getStatusCode().equals(HttpStatus.OK) && resp.getBody() != null) {
    			Gson gson = new Gson();
            	response = gson.fromJson(resp.getBody(), EditInvoiceRequest.class);
            	gson = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Extraction Failed");
        }
		return response;
	}

	
}
