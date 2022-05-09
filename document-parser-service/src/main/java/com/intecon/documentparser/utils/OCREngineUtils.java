package com.intecon.documentparser.utils;

import java.io.File;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.springframework.beans.factory.annotation.Value;

import com.intecon.documentparser.service.ws.client.OCREngineWSStub;


public class OCREngineUtils {

	@Value("${global.ocrws.context.path}")
	private static String globalOcrwsContextPath;
	
	//public static String ocrwsEndpoint;
	
	public static OCREngineWSStub ocrws;
	static {
		try {
			//ocrws = new OCREngineWSStub(DimConfig.getInstance().getPropertyValue("OCRENGINEWSLINK"));
			ocrws = new OCREngineWSStub("http://192.168.51.180:8080/OCREngineWS/services/OCREngineWS.OCREngineWSHttpSoap12Endpoint");
			//ocrws = new OCREngineWSStub(ocrwsEndpoint);
			ocrws._getServiceClient().getOptions().setTimeOutInMilliSeconds(20 * 60 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String convertPDFToTiff(String _fileName, String filetype) {
		String r = null;
		String _uuid = "";
		try {
			OCREngineWSStub.ConvertPDFToTIFF convt = new OCREngineWSStub.ConvertPDFToTIFF();
			OCREngineWSStub.DocumentTO doct = new OCREngineWSStub.DocumentTO();
			doct.setFileName(_fileName.substring(_fileName.lastIndexOf("/") + 1));
			OCREngineWSStub.Base64BinaryTO binf = new OCREngineWSStub.Base64BinaryTO();
			DataHandler dho = new DataHandler(new FileDataSource(_fileName));
			binf.setBase64Binary(dho);
			doct.setFileData(binf);
			doct.setDoctype(filetype);
			convt.setInf(doct);
			OCREngineWSStub.ConvertPDFToTIFFResponse resp = ocrws.convertPDFToTIFF(convt);
			OCREngineWSStub.DocumentTO doct2 = resp.get_return();
			_uuid = doct2.getDocRefUid();
			DataHandler dh = doct2.getFileData().getBase64Binary();
			Utils.writeInputStremToFile(dh.getInputStream(), _fileName.replace(".pdf", ".zip"));
			Utils.extractZipFile(_fileName.replace(".pdf", ".zip"), _fileName.replace(".pdf", "." + filetype));
			r = _fileName.replace(".pdf", "." + filetype);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (new File(globalOcrwsContextPath + "files/" + _uuid).exists() && !"".equals(_uuid)) {
				try {
					org.apache.commons.io.FileUtils.deleteDirectory(new File(globalOcrwsContextPath + "files/" + _uuid));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return r;
	}

	public static String runTesseract(String _fileName, String _params) {
		String r = null;
		try {
			File zipFile = new File(_fileName.replace(".tiff", ".zip"));
			Utils.createZipFile(zipFile, new File(_fileName));
			_fileName = _fileName.replace(".tiff", ".zip");
			OCREngineWSStub.RunTesseract convt = new OCREngineWSStub.RunTesseract();
			OCREngineWSStub.DocumentTO doct = new OCREngineWSStub.DocumentTO();
			doct.setFileName(zipFile.getName());
			OCREngineWSStub.Base64BinaryTO binf = new OCREngineWSStub.Base64BinaryTO();
			DataHandler dho = new DataHandler(new FileDataSource(_fileName));
			binf.setBase64Binary(dho);
			doct.setFileData(binf);
			convt.setInf(doct);
			convt.setParams(_params);
			OCREngineWSStub.RunTesseractResponse resp = ocrws.runTesseract(convt);
			r = resp.get_return();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	public static String runIMScripts(String _fileName, String _cargs) {
		String r = null;
		String _uuid = "";
		try {
			Utils.createZipFile(new File(_fileName.replace(".tiff", ".zip")), new File(_fileName));
			_fileName = _fileName.replace(".tiff", ".zip");
			OCREngineWSStub.RunIMScripts convt = new OCREngineWSStub.RunIMScripts();
			OCREngineWSStub.DocumentTO doct = new OCREngineWSStub.DocumentTO();
			doct.setFileName(_fileName.substring(_fileName.lastIndexOf("/") + 1));
			OCREngineWSStub.Base64BinaryTO binf = new OCREngineWSStub.Base64BinaryTO();
			DataHandler dho = new DataHandler(new FileDataSource(_fileName));
			binf.setBase64Binary(dho);
			doct.setFileData(binf);
			convt.setInf(doct);
			convt.setCargs(_cargs);
			OCREngineWSStub.RunIMScriptsResponse resp = ocrws.runIMScripts(convt);
			OCREngineWSStub.DocumentTO doct2 = resp.get_return();
			_uuid = doct2.getDocRefUid();
			DataHandler dh = doct2.getFileData().getBase64Binary();
			if (new File(_fileName).exists()) {
				new File(_fileName).delete();
			}
			Utils.writeInputStremToFile(dh.getInputStream(), _fileName);
			Utils.extractZipFile(_fileName, _fileName.replace(".zip", ".tiff"));
			r = _fileName;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (new File(globalOcrwsContextPath + "files/" + _uuid).exists() && !"".equals(_uuid)) {
				try {
					org.apache.commons.io.FileUtils.deleteDirectory(new File(globalOcrwsContextPath + "files/" + _uuid));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return r;
	}
}
