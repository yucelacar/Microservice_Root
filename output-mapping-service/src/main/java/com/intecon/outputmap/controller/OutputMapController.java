package com.intecon.outputmap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.intecon.outputmap.service.OutputMapService;
import com.intecon.outputmap.to.DocumentDetail;

@RestController
@RequestMapping("outputMap")
public class OutputMapController {

	@Autowired 
	private OutputMapService outputMapService;
	
//	@GetMapping("/document")
//	public String getDocument(@RequestBody DocumentDetail documentDetail){
//		return outputMapService.MapDocument(documentDetail);
//	}
	
	@GetMapping("/mapDocumentFilePath")
	public String getMapFile(@RequestBody String documentDetailPath){
		//return this.documentDetailRepository.findByDOCIDAndCLASSNAME(DOCID,"a");
		return outputMapService.MapFile(documentDetailPath);
		//return null;
		//return this.documentDetailRepository.findAll();
	}
	
	@PostMapping("/mapDocumentFile")
	public String getMapFile(@RequestParam("document") MultipartFile document, String documentType ) {
		 String path = outputMapService.storeFile(document);
		 return outputMapService.MapFile(path);
	}
	
	@PostMapping("/mapDocument")
	public String getMapFile(@RequestBody List<DocumentDetail> documentDetailList) {
		 return outputMapService.MapFileWithDocumentDetaiList(documentDetailList);
	}
	
	@PostMapping("/mapDocumentParam")
	public String getMapFileParam(@RequestBody String documentDetailList) {
		System.out.println("dsa:"+documentDetailList);
		return "SUCCESS";
		//return outputMapService.MapFileWithDocumentDetaiList(documentDetailList);
	}
}
