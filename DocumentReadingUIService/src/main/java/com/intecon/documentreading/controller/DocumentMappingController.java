package com.intecon.documentreading.controller;


import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intecon.documentreading.model.DocumentDetail;
import com.intecon.documentreading.repository.DocumentDetailRepository;



@RestController
@RequestMapping("/documentMapping")
public class DocumentMappingController {

	@Autowired
	private DocumentDetailRepository documentDetailRepository;

	
	@GetMapping("/mappingDocument/{DOCID}")
	public String pI(@PathVariable("DOCID") String DOCID){
		try {
			System.out.println("DOCID:"+DOCID);
			List<DocumentDetail> listDocument = this.documentDetailRepository.findByDOCID(DOCID);
			URL url = new URL("http://api-gateway:9090/outputMap/mapDocument");
			HttpURLConnection httpClient = (HttpURLConnection) url.openConnection();
	        //HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection();

	        //add reuqest header
	        httpClient.setRequestMethod("POST");
	        httpClient.setRequestProperty("Content-Type", "application/json; utf-8");
	        httpClient.setRequestProperty("Accept", "application/json");

	        httpClient.setDoOutput(true);
	        ObjectMapper objectMapper = new ObjectMapper();
	        String requestBody = objectMapper
	                .writeValueAsString(listDocument);

	        try(OutputStream os = httpClient.getOutputStream()) {
	            byte[] input = requestBody.getBytes("utf-8");
	            os.write(input, 0, input.length);			
	        }
	        int responseCode = httpClient.getResponseCode();
	        System.out.println("ResponseCode:"+ responseCode);
	        String res = "RESPONSE_CODE:"+responseCode;
	        try(BufferedReader br = new BufferedReader(
	        		  new InputStreamReader(httpClient.getInputStream(), "utf-8"))) {
	        		    StringBuilder response = new StringBuilder();
	        		    String responseLine = null;
	        		    while ((responseLine = br.readLine()) != null) {
	        		        response.append(responseLine.trim());
	        		    }
	        		    res = response.toString();
	        		    System.out.println(response.toString());
	        		}
	        return res;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getLocalizedMessage();
		}
	}
}