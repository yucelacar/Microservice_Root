package com.intecon.invoiceextractoservice.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.intecon.invoiceextractoservice.service.FileStorageService;



@RestController
@RequestMapping("/test")
public class TestController {


	@Autowired private FileStorageService fileStorageService;
	
    @RequestMapping(value = "/anonymous", method = RequestMethod.GET)
    public ResponseEntity<String> getAnonymous() {
    	/*try {
    		Thread.sleep(10000);
		} catch (Exception e) {
			// TODO: handle exception
		}*/
    	/*double x = 0.0001;
    	for(int i = 0; i < 1000000; i++) {
    		x += x*x;
    	}*/
        return ResponseEntity.ok("Hello Anonymous 3");
    }
    
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("Hello User");
    }

    
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ResponseEntity<String> getAdmin() {
        return ResponseEntity.ok("Hello Admin");
    }

    //@RolesAllowed({ "admin", "user" })
    @RequestMapping(value = "/all-user", method = RequestMethod.GET)
    public ResponseEntity<String> getAllUser() {
        return ResponseEntity.ok("Hello All User");
    }

	@PostMapping("/extractInvoice")
	public String uploadFile(@RequestParam("document") MultipartFile document, String documentType) {
		return "";
	}

}