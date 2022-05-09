package com.intecon.outputmap.service;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.intecon.outputmap.model.OutputMapTemplate;
import com.intecon.outputmap.repository.OutputMapTemplateRepository;
import com.intecon.outputmap.to.DocumentDetail;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.reflect.TypeToken;
@Service
public class OutputMapService {

	@Autowired 
	private OutputMapTemplateRepository outputMapRepository;
	
	
	@Value("${file.upload.path}")
	private String fileUploadPath;
//	public String MapFile(String fileDocumentDetailPath) {
//		
//		try {
//			Gson _gson = new Gson();
//			File documentDetailFile = new File(fileDocumentDetailPath);
//			String documentDetailValue = FileUtils.readFileToString(documentDetailFile);
//			 
//			Type documentDetailType = new TypeToken<ArrayList<DocumentDetail>>(){}.getType();
//			ArrayList<DocumentDetail> _documentDetails = _gson.fromJson(documentDetailValue, documentDetailType);  
//			
//			ObjectMapper mapper = new ObjectMapper();
//			Properties p = new Properties();
//			
//			for (DocumentDetail documentDetail : _documentDetails) {
//				OutputMapTemplate outputMapTemplate = outputMapRepository.findBySourceClass(documentDetail.getCLASSNAME());
//				System.out.println( outputMapTemplate.getTargerClass());
//				if(documentDetail.getCLASSNAME() != null && documentDetail.getCLASSNAME().trim().contains("LINE")) {
//					p.put
//				}else {
//					p.put(outputMapTemplate.getTargerClass(), documentDetail.getCLASSVALUE());
//				}
//			}
//			File adass = new File("D:\\Work\\NewVersion_Work\\Tubitak_Microservice\\temp\\outDoc.json");
//			mapper.writeValue(adass, p);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			// TODO: handle exception
//		}
//		return "";
//	}
	public String MapFile(String fileDocumentDetailPath) {
	
		try {
			Gson _gson = new Gson();
			File documentDetailFile = new File(fileDocumentDetailPath);
			String documentDetailValue = FileUtils.readFileToString(documentDetailFile);
			Type documentDetailType = new TypeToken<ArrayList<DocumentDetail>>(){}.getType();
			ArrayList<DocumentDetail> _documentDetails = _gson.fromJson(documentDetailValue, documentDetailType);  
			JSONObject jsonHeader = new JSONObject();
			JSONObject jsonLine = new JSONObject();
			for (DocumentDetail documentDetail : _documentDetails) {
				OutputMapTemplate outputMapTemplate = outputMapRepository.findBySourceClass(documentDetail.getCLASSNAME());
				//System.out.println( outputMapTemplate.getTargerClass());
				if(documentDetail.getCLASSNAME() != null && documentDetail.getCLASSNAME().trim().contains("LINE")) {
					jsonLine.put(outputMapTemplate.getTargerClass(), documentDetail.getCLASSVALUE());
				}else {
					jsonHeader.put(outputMapTemplate.getTargerClass(), documentDetail.getCLASSVALUE());
				}
			}
			jsonHeader.put("InvLines", jsonLine);
			File outDoc = new File(fileUploadPath + "/" +UUID.randomUUID().toString() +"_outDoc.json");
			String jsonStringData = jsonHeader.toString();
			FileUtils.writeStringToFile(outDoc, jsonStringData);
			System.out.println("FILE_PATH:"+outDoc.getAbsolutePath());
			return "SUCCESS PROCESS";
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR:"+e.getLocalizedMessage();
			// TODO: handle exception
		}
	
	}
	

    public String storeFile(MultipartFile file) {
        // Normalize file name
        //String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    	String fileName = file.getOriginalFilename();
       // String date = Calendar.getInstance().get(Calendar.YEAR) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1 ) + "/" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;
        String path = fileUploadPath  + "/" +  fileName;
        try {    
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }   
            Path targetLocation = Paths.get(path);
            
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("FilePath: "+targetLocation.toAbsolutePath().toString());
            return targetLocation.toAbsolutePath().toString();
        } catch (Exception ex) {
            //throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        	return "Error : " + ex.getMessage();
        }
    }
    public String MapFileWithDocumentDetaiList(List<DocumentDetail> _documentDetails) {    	
		try {			
			JSONObject jsonHeader = new JSONObject();
			
			HashMap<String, JSONObject> lineMapList = new HashMap<String, JSONObject>();
			for (DocumentDetail documentDetail : _documentDetails) {
				if(documentDetail != null && documentDetail.getCLASSVALUE() !=null && documentDetail.getCLASSNAME() != null ) {
					OutputMapTemplate outputMapTemplate = outputMapRepository.findBySourceClass(documentDetail.getCLASSNAME());
					//System.out.println( outputMapTemplate.getTargerClass());
					if(outputMapTemplate != null) {
						if(documentDetail.getCLASSNAME() != null && documentDetail.getCLASSNAME().trim().contains("LINE")) {
							if(lineMapList.containsKey(documentDetail.getCLASSSEQ())) {
								JSONObject jsonLine  = lineMapList.get(documentDetail.getCLASSSEQ());
								jsonLine.put(outputMapTemplate.getTargerClass(), documentDetail.getCLASSVALUE());
							}else {
								JSONObject jsonLine = new JSONObject();
								jsonLine.put(outputMapTemplate.getTargerClass(), documentDetail.getCLASSVALUE());
								lineMapList.put(documentDetail.getCLASSSEQ(), jsonLine);
							}
							
						}else {
							jsonHeader.put(outputMapTemplate.getTargerClass(), documentDetail.getCLASSVALUE());
						}
					}else {
						System.out.println("CLASSNAME NOT FOUND:"+documentDetail.getCLASSNAME());
					}
				}
			}
			List<JSONObject> lineList = new ArrayList<JSONObject>();
			for (JSONObject jsonLine : lineMapList.values()) {
				lineList.add(jsonLine);
			}
			jsonHeader.put("InvLines", lineList);
			File outDoc = new File(fileUploadPath + "/" +UUID.randomUUID().toString() +"_outDoc.json");
			String jsonStringData = jsonHeader.toString();
			FileUtils.writeStringToFile(outDoc, jsonStringData);
			System.out.println("DATA JSON:"+ jsonStringData);
			System.out.println("FILE_PATH:"+outDoc.getAbsolutePath());
			return "SUCCESS PROCESS";
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR:"+e.getLocalizedMessage();
			// TODO: handle exception
		}
	
	}
    
}
