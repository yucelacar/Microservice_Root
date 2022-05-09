package com.intecon.invoiceextractoservice.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.intecon.invoiceextractoservice.service.TemplateAdviceService;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("templateAdvice")
public class TemplateAdviceController {
	
	@Value("${file.common.invoice.path}")
	private String filePath;
	
	@Autowired TemplateAdviceService templateAdviceService;
	
	@PostMapping("/adviceTemplate")
	public String adviceTemplate(@RequestBody String documentId) {
		System.out.println("Image sent to fast api : " + filePath + documentId + ".png");
		return templateAdviceService.getTemplateAdviceFromFastApi(new File(filePath + documentId + ".png"));
	}
}
