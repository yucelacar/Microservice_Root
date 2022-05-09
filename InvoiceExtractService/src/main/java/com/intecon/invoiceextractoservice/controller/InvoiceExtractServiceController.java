package com.intecon.invoiceextractoservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.intecon.invoiceextractoservice.service.FileStorageService;



@RestController
@RequestMapping("/InvoiceExtraction")
public class InvoiceExtractServiceController {


	@Autowired private FileStorageService fileStorageService;
	
    
	@PostMapping("/extractInvoice")
	public String uploadFile(@RequestParam("document") MultipartFile document, String documentType) {
		return fileStorageService.storeFile(document);
	}

}