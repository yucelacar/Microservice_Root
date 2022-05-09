package com.intecon.invoiceextractoservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

@Service
public class InvoiceExtractionService {

	@Value("${fastapi.url}")
	private String FASTAPI_URL;

	public String extractWithFastApi(File file, String outputPath) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String url = FASTAPI_URL + "upload";
		HttpMethod requestMethod = HttpMethod.POST;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		
		MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
		ContentDisposition contentDisposition = ContentDisposition
		        .builder("form-data")
		        .name("file")
		        .filename(file.getName())
		        .build();
		
		fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
		HttpEntity<byte[]> fileEntity = new HttpEntity<>(Files.readAllBytes(file.toPath()), fileMap);
		
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", fileEntity);
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		
		ResponseEntity<String> response = restTemplate.exchange(url, requestMethod, requestEntity, String.class);
		
		System.out.println("file upload status code: " + response.getStatusCode());
		Files.writeString(Path.of(outputPath), response.getBody());
		return outputPath;
	}
}