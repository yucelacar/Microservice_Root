package com.intecon.documentreading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.intecon.documentreading.service.TemplateFinderService;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("templateFinder")
public class TemplateFinderController {

	@Autowired TemplateFinderService templateFinderService;
	
	@PostMapping("/findTemlate")
	public String findTemlate(@RequestBody String documentId) {
		String response = "";
		try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://api-gateway:9090/templateAdvice/adviceTemplate";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);

            HttpEntity<String> requestEntity = new HttpEntity<>(documentId, headers);

            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            System.out.println("Response: " + resp.getBody());
            if(resp != null && resp.getStatusCode().equals(HttpStatus.OK) && resp.getBody() != null) {
            	response = templateFinderService.getExtractedDocument(resp.getBody());
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = "Template not found!"; 
        }
		return response;
	}	
}
