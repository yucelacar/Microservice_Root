package com.intecon.documentparser.service;

import java.util.List;

import com.intecon.documentparser.to.EditInvoiceRequest;
import com.intecon.documentparser.to.GenericReturnTO;


public interface InvoiceParser {
	public EditInvoiceRequest prepareEditInvoiceRequest(String fid, boolean setOffset, int offsetTop, int offsetLeft,List<Integer> pageNumbers,String templete,String supplierName,String filePath,String comp);
	public GenericReturnTO parseSelectedArea(String fid, String templateId, String servlet_context_path, int top, int left, int width, int height);
	//Object 0=okunan vergi no , Object 1=List<Tfdim003>
	public Object[] findTemplate(String invoiceFilePath);
}
