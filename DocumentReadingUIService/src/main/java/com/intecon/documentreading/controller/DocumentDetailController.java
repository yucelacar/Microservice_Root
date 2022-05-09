package com.intecon.documentreading.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intecon.documentreading.model.DocumentDetail;
import com.intecon.documentreading.repository.DocumentDetailRepository;

import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;


@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("documentDetail")
public class DocumentDetailController {
	@Autowired
	private DocumentDetailRepository documentDetailRepository;
	
	@GetMapping("/list/{DOCID}")
	public List<DocumentDetail> getAll(@PathVariable("DOCID") String DOCID){
		//return this.documentDetailRepository.findByDOCIDAndCLASSNAME(DOCID,"a");
		return this.documentDetailRepository.findByDOCID(DOCID);
		//return null;
		//return this.documentDetailRepository.findAll();
	}
	
	@PostMapping("/add")
	public String addDD(@RequestBody String[][] data){
		String result=null;
		try {
			for(int i=0;i<data.length;i++) {
				long id = 0;
				List<DocumentDetail> DDs2 = documentDetailRepository.findAll(Sort.by(Sort.Direction.DESC, "DOCDETAILID"));
				
				for(DocumentDetail d:DDs2) {
					id=d.getDOCDETAILID();
					break;
				}
					DocumentDetail d1 =new DocumentDetail(id+1, data[i][0], data[i][1], data[i][2], data[i][3], data[i][4], data[i][5], data[i][6], data[i][7], "true");
					documentDetailRepository.save(d1);
					result="SUCCESS";
				} 
		}
		catch(Exception e) 
		{
			result="ERROR";
		}
		return result;
	}
	
	@GetMapping("/delete/{DOCID}")
	public List<DocumentDetail> deleteDD(@PathVariable("DOCID") String DOCID){
		documentDetailRepository.deleteByDOCID(DOCID);
		return null;
	}
	
}
