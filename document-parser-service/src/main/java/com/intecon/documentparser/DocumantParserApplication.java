package com.intecon.documentparser;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import com.intecon.documentparser.utils.OCREngineUtils;


@SpringBootApplication
public class DocumantParserApplication {
	
	//@Value("${global.ocrws.url}")
	//private static String ocrwsUrl;

	public static void main(String[] args) {
		//if(OCREngineUtils.ocrwsEndpoint == null) OCREngineUtils.ocrwsEndpoint = ocrwsUrl;
		SpringApplication.run(DocumantParserApplication.class, args);
	}
}
