package com.intecon.documentparser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.intecon.documentparser.service.impl.InvoiceParserSETDefaultImpl;
import com.intecon.documentparser.to.EditInvoiceRequest;
import com.intecon.documentparser.to.ExtractDocumentWithTemplateDTO;
import com.intecon.documentparser.utils.Utils;

@RestController
@RequestMapping("/documentParser")
public class DocumentParserController {

	@Autowired
	private InvoiceParserSETDefaultImpl invParser;
	
	@PostMapping("/parseDocumentByTemplate")
	public String parseDocumentByTemplate(@RequestBody ExtractDocumentWithTemplateDTO data){
		String result=null;
		try {
			//String tiffPath = ConverterService.converterPdfTotiff("D:\\Work\\NewVersion_Work\\Tubitak_Microservice\\temp\\DIM\\pdf\\broderick1.pdf");
			//InvoiceParserTestImpl.getSubImage("D:\\Work\\NewVersion_Work\\Tubitak_Microservice\\temp\\DIM\\pdf\\broderick1.tiff");
			EditInvoiceRequest envReq = invParser.prepareEditInvoiceRequest("fidnum", false, 0, 0, null, data.getDocumentTemplate(), "2222222222", data.getDocumentPath(), null);
			if(envReq != null) {
				result = Utils.convertObjectToString(envReq);
			}else {
				result = "ERROR : envReq is null"; 
			}
		}
		catch(Exception e) 
		{
			result = "ERROR : " + e.getMessage();
		}
		return result;
	}
}
