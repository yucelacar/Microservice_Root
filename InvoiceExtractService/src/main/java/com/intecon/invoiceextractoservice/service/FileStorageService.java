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
public class FileStorageService {

    String filePath = "/home/common/documents/";

	@Value("${fastapi.url}")
	private String FASTAPI_URL;
	
    public String storeFile(MultipartFile file) {
        // Normalize file name
        //String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    	String fileName = file.getOriginalFilename();
       // String date = Calendar.getInstance().get(Calendar.YEAR) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1 ) + "/" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;
        String path = filePath  + "/" +  fileName;
        try {    
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }   
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = Paths.get(path);
            
            //Path targetLocation2 = this.fileStorageLocation.resolve(fileName.replace(".jpg", "-org.jpg"));
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            //Files.copy(file.getInputStream(), targetLocation2, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toAbsolutePath().toString();
        } catch (Exception ex) {
            //throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        	return "Error : " + ex.getMessage();
        }
    }
    
    public String copyFile(String source, String target) {

    	try {

        	Path sourceLocation = Paths.get(source);
        	Path targetLocation = Paths.get(target);
            Path tmp = Files.copy(sourceLocation, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return tmp.toAbsolutePath().toString();
		} catch (Exception e) {
			return "Copy Failed.";
		}
    }
}