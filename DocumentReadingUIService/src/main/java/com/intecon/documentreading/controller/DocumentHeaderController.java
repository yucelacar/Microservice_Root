package com.intecon.documentreading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intecon.documentreading.model.DocumentHeader;
import com.intecon.documentreading.repository.DocumentHeaderRepository;


@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("documentHeader")
public class DocumentHeaderController {
	@Autowired
	private DocumentHeaderRepository documentHeaderRepository;
	
	@GetMapping("/list")
	public List<DocumentHeader> getAll(){
		return this.documentHeaderRepository.findAll();
	}
	
	/*@GetMapping("/getAllFilter/{COMPID}-{SENDERNAME}-{DOCSTATUS1}-{DOCSTATUS2}-{DOCSTATUS3}")
	public List<DocumentHeader> getAllFilter(@PathVariable("COMPID") String COMPID, @PathVariable("SENDERNAME") String SENDERNAME, @PathVariable("DOCSTATUS1") String DOCSTATUS1, @PathVariable("DOCSTATUS2") String DOCSTATUS2, @PathVariable("DOCSTATUS3") String DOCSTATUS3){
		return this.documentHeaderRepository.findByCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseInOrCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseInOrCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseIn(COMPID, SENDERNAME, DOCSTATUS1, COMPID, SENDERNAME, DOCSTATUS2, COMPID, SENDERNAME, DOCSTATUS3);
	}*/
	@GetMapping("/getAllFilter/{COMPID}-{SENDERNAME}-{DOCSTATUS1}-{DOCSTATUS2}-{DOCSTATUS3}-{DOCSTATUS4}-{DOCSTATUS5}")
	public List<DocumentHeader> getAllFilter(@PathVariable("COMPID") String COMPID, @PathVariable("SENDERNAME") String SENDERNAME, @PathVariable("DOCSTATUS1") String DOCSTATUS1, @PathVariable("DOCSTATUS2") String DOCSTATUS2, @PathVariable("DOCSTATUS3") String DOCSTATUS3, @PathVariable("DOCSTATUS4") String DOCSTATUS4, @PathVariable("DOCSTATUS5") String DOCSTATUS5){
		return this.documentHeaderRepository.findByCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseInOrCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseInOrCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseInOrCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseInOrCOMPIDIgnoreCaseInAndSENDERNAMEIgnoreCaseInAndDOCSTATUSIgnoreCaseIn(COMPID, SENDERNAME, DOCSTATUS1, COMPID, SENDERNAME, DOCSTATUS2, COMPID, SENDERNAME, DOCSTATUS3, COMPID, SENDERNAME, DOCSTATUS4, COMPID, SENDERNAME, DOCSTATUS5);
	}
	
	@GetMapping("/pI")
	public String pI(){
		return "a";
	}
	
}
