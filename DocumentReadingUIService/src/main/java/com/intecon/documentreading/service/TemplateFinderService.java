package com.intecon.documentreading.service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.intecon.coreapi.commands.PrepareDocumentCommand;
import com.intecon.coreapi.enums.DocumentStatus;
import com.intecon.documentreading.dto.ExtractDocumentDTO;
import com.intecon.documentreading.model.DocumentExtractionDetail;
import com.intecon.documentreading.repository.DocumentExtractionDetailRepository;

@Service
public class TemplateFinderService {


	@Value("${file.common.template.path}")
	private String filePath;
	
	public String getExtractedDocument(String documentId) {
		if(documentId != null && documentId.trim().length() > 0 ) {
			try {
				return Files.readString(Path.of(filePath + documentId + ".json") , StandardCharsets.UTF_8);
			} catch (Exception e) {
				return "Unexpected error! Detail : " + e.getMessage();
			}
		}else {
			return "Template not found!";
		}
	}
	
}
