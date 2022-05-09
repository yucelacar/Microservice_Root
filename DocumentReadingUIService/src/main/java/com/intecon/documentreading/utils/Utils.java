package com.intecon.documentreading.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.intecon.documentreading.model.DocumentDetail;
import com.intecon.documentreading.model.OutputMapTemplate;
import com.intecon.documentreading.model.ai.AiModel;
import com.intecon.documentreading.model.ai.File;
import com.intecon.documentreading.model.ai.Propery;
import com.intecon.documentreading.model.ai.Veriable;
import com.intecon.documentreading.model.ocr.Mapping;
import com.intecon.documentreading.model.ocr.OcrModel;
import com.intecon.documentreading.repository.OutputMapTemplateRepository;

@Service
public class Utils {

	@Autowired 
	private OutputMapTemplateRepository outputMapRepository;
	
	
	public static int calculateDistance(int start, int end) {
		return end - start;
	}
	

	public OcrModel convertDocumentDetailToOcrModel(List<DocumentDetail> documentDetailList) {
		OcrModel response = new OcrModel();
		response.setDate_format("dd/mm/yyyy");
		response.setSep_1000(",");
		response.setSep_decimal(".");
		response.setLine_first_col_tmi("inv_line_no");
		response.setLine_last_col_tmi("inv_line_total");
		response.setLine_end_tmi("inv_line_item");
		response.setIstotalfixed("1");
		
		List<Mapping> mappingList = new ArrayList<Mapping>();
		for(DocumentDetail docDetail : documentDetailList) {
			if(docDetail.getCLASSSEQ().equals("0")) {
				Mapping mapping = new Mapping();
				OutputMapTemplate outputMapTemplate = outputMapRepository.findBySourceClass(docDetail.getCLASSNAME());
				if(outputMapTemplate != null && outputMapTemplate.getTargetClass() != null && outputMapTemplate.getTargetClass().trim().length() > 0 ) {
					mapping.setFieldId(outputMapTemplate.getTargetClass());
				}else {
					mapping.setFieldId(docDetail.getCLASSNAME());
					
				}
				mapping.setFieldName(docDetail.getCLASSNAME());
				mapping.setLeft(docDetail.getCOORDX());
				mapping.setTop(docDetail.getCOORDY());
				int height = calculateDistance(Integer.parseInt(docDetail.getCOORDY()), Integer.parseInt(docDetail.getCOORDH()));
				mapping.setHeight(String.valueOf(height));
				int width = calculateDistance(Integer.parseInt(docDetail.getCOORDX()), Integer.parseInt(docDetail.getCOORDW()));
				mapping.setWidth(String.valueOf(width));
				mappingList.add(mapping);
			}
		}
		response.setMapping(mappingList);		
		return response;
	}
	
	public static OcrModel convertAiModelToOcrModel(AiModel aiModel) {
		OcrModel response = new OcrModel();
		if(aiModel.getFile()!= null && aiModel.getFile().size() > 1 ) {
			List<File> aiFile = aiModel.getFile();
			if(aiFile.get(0).getProperies() != null && aiFile.get(0).getProperies().get(0) != null) {
				Propery fileProperty = aiFile.get(0).getProperies().get(0);
				response.setTitle(fileProperty.getFileName());
			}
			if(aiFile.get(1).getVeriable() != null && aiFile.get(0).getVeriable().get(0) != null) {
				List<Veriable> fileVeriables = aiModel.getFile().get(0).getVeriable();
				List<Mapping> mappingList = new ArrayList<Mapping>();
				for(Veriable variable : fileVeriables) {
					Mapping mapping = new Mapping();
					mapping.setFieldId(variable.getClass_());
					mapping.setFieldName(variable.getClass_());
					mapping.setLeft(variable.getX1());
					mapping.setTop(variable.getY1());
					int height = calculateDistance(Integer.parseInt(variable.getY2()), Integer.parseInt(variable.getY1()));
					mapping.setHeight(String.valueOf(height));
					int width = calculateDistance(Integer.parseInt(variable.getX2()), Integer.parseInt(variable.getX1()));
					mapping.setWidth(String.valueOf(width));
					mappingList.add(mapping);
				}
				response.setMapping(mappingList);
			}
			
		}
		return response;
	}
	
	public static String convertObjectToString(Object object) {
		String response = "";
		Gson gson = null;
		try {
			gson = new Gson();
			response = gson.toJson(object);
		} catch (Exception e) {
			response= "Conversion failed!";
		}finally {
			gson = null;
		}
		return response;
	}
}
