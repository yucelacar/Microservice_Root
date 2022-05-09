/**
 * 
 */
package com.intecon.documentparser.service.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.intecon.documentparser.service.InvoiceParser;
import com.intecon.documentparser.to.EditInvoiceRequest;
import com.intecon.documentparser.to.GenericDataItem;
import com.intecon.documentparser.to.GenericReturnTO;
import com.intecon.documentparser.to.OCRTemplateMapItem;
import com.intecon.documentparser.to.OCRTemplateRequest;
import com.intecon.documentparser.utils.DimConfig;
import com.intecon.documentparser.utils.OCREngineUtils;
import com.intecon.documentparser.utils.OCRUtils;
import com.intecon.documentparser.utils.Utils;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;


/**
 * @author baris.deveci
 *
 */
@Service
public class InvoiceParserSETDefaultImpl {
	
	@Value("${global.context.path}")
	private String globalContextPath;
	
	private static int OSTYPE = 1;// 1=WIN,2=LINUX
	
	private int find_templ_try = 1;
	private String INVOICECONFPATH = globalContextPath + "V2/files/dimdata/diminvoice.conf";

	static {
		if (System.getProperty("os.name").toLowerCase().indexOf("win") > -1) {
			// OSTYPE=1;
		}
		if (OSTYPE == 2) {
			//TESSBINPATH disabled start
			//DimService.TESSBINPATH = "";
			//TESSBINPATH disabled end
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.eintecon.service.InvoiceParser#prepareEditInvoiceRequest(java.lang .String, java.lang.String, com.eintecon.model.Tfedf000, java.lang.String)
	 */
	
	public EditInvoiceRequest prepareEditInvoiceRequest(String fid, boolean setOffset, int offsetTop,
			int offsetLeft,List<Integer> pageNumbers,String template, String supplierTax,String fileName,String comp) {
		ImageIO.scanForPlugins();
		// System.out.println("ID: " + fid + ", TEMPLATEID: " + templateId + ", SETOFFSET: " + setOffset + ", TOP: " + offsetTop + ", LEFT: " + offsetLeft);
		EditInvoiceRequest einvreq = null;

		//DimService disabled start
		//DimService dimsrv = null;
		//DimService disabled end
		
		java.io.FileOutputStream fos = null;
		
		try { 
			FileUtils.writeStringToFile(new File(globalContextPath+"logs/log_"+Utils.getToday()+".log")
				, "invPart_fn: :start:"+new java.sql.Timestamp(new java.util.Date().getTime()).toString()+"\n"
				, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			//Dimservice disabled start
			//dimsrv = new DimService();
			//Tfdim001 _diminv = dimsrv.getDimInvoiceById(Integer.parseInt(fid));
			//Dimservice disabled end
			
			//path disabled start
			//String _pre_dir = globalContextPath + "V2/" + _diminv.getInvpath().substring(0, _diminv.getInvpath().lastIndexOf("/") + 1);// xml/einvoice/COMP"+((Tfedf000)session.getAttribute("Company")).getComp()+"/ETC/";
			//String _psfn = _pre_dir + _diminv.getInvpath().substring(_diminv.getInvpath().lastIndexOf("/") + 1);
			String _pre_dir =   globalContextPath;
			String _psfn = _pre_dir + fileName;
			//path disabled end
			try {
				String tiffPath = ConverterService.converterPdfTotiff(_psfn);
				System.out.println("TiffPath:"+tiffPath);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
			
			if (!new java.io.File(_psfn.replace(".pdf", ".tiff")).exists()) {
				OCREngineUtils.convertPDFToTiff(_psfn, "tiff");
			}

			if (new java.io.File(_psfn.replace(".pdf", ".tiff")).exists()) {
				BufferedImage bi = ImageIO.read(new FileInputStream(new java.io.File(_psfn.replace(".pdf", ".tiff"))));
				
			
				if (template != null) {
					//String _tesslang = findInvoiceConfByTaxNo(supplierTax, "TESSLANG");
					String _tesslang = "eng";
					// System.out.println(_tesslang);
					einvreq = new EditInvoiceRequest();

					OCRTemplateRequest ocrt = null;
					Gson gson = new Gson();
					//ocrt = (OCRTemplateRequest) gson.fromJson(_tmpl.getTemplmap(), OCRTemplateRequest.class);
					ocrt = (OCRTemplateRequest) gson.fromJson(template, OCRTemplateRequest.class);
					String tmpl_name_ = ocrt.getTitle();
					 boolean isServiceInvoice = false;
    				 if(ocrt.getIsserviceinvoice()!=null&&ocrt.getIsserviceinvoice().trim().equals("1")) {
    					isServiceInvoice = true;
    				 }
					gson = null;

					if (ocrt != null) {
						DecimalFormat df = new DecimalFormat();
						DecimalFormatSymbols symbols = new DecimalFormatSymbols();
						try {
							symbols.setDecimalSeparator(ocrt.getSep_decimal().charAt(0));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						try {
							symbols.setGroupingSeparator(ocrt.getSep_1000().charAt(0));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						df.setDecimalFormatSymbols(symbols);

						String datePatern = ocrt.getDate_format().replace("YYYY", "yyyy").replace("mmm", "MMM").replace("mm", "MM").replace("DD", "dd").trim();
 		            	if(datePatern.equals("dd MMMM yyyy") || datePatern.equals("dd MMM yyyy")|| datePatern.equals("MMM dd yyyy")|| datePatern.equals("MMM dd yyyy")) {
 		            		datePatern=datePatern.replace(" ", "-");
 		            	}
 		            	SimpleDateFormat parserSDF=new SimpleDateFormat(datePatern,Locale.UK);

						List<OCRTemplateMapItem> tmis = ocrt.getMapping();

						OCRTemplateMapItem line_end_tmi = null;
						OCRTemplateMapItem line_item_tmi = null;
						OCRTemplateMapItem line_start_tmi = null;
						// OCRTemplateMapItem line_subtotal_pad_tmi = null;
						OCRTemplateMapItem inv_taxexclamnt_tmi = null;
						OCRTemplateMapItem inv_total_tmi = null;
						OCRTemplateMapItem inv_line_height = null;
						OCRTemplateMapItem inv_line_first_element = null;
						OCRTemplateMapItem inv_line_last_element = null;
						BufferedImage _inv_lines_area_bi = null;
						int _line_cnt = 0;
						int _line_end = 0;
						int _subs_height = 0;

						int default_offset = 0;

						String _inv_line_top = "0";
						String _inv_line_height = "0";
						for(OCRTemplateMapItem item: tmis){ 
 		            		if (item.getLeft().indexOf(".") != -1) 
 		            		{
 		            			item.setLeft(item.getLeft().substring(0 , item.getLeft().indexOf("."))); 
 		            		}
 		            		if (item.getHeight().indexOf(".") != -1) 
 		            		{
 		            			item.setHeight(item.getHeight().substring(0 , item.getHeight().indexOf("."))); 
 		            		}
 		            		if (item.getTop().indexOf(".") != -1) 
 		            		{
 		            			item.setTop(item.getTop().substring(0 , item.getTop().indexOf("."))); 
 		            		}
 		            		if (item.getWidth().indexOf(".") != -1) 
 		            		{
 		            			item.setWidth(item.getWidth().substring(0 , item.getWidth().indexOf("."))); 
 		            		}
 		            	}
						if (setOffset) {
							int _offset_top = 0;
							int _offset_left = 0;
							for (OCRTemplateMapItem tmi : tmis) {
								if (tmi.getFieldId().equals("inv_date")) {// inv_line_item_code
									_offset_top = offsetTop - Integer.parseInt(tmi.getTop());
									_offset_left = offsetLeft - Integer.parseInt(tmi.getLeft());
									break;
								}
							}
							// System.out.println("OSTOP: " + _offset_top + ", OSLEFT: " + offsetLeft);
							for (OCRTemplateMapItem tmi : tmis) {
								tmi.setTop("" + (Integer.parseInt(tmi.getTop()) + _offset_top));
								tmi.setLeft("" + (Integer.parseInt(tmi.getLeft()) + _offset_left));
							}
						}

						for (OCRTemplateMapItem tmi : tmis) {
							tmi.setTop("" + (Integer.parseInt(tmi.getTop()) - default_offset));
							tmi.setLeft("" + (Integer.parseInt(tmi.getLeft()) - default_offset));
							if (tmi.getFieldId().equals(ocrt.getLine_end_tmi())) {// inv_line_item_code
								_inv_line_top = tmi.getTop();
								_inv_line_height = tmi.getHeight();
							}
							/*
							if (tmi.getFieldId().equals("inv_line_total")) {// inv_line_item_code
								_inv_line_top = tmi.getTop();
								_inv_line_height = tmi.getHeight();
							}
							*/
							if (tmi.getFieldId().equals(ocrt.getLine_first_col_tmi())) {// inv_line_item_code
								inv_line_first_element = tmi;
							}
							if (tmi.getFieldId().equals(ocrt.getLine_last_col_tmi())) {// inv_line_item_code
								inv_line_last_element = tmi;
							}
							if (tmi.getLeft().indexOf(".") != -1) 
 		            		{
								tmi.setLeft(tmi.getLeft().substring(0 , tmi.getLeft().indexOf("."))); 
 		            		}
 		            		if (tmi.getHeight().indexOf(".") != -1) 
 		            		{
 		            			tmi.setHeight(tmi.getHeight().substring(0 , tmi.getHeight().indexOf("."))); 
 		            		}
 		            		if (tmi.getTop().indexOf(".") != -1) 
 		            		{
 		            			tmi.setTop(tmi.getTop().substring(0 , tmi.getTop().indexOf("."))); 
 		            		}
 		            		if (tmi.getWidth().indexOf(".") != -1) 
 		            		{
 		            			tmi.setWidth(tmi.getWidth().substring(0 , tmi.getWidth().indexOf("."))); 
 		            		}
						}
						for (OCRTemplateMapItem tmi : tmis) {
							if (tmi.getFieldId().startsWith("inv_line_")) {
								tmi.setTop(_inv_line_top);
								tmi.setHeight(_inv_line_height);
							}
							if (tmi.getFieldId().equals(ocrt.getLine_end_tmi())) {// if (tmi.getFieldId().equals("inv_line_price")) {// if(tmi.getFieldId().equals("inv_line_total")){
								line_end_tmi = tmi;
							}
							if (tmi.getFieldId().equals(ocrt.getLine_first_col_tmi())) {
								line_start_tmi = tmi;
							}
							if (tmi.getFieldId().equals("line_height")) {// if(tmi.getFieldId().equals("inv_line_total")){
								inv_line_height = tmi;
							}
							if (tmi.getFieldId().startsWith("inv_line_item")) {// if (tmi.getFieldId().equals("inv_line_price")) {// if(tmi.getFieldId().equals("inv_line_total")){
								line_item_tmi = tmi;
							}
						}

						if (inv_line_height == null) {
							inv_line_height = new OCRTemplateMapItem();
							inv_line_height.setHeight("0");
						}

						// if(!ocrt.getIstotalfixed().equals("1")){
						for (OCRTemplateMapItem tmi : tmis) {
							if (tmi.getFieldId().equals("line_subtotal_pad")) {// if(tmi.getFieldId().equals("inv_line_total")){
								// line_subtotal_pad_tmi = tmi;
							}
							if (tmi.getFieldId().equals("inv_taxexclamnt")) {// if(tmi.getFieldId().equals("inv_line_total")){
								inv_taxexclamnt_tmi = tmi;
							}
							if (tmi.getFieldId().equals("inv_total")) {// if(tmi.getFieldId().equals("inv_line_total")){
								inv_total_tmi = tmi;
							}

						}
						
						List<OCRTemplateMapItem> tmis2 = new ArrayList<>();
						if (!isServiceInvoice) {
							for (OCRTemplateMapItem tmi : tmis) {

								if (tmi.getFieldId().startsWith("inv_line_")) {
									tmis2.add(tmi);
								}
							}
							Collections.sort(tmis2, new Comparator<OCRTemplateMapItem>() {

								public int compare(OCRTemplateMapItem o1, OCRTemplateMapItem o2) {
									// compare two instance of `Score` and return `int` as result.
									int a = Integer.parseInt(o2.getLeft());
									int b = Integer.parseInt(o1.getLeft());
									return Integer.compare(a, b);
								}
							});
							if (line_end_tmi != null) {

								String _line_txt = "";
								String _line_txt_2 = "";
								int is_line_end_number = 0;

								do {
									_line_txt = "";
									_line_txt_2 = "";
									BufferedImage bi_1 = bi.getSubimage(Integer.parseInt(line_end_tmi.getLeft()),
											(Integer.parseInt(line_end_tmi.getTop())
													+ (_line_cnt * (Integer.parseInt(line_end_tmi.getHeight())
															+ Integer.parseInt(inv_line_height.getHeight())))),
											Integer.parseInt(line_end_tmi.getWidth()),
											Integer.parseInt(line_end_tmi.getHeight()));
									String _inv_part_fn = _psfn.substring(0, _psfn.lastIndexOf(".")) + "_lf_"
											+ _line_cnt + "_" + line_end_tmi.getFieldId() + ".tiff";
									NoiseFilter n = new NoiseFilter();
									BufferedImage bi_2 = bi_1;
									//bi_2=n.filter(bi_1, bi_2);
									ImageIO.write(bi_2, "tiff", new File(_inv_part_fn));

									BufferedImage bi_4 = bi.getSubimage(Integer.parseInt(line_item_tmi.getLeft()),
											(Integer.parseInt(line_item_tmi.getTop())
													+ (_line_cnt * (Integer.parseInt(line_item_tmi.getHeight())
															+ Integer.parseInt(inv_line_height.getHeight())))),
											Integer.parseInt(line_item_tmi.getWidth()),
											Integer.parseInt(line_item_tmi.getHeight()));
									String _inv_part_fn3 = _psfn.substring(0, _psfn.lastIndexOf(".")) + "_lf_"
											+ _line_cnt + "_" + line_item_tmi.getFieldId() + ".tiff";

									BufferedImage bi_3 = bi_4;
									//bi_3=n.filter(bi_4, bi_3);
									ImageIO.write(bi_3, "tiff", new File(_inv_part_fn3));

									_line_txt = doOcr2(_inv_part_fn, ocrt.getIsdotmatrix(), _tesslang, false);
									_line_txt_2 = doOcr2(_inv_part_fn3, ocrt.getIsdotmatrix(), _tesslang, false);
									if (!(_line_txt != null && _line_txt.trim().length() > 0)) {
										_line_txt = doOcr2(_inv_part_fn, ocrt.getIsdotmatrix(), _tesslang, true);
									}

									if (_line_txt != null && _line_txt.trim().length() > 0) {
										_line_cnt++;
									} else {
										if (_line_txt_2 != null && _line_txt_2.trim().length() > 0) {
											_line_txt_2 = _line_txt_2.trim();
											_line_cnt++;
										} else {
											_line_end = Integer.parseInt(line_end_tmi.getTop())
													+ (_line_cnt * Integer.parseInt(line_end_tmi.getHeight()));
										}
									}
									if (!((_line_txt != null && _line_txt.trim().length() > 0)
											|| (_line_txt_2 != null && _line_txt_2.trim().length() > 0))) {
										is_line_end_number++;
									}
								} while (is_line_end_number <= 1);
							}
							if (_line_cnt == 0) {
								_line_cnt = 1;
								_line_end = Integer.parseInt(line_end_tmi.getTop())
										+ Integer.parseInt(line_end_tmi.getHeight());
							}
							if (_line_cnt > 0) {
								einvreq.setLine_list(new ArrayList<GenericDataItem>());

								// satirlar bolgesini kes hocr ile tara
								_inv_lines_area_bi = bi.getSubimage(Integer.parseInt(inv_line_first_element.getLeft()),
										Integer.parseInt(inv_line_first_element.getTop()) - 10,
										((Integer.parseInt(inv_line_last_element.getLeft())
												+ Integer.parseInt(inv_line_last_element.getWidth()))
												- Integer.parseInt(inv_line_first_element.getLeft())) + 10,
										(_line_cnt * (Integer.parseInt(inv_line_first_element.getHeight()))) + 20);

								String _inv_part_fn = _psfn.substring(0, _psfn.lastIndexOf(".")) + "_" + 0 + "_"
										+ "inv_line_area" + ".tiff";
								ImageIO.write(_inv_lines_area_bi, "tiff", new File(_inv_part_fn));

								String _rt = OCREngineUtils.runTesseract(_inv_part_fn,
										"-l tur " + ((_tesslang != null) ? "-l " + _tesslang : "") + " -psm 6 hocr");
								FileUtils.writeStringToFile(new File(_inv_part_fn.replace(".tiff", ".out.html")), _rt);
								LineIterator it = FileUtils
										.lineIterator(new File(_inv_part_fn.replace(".tiff", ".out.html")), "UTF-8");
								int _lastii = 1;

								int lineNumberInImage = 0;
								int wordNumberInImage = 0;
								boolean inLine_ = false;

								List<Object[]> lines_1 = new ArrayList<>();
								int lastElementLeft = Integer.parseInt(inv_line_last_element.getLeft());
								String _line_txt2 = "";
								while (it.hasNext()) {
									String _line_txt = it.nextLine().trim();

									String _st_word = "span class='ocr_line' id='line_1_";
									if (_line_txt != null && _line_txt.indexOf(_st_word) > -1) {

										lineNumberInImage++;
										int _st1 = _line_txt.indexOf(_st_word) + _st_word.length();
										_st_word = _line_txt.substring(_st1 + 1, _line_txt.indexOf(">"));
										_st_word = _st_word.replace("\"", "");
										_st_word = _st_word.replace("'", "");
										_st_word = _st_word.replace("title=bbox ", "");
										String _tmp_arr[] = _st_word.split(Pattern.quote(" "));
										if (_tmp_arr != null && _tmp_arr.length >= 4) {

											if (Math.abs(Integer.parseInt(_tmp_arr[3]) - lastElementLeft) < 750) {
												inLine_ = true;
												_line_txt2 = _line_txt;
											} else {
												inLine_ = false;
											}
										}
									}
									if (_line_txt != null && _line_txt.equals("</span>")) {
										inLine_ = false;
									}
									String _st_word2 = "id='word_1_";
									String lineData = "";
									if (_line_txt != null && _line_txt.indexOf(_st_word2) > -1 && inLine_) {
										wordNumberInImage++;
										lineData = _line_txt.substring(_line_txt.indexOf(">") + 1,
												_line_txt.indexOf("</"));

										Object[] line_1 = new Object[4];
										line_1[0] = lineNumberInImage;
										line_1[1] = _line_txt2;
										line_1[2] = lineData;
										line_1[3] = wordNumberInImage;
										lines_1.add(line_1);

									}
								}

								//							Collections.sort(lines_1, new Comparator<Object[]>() {
								//
								//						        public int compare(Object[] o1, Object[] o2) {
								//						            // compare two instance of `Score` and return `int` as result.
								//						        	int a = (int)o2[3];
								//						        	int b = (int)o1[3];
								//						            return  Integer.compare(a, b);
								//						        }
								//						    });
								Collections.sort(lines_1, new Comparator<Object[]>() {

									public int compare(Object[] o1, Object[] o2) {
										// compare two instance of `Score` and return `int` as result.
										int a = (int) o2[0];
										int b = (int) o1[0];
										int sComp = Integer.compare(b, a);

										if (sComp != 0) {
											return sComp;
										}
										int c = (int) o2[3];
										int d = (int) o1[3];
										return Integer.compare(c, d);
									}

								});
								ArrayList<GenericDataItem> _tmp_list4 = new ArrayList<GenericDataItem>();
								HashMap<Integer, String> hmap = new HashMap<Integer, String>();
								int last3Field = 0;
								int whichLine = 1;
								boolean isLine = true;
								for (int i33 = 0; i33 < lines_1.size(); i33++) {

									GenericDataItem line_X = new GenericDataItem();

									if (whichLine == (int) lines_1.get(i33)[0]) {
										if (i33 != 0) {
											last3Field++;
										}
										try {
											if (last3Field <= 2 && isLine) {
												df.parse(
														cleanTextForNumbers(
																((String) lines_1.get(i33)[2]).replace('.', '1')
																		.replace(',', '1').replace(':', '1').trim(),
																ocrt))
														.doubleValue();

												hmap.put((int) lines_1.get(i33)[0], (String) lines_1.get(i33)[1]);
												isLine = true;

											}

										} catch (Exception e) {
											isLine = false;
										}
									} else {
										last3Field = 1;
										try {

											df.parse(cleanTextForNumbers(
													((String) lines_1.get(i33)[2]).replace('.', '1').replace(',', '1')
															.replace(':', '1').replace('e', 'r').trim(),
													ocrt)).doubleValue();
											hmap.put((int) lines_1.get(i33)[0], (String) lines_1.get(i33)[1]);
											isLine = true;

										} catch (Exception e) {
											isLine = false;
										}
									}
									whichLine = (int) lines_1.get(i33)[0];

								}
								Set set = hmap.entrySet();
								Iterator iterator = set.iterator();
								while (iterator.hasNext()) {
									Map.Entry mentry = (Map.Entry) iterator.next();
									String _line_txt = ((String) mentry.getValue()).trim();
									GenericDataItem gendi = new GenericDataItem();
									int _ii = (int) mentry.getKey();
									//									String _st_word = "span class='ocr_line' id='line_" + _ii + "_1'";
									String _st_word = "span class='ocr_line' id='line_1_" + _ii + "'";
									int _st1 = _line_txt.indexOf(_st_word) + _st_word.length();
									_st_word = _line_txt.substring(_st1 + 1, _line_txt.indexOf(">"));
									_st_word = _st_word.replace("\"", "");
									_st_word = _st_word.replace("'", "");
									_st_word = _st_word.replace("title=bbox ", "");
									String _tmp_arr[] = _st_word.split(Pattern.quote(" "));
									if (_tmp_arr != null && _tmp_arr.length >= 4) {
										gendi.setTop((Integer.parseInt(inv_line_first_element.getTop())
												+ Integer.parseInt(_tmp_arr[1])) - 10);
										gendi.setHeight((Integer.parseInt(_tmp_arr[3].replace(";", ""))
												- Integer.parseInt(_tmp_arr[1])) + 10);
										einvreq.getLine_list().add(gendi);
									}

								}

								LineIterator.closeQuietly(it);
								//							if (einvreq.getLine_list() != null && _lastii != einvreq.getLine_list().size()) {
								//								ArrayList<GenericDataItem> _tmp_list = new ArrayList<GenericDataItem>();
								//								for (int _ii = 0; _ii < einvreq.getLine_list().size(); _ii++) {
								//									if (_ii < _lastii) {
								//										_tmp_list.add(einvreq.getLine_list().get(_ii));
								//									}
								//								}
								//								einvreq.setLine_list(_tmp_list);
								//								_line_cnt = einvreq.getLine_list().size();
								//							}
								// satirlar bolgesini kes hocr ile tara
							} 
						}
						
						for (OCRTemplateMapItem tmi : tmis) {
							try {
								if (tmi.getFieldId() != null) {
									if (!tmi.getFieldId().startsWith("inv_line_")) {
										int _xpos = 0, _ypos = 0, _width = 0, _height = 0;
										_xpos = Integer.parseInt(tmi.getLeft());
										_ypos = Integer.parseInt(tmi.getTop());
										_width = Integer.parseInt(tmi.getWidth());
										_height = Integer.parseInt(tmi.getHeight());

										String _line_txt = "";
										BufferedImage bi_2 = bi.getSubimage(_xpos, _ypos, _width, _height);
										String _inv_part_fn = _psfn.substring(0, _psfn.lastIndexOf(".")) + "_" + tmi.getFieldId() + ".tiff";
										ImageIO.write(bi_2, "tiff", new File(_inv_part_fn));

										_line_txt = doOcr(_inv_part_fn, tmi, ocrt, _tesslang, false);

										if (!(_line_txt != null && _line_txt.trim().length() > 0)) {
											_line_txt = doOcr(_inv_part_fn, tmi, ocrt, _tesslang, true);
										}
										if (_line_txt != null && _line_txt.trim().length() > 0) {
											_line_txt = _line_txt.trim().replace("\"", "").replace("'", "");
											//einvreq.setCurrcode("GBP");
											if (tmi.getFieldId().equals("inv_no")) {
												//_line_txt = OCRUtils.cleanTextForNumbers(_line_txt, ocrt, true);
												_line_txt = OCRUtils.cleanForItem(_line_txt);
												if (einvreq.getInvoice_id() != null && einvreq.getInvoice_id().trim().length() > 0) {
													einvreq.setInvoice_id(einvreq.getInvoice_id().trim() + _line_txt);
												} else {
													einvreq.setInvoice_id(_line_txt);
												}
											}
											if (tmi.getFieldId().equals("inv_serial")) {
												_line_txt = OCRUtils.cleanForSeriNo(_line_txt);
												if (einvreq.getInvoice_id() != null && einvreq.getInvoice_id().trim().length() > 0) {
													einvreq.setInvoice_id(_line_txt + einvreq.getInvoice_id().trim());
												} else {
													einvreq.setInvoice_id(_line_txt);
												}
											}
											if (tmi.getFieldId().equals("inv_title")) {
												einvreq.setCustomer_name(_line_txt);
											}
											if(tmi.getFieldId().equals("inv_currency")){
												einvreq.setCurrcode(_line_txt.trim());
				 	                        }
											if(tmi.getFieldId().equals("inv_orderno")){ // order number
												einvreq.setDespno(_line_txt.trim().replace("PO", ""));
				 	                        }
											if (tmi.getFieldId().equals("inv_taxno")) {
												_line_txt = cleanTextForNumbers(_line_txt, ocrt);
												einvreq.setCustomer_taxn(_line_txt);
											}
											if (tmi.getFieldId().equals("inv_taxoffice")) {
												einvreq.setCust_taxo(_line_txt);
											}
											if (tmi.getFieldId().equals("inv_address")) {
												if (_line_txt.lastIndexOf(" ") > -1) {
													einvreq.setCust_street(_line_txt.substring(0, _line_txt.lastIndexOf(" ")));
													einvreq.setCust_city(_line_txt.substring(_line_txt.lastIndexOf(" ")));
												} else {
													einvreq.setCust_street(_line_txt);
												}
											}
											//einvreq.setCurrcode("YTL"); // DEGISECEK

											if (tmi.getFieldId().equals("inv_date")) {

												//_line_txt = _line_txt.trim().substring(_line_txt.lastIndexOf(" ")+1,_line_txt.length());
				 	                        	System.out.println("inv_date word:" + _line_txt);
//				 	                        	if(word!=null && word.trim().length()>0){  
//				 	                        		invReq.setIssue_date(new java.sql.Date(parserSDF.parse(word.trim()).getTime()).toString());
//				 	                        	}
				 	                        	try {
				 	                        		_line_txt=_line_txt.replaceAll("(?i)invoice", "");
				 	                        		_line_txt=_line_txt.replaceAll("(?i)date", "");
				 	                        		_line_txt=_line_txt.replace(":", " ");
				 	                        		String datePatern2 = ocrt.getDate_format().replace("YYYY", "yyyy").replace("mmm", "MMM").replace("mm", "MM").replace("DD", "dd").trim();
				 	        		            	String word_date = _line_txt.trim().replaceAll("[^\\x00-\\x7F]", "-").replaceAll("\\P{Print}", "-")
				 	        		            			.replace(",", "-").replace(".", "-").replace(" ", "-").
				 	        		            			replace("st","-").replace("nd","-").replace("rd","-").replace("th","-").replace("---","-").replace("--","-");
				 	        		            	word_date =(word_date.startsWith("-")?word_date.replaceFirst("-", ""):word_date);
				 	        		            	
				 	        		            	_line_txt = _line_txt.trim().replaceAll("[^\\x00-\\x7F]", "").replaceAll("\\P{Print}", "-");
				 	        		            	
				 	        		            	
				 	        		            	if(datePatern2.equals("dd MMMM yyyy") || datePatern2.equals("dd MMM yyyy")|| datePatern2.equals("MMM dd yyyy")|| datePatern2.equals("MMM dd yyyy")) {
				 	        		            		einvreq.setIssue_date(new java.sql.Date(parserSDF.parse(word_date).getTime()).toString());
				 	        		            	}
				 	        		            	else {
				 	        		            		einvreq.setIssue_date(new java.sql.Date(parserSDF.parse(_line_txt).getTime()).toString());
				 	        		            	}
				 	                        		
												} catch (Exception e) {
													e.printStackTrace();
												}
			 	                        		
											}

											if (tmi.getFieldId().equals("inv_despno")) {
												einvreq.setDespno(_line_txt.trim().replace(" ", ""));

											}

											if (tmi.getFieldId().equals("inv_despdate")) {
												String datePatern2 = ocrt.getDate_format().replace("YYYY", "yyyy").replace("mmm", "MMM").replace("mm", "MM").replace("DD", "dd").trim();
												String word_date = _line_txt.trim().replaceAll("\\s", "-");
												if(datePatern2.equals("dd MMMM yyyy") || datePatern2.equals("dd MMM yyyy")) {
			 	        		            		einvreq.setIssue_date(new java.sql.Date(parserSDF.parse(word_date).getTime()).toString());
			 	        		            	}
			 	        		            	else {
			 	        		            		einvreq.setIssue_date(new java.sql.Date(parserSDF.parse(_line_txt.trim()).getTime()).toString());
			 	        		            	}
											}

											einvreq.setInvoice_type("SATIS");
											einvreq.setIsservicerequest((isServiceInvoice?"1":"0"));
											einvreq.setTotal_tevbamnt(0);
											einvreq.setTotal_tevkdv(0);
											einvreq.setTotal_tev(0);

											if (tmi.getFieldId().equals("inv_taxexclamnt")) {
												_line_txt=_line_txt.replace("�", "").replace("�", "").replace("$", "");
												try {
													_subs_height += Integer.parseInt(tmi.getHeight());
													_line_txt = cleanTextForNumbers(_line_txt, ocrt);
													einvreq.setTotal_amnt(df.parse(_line_txt).doubleValue());
												} catch (Exception e) {
													e.printStackTrace();
												}
											}

											if (tmi.getFieldId().equals("inv_kdvamnt")) {
												try {
													_line_txt=_line_txt.replace("�", "").replace("�", "").replace("$", "");
													_subs_height += Integer.parseInt(tmi.getHeight());
													_line_txt = cleanTextForNumbers(_line_txt, ocrt);
													einvreq.setTotal_kdv(df.parse(_line_txt).doubleValue());
												} catch (Exception e) {
													e.printStackTrace();
												}
											}

											einvreq.setTotal_disc(0);

											if (tmi.getFieldId().equals("inv_total")) {
												try {
													_line_txt=_line_txt.replace("�", "").replace("�", "").replace("$", "");
													_subs_height += Integer.parseInt(tmi.getHeight());
													_line_txt = cleanTextForNumbers(_line_txt, ocrt);
													einvreq.setTotal_taxincl(df.parse(_line_txt).doubleValue());
													einvreq.setTotal_final(df.parse(_line_txt).doubleValue());
												} catch (Exception e) {
													e.printStackTrace();
												}
											}

										}
									} 
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}		
						EditInvoiceRequest einvreq2 = new EditInvoiceRequest();
						ArrayList<GenericDataItem> _tmp_list2 = new ArrayList<GenericDataItem>();
						int lineCountR = 0 ;
						if (!isServiceInvoice) {
							if (einvreq.getLine_list() != null && einvreq.getLine_list().size() > 0) {
								Collections.sort(einvreq.getLine_list(), new Comparator<GenericDataItem>() {

									public int compare(GenericDataItem o1, GenericDataItem o2) {
										// compare two instance of `Score` and return `int` as result.
										int a = o2.getTop();
										int b = o1.getTop();
										return Integer.compare(b, a);
									}
								});
							}
							for (int ii = 0; ii < einvreq.getLine_list().size(); ii++) {
								boolean isLine = false;
								boolean isLine2 = false;

								for (OCRTemplateMapItem tmi : tmis) {

									if (tmi.getFieldId() != null) {
										if (tmi.getFieldId().startsWith("inv_line_")) {
											String _line_txt = null;

											// System.out.println(tmi.getLeft() + " " + einvreq.getLine_list().get(ii).getTop() + " " + tmi.getWidth() + " "
											// + einvreq.getLine_list().get(ii).getHeight());

											BufferedImage bi_2 = bi.getSubimage(Integer.parseInt(tmi.getLeft()),
													einvreq.getLine_list().get(ii).getTop(),
													Integer.parseInt(tmi.getWidth()),
													einvreq.getLine_list().get(ii).getHeight());

											String _inv_part_fn = _psfn.substring(0, _psfn.lastIndexOf(".")) + "_" + ii
													+ "_" + tmi.getFieldId() + ".tiff";
											ImageIO.write(bi_2, "tiff", new File(_inv_part_fn));

											_line_txt = doOcr3(_inv_part_fn, tmi, ocrt, _tesslang, false);

											if (!(_line_txt != null && _line_txt.trim().length() > 0)) {
												_line_txt = doOcr3(_inv_part_fn, tmi, ocrt, _tesslang, true);
											}

											if (_line_txt != null && _line_txt.trim().length() > 0) {

												_line_txt = _line_txt.trim().replace("\"", "").replace("'", "");
												einvreq.getLine_list().get(ii).setInd((ii + 1));
												einvreq.getLine_list().get(ii).setUnit("EA");

												einvreq.getLine_list().get(ii).setMancode("-");
												//einvreq.getLine_list().get(ii).setTaxp(18.0);
												if (tmi.getFieldId().equals("inv_line_item_code")) {
													einvreq.getLine_list().get(ii).setMancode(_line_txt.trim());
												}
												if (tmi.getFieldId().equals("inv_line_item")) {
													einvreq.getLine_list().get(ii).setItem(_line_txt.trim());
												}
												if (tmi.getFieldId().equals("inv_line_unit")) {
													einvreq.getLine_list().get(ii).setUnit(_line_txt.trim());
													// System.out.println("LINE " + ii + " UNIT: " + _line_txt.trim());
												}
												if (tmi.getFieldId().equals("inv_line_qty")) {
													try {
														_line_txt = cleanTextForNumbers(_line_txt, ocrt);
														// System.out.println("LINE " + ii + " QTY: " + _line_txt.trim());
														einvreq.getLine_list().get(ii)
																.setQty(df.parse(_line_txt).doubleValue());
													} catch (Exception e) {
													}
												}
												if (tmi.getFieldId().equals("inv_line_price")) {
													try {
														_line_txt = cleanTextForNumbers(_line_txt, ocrt);
														einvreq.getLine_list().get(ii)
																.setPrice(df.parse(_line_txt).doubleValue());
													} catch (Exception e) {
													}
												}
												if (tmi.getFieldId().equals("inv_line_total")) {
													try {
														_line_txt = cleanTextForNumbers(_line_txt, ocrt);
														einvreq.getLine_list().get(ii)
																.setAmnt(df.parse(_line_txt).doubleValue());
														einvreq.getLine_list().get(ii)
																.setTotal(df.parse(_line_txt).doubleValue());

													} catch (Exception e) {
													}
												}
												if (tmi.getFieldId().equals("inv_line_kdvrate")) {
													try {
														einvreq.getLine_list().get(ii)
																.setTaxp(df.parse(_line_txt).doubleValue());

													} catch (Exception e) {
													}
												}
												if (tmi.getFieldId().equals("inv_line_kdv")) {
													try {
														einvreq.getLine_list().get(ii)
																.setTax(df.parse(_line_txt).doubleValue());

													} catch (Exception e) {
													}
												}

												if (tmi.getFieldId().equals("inv_line_despno")) {
													if (einvreq.getDespatch_list() == null) {
														einvreq.setDespatch_list(new ArrayList<GenericDataItem>());
													}
													boolean _add_desp = true;
													if (einvreq.getDespatch_list() != null
															&& einvreq.getDespatch_list().size() > 0) {
														for (GenericDataItem gendi : einvreq.getDespatch_list()) {
															if (gendi.getValue().equals(_line_txt)) {
																_add_desp = false;
															}
														}
													}
													if (_add_desp) {
														GenericDataItem gendi = new GenericDataItem();
														gendi.setValue(_line_txt);
														einvreq.getDespatch_list().add(gendi);
														gendi = null;
													}
												}
												// if(ii < _line_cnt-1){
												// ii++;
												// }
											} // line_txt te veri var ise
												// }// while it.hasnext
										}
									}
								}

							} 
						}
						if(einvreq != null && einvreq.getCustomer_taxn() == null) {
							einvreq.setCustomer_taxn(supplierTax);
						}
						if (isServiceInvoice) {
							einvreq.setLine_list(new ArrayList<GenericDataItem>());
							einvreq.getLine_list().add(new GenericDataItem());
							einvreq.getLine_list().get(0).setQty(1);
							einvreq.getLine_list().get(0).setAmnt(einvreq.getTotal_amnt()); 
							einvreq.getLine_list().get(0).setTax(einvreq.getTotal_kdv());
							einvreq.getLine_list().get(0).setPrice(einvreq.getTotal_amnt());
							einvreq.getLine_list().get(0).setItem("General Service Fees");
							einvreq.getLine_list().get(0).setUnit("EA");
							einvreq.getLine_list().get(0).setMancode("-");
							einvreq.getLine_list().get(0).setInd(1);
	     				 }
						// subtotal find start
						if (_line_end > 0 && _subs_height > 0 && Integer.parseInt(_inv_line_top) > 0 && Integer.parseInt(_inv_line_height) > 0
								&& inv_taxexclamnt_tmi != null && inv_total_tmi != null && _line_cnt > 0
								&& !(ocrt.getIstotalfixed() != null && ocrt.getIstotalfixed().equals("1"))) {
							int _subtotstart = Integer.parseInt(inv_taxexclamnt_tmi.getTop());
							int _h2 = _line_end - (Integer.parseInt(_inv_line_top) + Integer.parseInt(_inv_line_height));
							if (ocrt.getIstotalfixed().equals("1")) {
								_h2 = 0;
							}

							_subtotstart += _h2;

							_subtotstart = _subtotstart - 25;// guven araligi 25 px geri aldim baslangici

							int _subtotend = Integer.parseInt(inv_total_tmi.getTop()) + Integer.parseInt(inv_total_tmi.getHeight());
							_subtotend += _h2;

							_subtotend = _subtotend + 25;// guven araligi 25 px geri aldim baslangici

							String _line_txt = null;

							BufferedImage bi_2 = bi.getSubimage(Integer.parseInt(inv_taxexclamnt_tmi.getLeft()) - 10,
							// ( Integer.parseInt(tmi.getTop()) + (ii * (Integer.parseInt(tmi.getHeight()) + Integer.parseInt(inv_line_height.getHeight())) ) ),
									_subtotstart, Integer.parseInt(inv_taxexclamnt_tmi.getWidth()) + 10, _subtotend - _subtotstart);

							String _inv_part_fn = _psfn.substring(0, _psfn.lastIndexOf("."))

							+ "_" + "subtotalpart" + ".tiff";
							ImageIO.write(bi_2, "tiff", new File(_inv_part_fn));

							_line_txt = doOcr2(_inv_part_fn, ocrt.getIsdotmatrix(), _tesslang, false);

							if (!(_line_txt != null && _line_txt.trim().length() > 0)) {
								_line_txt = doOcr2(_inv_part_fn, ocrt.getIsdotmatrix(), _tesslang, true);
							}
							
							
							LineIterator it = FileUtils.lineIterator(new File(_inv_part_fn.replace(".tiff", ".out.txt")), "UTF-8");
							int _cnt = 0;
							while (it != null && it.hasNext()) {
								_line_txt = it.nextLine().trim();
								if (_line_txt != null && _line_txt.length() > 0) {
									_cnt++;

									// System.out.println("STFINDLINE " + _cnt + ": " + cleanTextForNumbers(_line_txt, ocrt));
									if (_cnt == 1) {
										try {
											_line_txt = cleanTextForNumbers(_line_txt, ocrt);
											einvreq.setTotal_amnt(df.parse(_line_txt).doubleValue());
										} catch (Exception e) {
										}
									}

									if (_cnt == 2) {
										try {

											_line_txt = cleanTextForNumbers(_line_txt, ocrt);
											einvreq.setTotal_kdv(df.parse(_line_txt).doubleValue());
										} catch (Exception e) {
										}
									}

									if (_cnt == 3) {
										try {
											_line_txt = cleanTextForNumbers(_line_txt, ocrt);
											einvreq.setTotal_taxincl(df.parse(_line_txt).doubleValue());
											einvreq.setTotal_final(df.parse(_line_txt).doubleValue());
										} catch (Exception e) {
										}
									}
								}
							}

							LineIterator.closeQuietly(it);

						}
						// subtotal find end
					}
				} else {
					// sablon yok start backupladim
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			org.apache.commons.io.IOUtils.closeQuietly(fos);
			//dimsrv.close();
		}

		if (einvreq != null) {
			if (einvreq.getCust_street() == null) {
				einvreq.setCust_street(" ");
			}
			if (einvreq.getCust_city() == null) {
				einvreq.setCust_city(" ");
			}
		}

		return einvreq;
	}

	private String cleanTextForNumbers(String _line_txt, OCRTemplateRequest ocrt) {
		_line_txt = _line_txt.trim().replace('/', '7').replace('f', '7').replace('O', '0').replace('o', '0').replace(';', ',').replace('I', '1').replace('�', '1').replace('|', '1')
				.replace('l', '1').replace('$', '5').replace("�", ""); //.replaceAll("[^\\d.,]", "");  degisti.
		if (!_line_txt.contains(ocrt.getSep_decimal())) {
			if (_line_txt.contains(ocrt.getSep_1000())) {
				_line_txt = _line_txt.replace(ocrt.getSep_1000(), ocrt.getSep_decimal());
			}
		}
		_line_txt = _line_txt.replace(ocrt.getSep_1000(), "");
		_line_txt.trim();
		return _line_txt;
	}

	private String findInvoiceConfByTaxNo(String taxno, String fieldName) {
		String r = null;
		if (new File(INVOICECONFPATH).exists()) {
			LineIterator it = null;
			try {
				it = FileUtils.lineIterator(new File(INVOICECONFPATH), "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}

			while (it != null && it.hasNext()) {
				String _data = it.nextLine();
				if (_data.contains("[TAXNO=" + taxno + "]")) {
					String[] _dataarr = _data.split(Pattern.quote("|"));
					if (_dataarr != null) {
						for (int i = 0; i < _dataarr.length; i++) {
							if (_dataarr[i].contains("[" + fieldName + "=")) {
								r = _dataarr[i].replace("[" + fieldName + "=", "");
								r = r.replace("]", "");
								break;
							}
						}
					}
					break;
				}
			}
			if (r == null) {
				try {
					it = FileUtils.lineIterator(new File(INVOICECONFPATH), "UTF-8");
				} catch (IOException e) {
					e.printStackTrace();
				}
				while (it != null && it.hasNext()) {
					String _data = it.nextLine();
					if (_data.contains("[TAXNO=%]")) {
						String[] _dataarr = _data.split(Pattern.quote("|"));
						if (_dataarr != null) {
							for (int i = 0; i < _dataarr.length; i++) {
								if (_dataarr[i].contains("[" + fieldName + "=")) {
									r = _dataarr[i].replace("[" + fieldName + "=", "");
									r = r.replace("]", "");
									break;
								}
							}
						}
						break;
					}
				}

			}
			LineIterator.closeQuietly(it);

		}
		return r;
	}

	private String doOcr(String _inv_part_fn, OCRTemplateMapItem tmi, OCRTemplateRequest ocrt, String _tesslang, boolean clean_extra) {
		String r = null;
		if (OSTYPE == 2) {
			if (tmi.getFieldId().equals("inv_no") || tmi.getFieldId().equals("inv_serial")) {
				runIMScripts(_inv_part_fn, "1", false);
			} else {
				runIMScripts(_inv_part_fn, ocrt.getIsdotmatrix(), clean_extra);
			}

		}
		try {

			r = OCREngineUtils.runTesseract(_inv_part_fn, ((tmi.getFieldId().equals("inv_no") || tmi.getFieldId().equals("inv_date")) ? " "
					+ ((_tesslang != null) ? "-l " + _tesslang : "") + " " : " -l tur " + ((_tesslang != null) ? "-l " + _tesslang : "") + " ") + " -psm 6");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}

	private String doOcr3(String _inv_part_fn, OCRTemplateMapItem tmi, OCRTemplateRequest ocrt, String _tesslang, boolean clean_extra) {
		String r = null;
		if (OSTYPE == 2) {
			if (tmi.getFieldId().equals("inv_no") || tmi.getFieldId().equals("inv_serial")) {
				runIMScripts(_inv_part_fn, "1", false);
			} else {
				runIMScripts(_inv_part_fn, ocrt.getIsdotmatrix(), clean_extra);
			}

		}
		try {
			String _params = "";
			if (tmi.getFieldId().equals("inv_line_qty") || tmi.getFieldId().equals("inv_line_unit")) {

				_params = ((tmi.getFieldId().equals("inv_line_item")) ? " -l tur " + ((_tesslang != null) ? "-l " + _tesslang : "") + "" : " -l eng "
						+ ((_tesslang != null) ? "-l " + _tesslang : "") + " ")
						+ " -psm 6 ";
			}
			else {

				_params = ((tmi.getFieldId().equals("inv_line_item")) ? " -l tur " + ((_tesslang != null) ? "-l " + _tesslang : "") + "" : " -l eng "
						+ ((_tesslang != null) ? "-l " + _tesslang : "") + " ") + " -psm 6";
			}

			r = OCREngineUtils.runTesseract(_inv_part_fn, _params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}

	private String doOcr2(String _inv_part_fn, String isdm, String _tesslang, boolean clean_extra) {
		String r = null;
		try {
			if (OSTYPE == 2) {
				runIMScripts(_inv_part_fn, isdm, clean_extra);
			}
			r = OCREngineUtils.runTesseract(_inv_part_fn, " -l tur" + ((_tesslang != null) ? "+" + _tesslang : "") + " -psm 6");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	private void runIMScripts(String _inv_part_fn, String is_dm, boolean doExtraCleaning) {
		String cargs = "";

		if (!(_inv_part_fn.contains("inv_no") || _inv_part_fn.contains("_header."))) {
			cargs += "textcleaner -g -e stretch -f 25 -o 10 -s 1_";
		}

		if (is_dm.equals("1")) {

			if (doExtraCleaning) {
				cargs += "convert#-morphology close square:2_";
			}

			cargs += "gaussian -w 1 -k low -m 100_";

			cargs += "edgefx -s 4 -m 80 -c darken_";

			cargs += "gaussian -w 1 -k low -m 100_";

			cargs += "gaussian -w 1 -k low -m 100_";
		}

		cargs += "gaussian -w 1 -k low -m 100_";
		if (doExtraCleaning) {

			cargs += "convert#-morphology close square:2_";
		}

		OCREngineUtils.runIMScripts(_inv_part_fn, cargs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.eintecon.service.InvoiceParser#findTemplate(java.lang.String)
	 */
	
	public Object[] findTemplate(String invoiceFilePath) {
		String[] TAXNOKEYWORDS = readVatNoTypingTypes() ;
		
		Object[] r = new Object[2];
		r[0] = null;
		r[1] = null;
		//DimService dimsrv = new DimService();
		String _inv_header_fn = "";

		String _pre_dir = globalContextPath + "V2/" + invoiceFilePath.substring(0, invoiceFilePath.lastIndexOf("/") + 1);// xml/einvoice/COMP"+((Tfedf000)session.getAttribute("Company")).getComp()+"/ETC/";
		String _psfn = _pre_dir + invoiceFilePath.substring(invoiceFilePath.lastIndexOf("/") + 1);
		String _psfnPdf = _psfn;
		ImageIO.scanForPlugins();
		try {

			OCREngineUtils.convertPDFToTiff(_psfn, "tiff");

			if (new java.io.File(_psfn.replace(".pdf", ".tiff")).exists()) {
				BufferedImage bi = ImageIO.read(new FileInputStream(new java.io.File(_psfn.replace(".pdf", ".tiff"))));

				BufferedImage bi_1 = bi.getSubimage(0, 0, bi.getWidth(), bi.getHeight());
				_inv_header_fn = _psfn.substring(0, _psfn.lastIndexOf(".")) + "_header.tiff";
				ImageIO.write(bi_1, "tiff", new File(_inv_header_fn));

				if (OSTYPE == 2) {
					if (find_templ_try == 1) {
						runIMScripts(_inv_header_fn, "0", false);
					}
					if (find_templ_try == 2) {
						runIMScripts(_inv_header_fn, "1", true);
					}
				}

				String _rt = OCREngineUtils.runTesseract(_inv_header_fn, "-l tur+dim");
				FileUtils.writeStringToFile(new File(_inv_header_fn.replace(".tiff", ".out.txt")), _rt);
				if (new File(_inv_header_fn.replace(".tiff", ".out.txt")).exists()) {
					List<String> taxNoLineList = new ArrayList<String>();
					List<String> taxNoList = new ArrayList<String>();
					List<String> taxKwList = new ArrayList<String>();
					String _head_content = null;
					String _kw = null;
					LineIterator it = null;
					try {
						it = FileUtils.lineIterator(new File(_inv_header_fn.replace(".tiff", ".out.txt")), "UTF-8");
					} catch (IOException e) {
						e.printStackTrace();
					}
					while (it != null && it.hasNext()) {
						String _data = it.nextLine();
						_data = _data.trim().replace(" ", "").replace("_", "").replace("\\/", "V").replace("\\I", "V").replace("Y.", "V.").replace(".0.", ".D.")
								.replace(".O.", ".D.").replace("J)", "D").replace("]", "1").replace("|", "1").replace("[", "1").replace("]", "I");
						// // System.out.println("LINEFORLINETEMPL:" + _data);
						for (String s : TAXNOKEYWORDS) {
							if (_data.indexOf(s) > -1) {
								taxKwList.add(s);
								taxNoLineList.add(_data);
								// System.out.println("KW:" + s);
							
							}
						}
					}
					LineIterator.closeQuietly(it);
					System.err.println("TAX NO LINE LIST SIZE : "+taxNoLineList.size());
					for (int i = 0; i < taxNoLineList.size(); i++) {
						_head_content = taxNoLineList.get(i);
						_kw = taxKwList.get(i);
						if (_kw != null && _head_content.indexOf(_kw) > -1) {
							_head_content =_head_content.replace(")", "");
							_head_content =_head_content.replace("(", "");
							int _end_index = _head_content.indexOf(_kw) + (12 + _kw.length());
							if (_end_index > _head_content.length()) {
								_end_index = _head_content.length();
							}
							String _taxn = _head_content.substring(_head_content.indexOf(_kw) + _kw.length(), _end_index);
							_taxn = _taxn.replace(_kw, "");
							_taxn = _taxn.replace(",", "");
							_taxn = _taxn.replace(".", "");
							_taxn = _taxn.replace(":", "");
							//Atalay start
							_taxn = _taxn.replace("-CO", "");
							
							if(_taxn.contains("Cont")) {
								
							} else {
								_taxn = _taxn.replace("Co", "");
							}
							_taxn = _taxn.replace("-C", "");
							//Atalay finish
							
//							_taxn = _taxn.replace("s", "5");
//							_taxn = _taxn.replace("S", "5");
//							_taxn = _taxn.replace("l", "1");
//							_taxn = _taxn.replace("|", "1");
//							_taxn = _taxn.replace("B", "8");
//							_taxn = _taxn.replace("D", "0");
//							_taxn = _taxn.replace("Z", "2");
//							_taxn = _taxn.replace("z", "2");
//							_taxn = _taxn.replace("G", "6");
//							_taxn = _taxn.replaceAll("[^\\d]", "");
							// System.out.println("TAXN" + i + ":" + _taxn);
//							if (_taxn.length() >= 10) {
//								_taxn = _taxn.substring(0, 10);
//							} else {
//								_taxn = _taxn.substring(0, _taxn.length());
//							}
							taxNoList.add(_taxn);
						}
					}
					if (taxNoList.size() > 0) {
						int isPdforScan =0 ;
						PdfReader reader = new PdfReader(_psfnPdf);
					    try {
					    	
							
						    
					        int n = reader.getXrefSize();
					        PdfObject object;
					        PdfDictionary font;

					        for (int i = 0; i < n; i++) {
					            object = reader.getPdfObject(i);
					            if (object == null || !object.isDictionary()) {
					                 continue;
					            }

					            font = (PdfDictionary)object;

					            if (font.get(PdfName.BASEFONT) != null) {
					                
					            	isPdforScan = 1;
					            }

					        }


					    } catch (Exception e) {
					        System.out.println("error " + e.getMessage());
					        reader.close();
					    }
						//Collections.sort(taxNoList, new StringLengthComparator());
						r[0] = taxNoList.get(0);
						
						
						//template find disabled start
//						List<Tfdim003> _tmpl = dimsrv.getTemplateByTaxn(taxNoList.get(0));
//						if (_tmpl != null && _tmpl.size() > 0) {
//							r[1] = _tmpl;
//						}
//						else {
//							List<Tfdim003> _tmpl22 = null;
//							System.err.println("VAT NUMBER : "+taxNoList.toString());
//							// Atalay Cakisan VatRegNo ve VatReg ler i�in if baslangic
//							if(taxNoList.get(0).substring(0, 2).equals("No")) {
//								if(taxNoList.get(0).substring(0, 3).equals("No:")) {
//									_tmpl22 = dimsrv.getTemplateByTaxn(taxNoList.get(0).substring(3, taxNoList.get(0).length()));
//								} else {
//									_tmpl22 = dimsrv.getTemplateByTaxn(taxNoList.get(0).substring(2, taxNoList.get(0).length()-1));
//								}
//								
//							} else {
//								_tmpl22 = dimsrv.getTemplateByTaxn(taxNoList.get(0).substring(0, taxNoList.get(0).length()-1));
//								
//							}
//							
//							
//							// Atalay bitis
//							if (_tmpl22 != null && _tmpl22.size() > 0) {
//								r[1] = _tmpl22;
//							}
//							else {
//								
//								List<Tfdim003> _tmpl23 = dimsrv.getTemplateByTaxn(taxNoList.get(0).substring(0, taxNoList.get(0).length()-2));
//								if (_tmpl23 != null && _tmpl23.size() > 0) {
//									r[1] = _tmpl23;
//								}
//								else  {
//									_tmpl23 = dimsrv.getTemplateByTaxn(taxNoList.get(0).substring(0, taxNoList.get(0).length()-3));
//									if (_tmpl23 != null && _tmpl23.size() > 0) {
//										r[1] = _tmpl23;
//									}
//									else {
//										List<Tfdim003> _tmpl1 = null;
//										String taxNOinFor = null;
//										for(String taxNo: taxNoList) {
//											taxNOinFor= taxNo;
//											_tmpl1 = dimsrv.getTemplateByTaxn(taxNo.trim());
//											if (_tmpl1 != null && _tmpl1.size() > 0) {
//												r[0] = taxNo;
//												r[1] = _tmpl1;
//												break;
//											}
//										}
//										if(_tmpl1==null){
//											System.out.println(taxNOinFor + " VatNo not Found " );
//										}
//									}
//								}
//								
//								
//								
//							}
//							
//							
//						}
						//template find disabled start
					}else {
						System.out.println("VatNo string not Found 1236" );
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//dimsrv.close();
			
			
//			try {
//				org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(_psfn.replace(".pdf", ".png")));
//				org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(_psfn.replace(".pdf", ".tiff")));
//				org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(_psfn.replace(".pdf", ".zip")));
//
//				org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(_inv_header_fn));
//				org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(_inv_header_fn.replace(".tiff", ".zip")));
//				org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(_inv_header_fn.replace(".tiff", ".out.txt")));
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
		// if(r[0]==null || (r[0]!=null && ((String)r[0]).trim().length() < 10) ){
		// if(find_templ_try==1){
		// find_templ_try++;
		// r=findTemplate(invoiceFilePath);
		// }
		// }

		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.eintecon.service.InvoiceParser#findTemplate(java.lang.String)
	 */
	public Object[] findTemplate_old(String invoiceFilePath) {
		String[] TAXNOKEYWORDS = readVatNoTypingTypes() ;
		Object[] r = new Object[2];
		r[0] = null;
		r[1] = null;
		//DimService dimsrv = new DimService();
		String _inv_header_fn = "";

		String _pre_dir = globalContextPath + "V2/" + invoiceFilePath.substring(0, invoiceFilePath.lastIndexOf("/") + 1);// xml/einvoice/COMP"+((Tfedf000)session.getAttribute("Company")).getComp()+"/ETC/";
		String _psfn = _pre_dir + invoiceFilePath.substring(invoiceFilePath.lastIndexOf("/") + 1);
		ImageIO.scanForPlugins();
		try {

			OCREngineUtils.convertPDFToTiff(_psfn, "tiff");

			if (new java.io.File(_psfn.replace(".pdf", ".tiff")).exists()) {
				BufferedImage bi = ImageIO.read(new FileInputStream(new java.io.File(_psfn.replace(".pdf", ".tiff"))));

				BufferedImage bi_1 = bi.getSubimage(0, 0, bi.getWidth() - 10, 1500);
				_inv_header_fn = _psfn.substring(0, _psfn.lastIndexOf(".")) + "_header.tiff";
				ImageIO.write(bi_1, "tiff", new File(_inv_header_fn));

				if (OSTYPE == 2) {
					if (find_templ_try == 1) {
						runIMScripts(_inv_header_fn, "0", false);
					}
					if (find_templ_try == 2) {
						runIMScripts(_inv_header_fn, "1", true);
					}
				}

				String _rt = OCREngineUtils.runTesseract(_inv_header_fn, "-l tur+dim");
				FileUtils.writeStringToFile(new File(_inv_header_fn.replace(".tiff", ".out.txt")), _rt);
				if (new File(_inv_header_fn.replace(".tiff", ".out.txt")).exists()) {
					String _head_content = null;

					LineIterator it = null;
					try {
						it = FileUtils.lineIterator(new File(_inv_header_fn.replace(".tiff", ".out.txt")), "UTF-8");
					} catch (IOException e) {
						e.printStackTrace();
					}
					String _kw = null;
					while (it != null && it.hasNext()) {
						String _data = it.nextLine();
						_data = _data.trim().replace(" ", "").replace("_", "").replace("\\/", "V").replace("\\I", "V").replace("Y.", "V.").replace(".0.", ".D.")
								.replace(".O.", ".D.");
						// System.out.println("LINEFORLINETEMPL:" + _data);

						for (String s : TAXNOKEYWORDS) {
							if (_data.indexOf(s) > -1) {
								_kw = s;
								_head_content = _data;
								// System.out.println("KW:" + _kw);
								break;
							}
						}
						if (_head_content != null) {
							break;
						}
					}
					LineIterator.closeQuietly(it);

					if (_kw != null && _head_content.indexOf(_kw) > -1) {
						int _end_index = _head_content.indexOf(_kw) + (12 + _kw.length());
						if (_end_index > _head_content.length()) {
							_end_index = _head_content.length();
						}
						String _taxn = _head_content.substring(_head_content.indexOf(_kw) + _kw.length(), _end_index);
						_taxn = _taxn.replace(_kw, "");
						_taxn = _taxn.replace("O", "0");
						_taxn = _taxn.replace("o", "0");
						_taxn = _taxn.replace("s", "5");
						_taxn = _taxn.replace("S", "5");
						_taxn = _taxn.replace("l", "1");
						_taxn = _taxn.replace("|", "1");
						_taxn = _taxn.replace("B", "8");
						_taxn = _taxn.replace("D", "0");
						_taxn = _taxn.replace("Z", "2");
						_taxn = _taxn.replace("z", "2");
						_taxn = _taxn.replace("G", "6");
						_taxn = _taxn.replaceAll("[^\\d]", "");
						// System.out.println("TAXN:" + _taxn);
						if (_taxn.length() >= 10) {
							_taxn = _taxn.substring(0, 10);
						} else {
							_taxn = _taxn.substring(0, _taxn.length());
						}
						r[0] = _taxn;
						//template find 2 disabled start
//						List<Tfdim003> _tmpl = dimsrv.getTemplateByTaxn(_taxn);
//						if (_tmpl != null && _tmpl.size() > 0) {
//							r[1] = _tmpl;
//						}
						//template find 2 disabled start
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// dimsrv.close();
			// delete file
			if (DimConfig.getInstance().getPropertyValue("OCRENGINEWSLINK").indexOf("10.157.20.20") > -1) { // just for production
				// org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(_psfn.replace(".pdf", ".png")));
				org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(_psfn.replace(".pdf", ".tiff")));
				org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(_psfn.replace(".pdf", ".zip")));

				org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(_inv_header_fn));
				org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(_inv_header_fn.replace(".tiff", ".zip")));
				org.apache.commons.io.FileUtils.deleteQuietly(new java.io.File(_inv_header_fn.replace(".tiff", ".out.txt")));
			}
		}
		// if(r[0]==null || (r[0]!=null && ((String)r[0]).trim().length() < 10) ){
		// if(find_templ_try==1){
		// find_templ_try++;
		// r=findTemplate(invoiceFilePath);
		// }
		// }

		return r;
	}
	//parseSelectedArea disabled start
/*
	public GenericReturnTO parseSelectedArea(String fid, String templateId, Tfedf000 comp, String servlet_context_path, int top, int left, int width, int height) {
		ImageIO.scanForPlugins();
		// System.out.println("parseSelectedArea GIRDI");
		GenericReturnTO r = new GenericReturnTO();

		//DimService dimsrv = null;
		try {
			//dimsrv = new DimService();
			//Tfdim001 _diminv = dimsrv.getDimInvoiceById(Integer.parseInt(fid));

			//_pre_dir disabled start
			//String _pre_dir = globalContextPath + "V2/" + _diminv.getInvpath().substring(0, _diminv.getInvpath().lastIndexOf("/") + 1);// xml/einvoice/COMP"+((Tfedf000)session.getAttribute("Company")).getComp()+"/ETC/";
			//String _psfn = _pre_dir + _diminv.getInvpath().substring(_diminv.getInvpath().lastIndexOf("/") + 1);
			String _pre_dir ="";
			String _psfn = "";
			//_pre_dir disabled end
			
			Pattern dec_pat = Pattern.compile("[0-9]+\\.?[0-9]+(\\,[0-9][0-9]?)?");
			DecimalFormat df = new DecimalFormat();
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			try {
				symbols.setDecimalSeparator(',');
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			symbols.setGroupingSeparator('.');
			df.setDecimalFormatSymbols(symbols);

			BufferedImage bi = ImageIO.read(new java.io.File(_psfn.replace(".pdf", ".tiff")));
			String _tesslang = "dim";

			Tfdim003 _tmpl = null;
			if (!templateId.startsWith("[TAXNO]")) {
				_tmpl = dimsrv.getTemplateById(Integer.parseInt(templateId));
			}
			if (_tmpl != null) {
				_tesslang = findInvoiceConfByTaxNo(_tmpl.getSuppltaxn(), "TESSLANG");

				OCRTemplateRequest ocrt = null;
				Gson gson = new Gson();
				ocrt = (OCRTemplateRequest) gson.fromJson(_tmpl.getTemplmap(), OCRTemplateRequest.class);
				gson = null;

				if (ocrt != null) {
					BufferedImage bi_2 = bi.getSubimage(left, top, width, height);
					String _inv_part_fn = _psfn.substring(0, _psfn.lastIndexOf(".")) + "_selected_area" + ".tiff";
					ImageIO.write(bi_2, "tiff", new File(_inv_part_fn));

					String _line_txt = doOcr2(_inv_part_fn, ocrt.getIsdotmatrix(), _tesslang, false);

					if (!(_line_txt != null && _line_txt.trim().length() > 0)) {
						_line_txt = doOcr2(_inv_part_fn, ocrt.getIsdotmatrix(), _tesslang, true);
					}

					if (_line_txt != null && _line_txt.trim().length() > 0) {
						Matcher m = dec_pat.matcher(_line_txt.trim().replace(" ", ""));
						if (m.matches()) {
							r.setRetval("" + df.parse(_line_txt.trim().replace(" ", "")));
						} else {
							r.setRetval(_line_txt.trim());
						}

					}
				}
			} else {
				_tesslang = findInvoiceConfByTaxNo("%", "TESSLANG");
				String _isdm = findInvoiceConfByTaxNo("%", "ISDM");
				BufferedImage bi_2 = bi.getSubimage(left, top, width, height);
				String _inv_part_fn = _psfn.substring(0, _psfn.lastIndexOf(".")) + "_selected_area" + ".tiff";
				ImageIO.write(bi_2, "tiff", new File(_inv_part_fn));

				String _line_txt = doOcr2(_inv_part_fn, _isdm, _tesslang, false);

				if (!(_line_txt != null && _line_txt.trim().length() > 0)) {
					_line_txt = doOcr2(_inv_part_fn, _isdm, _tesslang, true);
				}

				if (_line_txt != null && _line_txt.trim().length() > 0) {
					Matcher m = dec_pat.matcher(_line_txt.trim().replace(" ", ""));
					if (m.matches()) {
						r.setRetval("" + df.parse(_line_txt.trim().replace(" ", "")));
					} else {
						r.setRetval(_line_txt.trim());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}
*/
	//parseSelectedArea disabled end
	
	//prepareOcrTraining disabled start
	/*
	public List<OCRTrainingLine> prepareOcrTraining(int templateId, String _psfn, boolean setOffset, int offsetTop, int offsetLeft) {
		List<OCRTrainingLine> ocrTrainingLines = null;

		int xPos = 0, yPos = 0, w = 0, h = 0;
		ImageIO.scanForPlugins();

		DimService dimsrv = null;
		FileInputStream fis = null;

		try {
			dimsrv = new DimService();

			// String _pre_dir = globalContextPath + "V2/" + _psfn.substring(0, _psfn.lastIndexOf("/") + 1);// xml/einvoice/COMP"+((Tfedf000)session.getAttribute("Company")).getComp()+"/ETC/";

			if (!new java.io.File(_psfn.replace(".pdf", ".tiff")).exists()) {
				OCREngineUtils.convertPDFToTiff(_psfn, "tiff");
			}

			if (new java.io.File(_psfn.replace(".pdf", ".tiff")).exists()) {
				fis = new FileInputStream(new java.io.File(_psfn.replace(".pdf", ".tiff")));
				BufferedImage bi = ImageIO.read(fis);

				Tfdim003 _tmpl = null;
				if (templateId > 0) {
					_tmpl = dimsrv.getTemplateById(templateId);
				}
				if (_tmpl != null) {
					String _tesslang = findInvoiceConfByTaxNo(_tmpl.getSuppltaxn(), "TESSLANG");

					OCRTemplateRequest ocrt = null;
					Gson gson = new Gson();
					ocrt = (OCRTemplateRequest) gson.fromJson(_tmpl.getTemplmap(), OCRTemplateRequest.class);
					gson = null;

					if (ocrt != null) {
						DecimalFormat df = new DecimalFormat();
						DecimalFormatSymbols symbols = new DecimalFormatSymbols();
						try {
							symbols.setDecimalSeparator(ocrt.getSep_decimal().charAt(0));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						try {
							symbols.setGroupingSeparator(ocrt.getSep_1000().charAt(0));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						df.setDecimalFormatSymbols(symbols);

						List<OCRTemplateMapItem> tmis = ocrt.getMapping();

						OCRTemplateMapItem line_end_tmi = null;
						OCRTemplateMapItem inv_line_height = null;
						OCRTemplateMapItem inv_line_first_element = null;
						OCRTemplateMapItem inv_line_last_element = null;
						BufferedImage _inv_lines_area_bi = null;
						OCRTrainingField ocrTrainingField = null;
						int _line_cnt = 0;

						int default_offset = 10;

						String _inv_line_top = "0";
						String _inv_line_height = "0";

						if (setOffset) {
							int _offset_top = 0;
							int _offset_left = 0;
							for (OCRTemplateMapItem tmi : tmis) {
								if (tmi.getFieldId().equals("inv_date")) {
									_offset_top = offsetTop - Integer.parseInt(tmi.getTop());
									_offset_left = offsetLeft - Integer.parseInt(tmi.getLeft());
									break;
								}
							}
							// System.out.println("OSTOP: " + _offset_top + ", OSLEFT: " + offsetLeft);
							for (OCRTemplateMapItem tmi : tmis) {
								tmi.setTop("" + (Integer.parseInt(tmi.getTop()) + _offset_top));
								tmi.setLeft("" + (Integer.parseInt(tmi.getLeft()) + _offset_left));
							}
						}

						for (OCRTemplateMapItem tmi : tmis) {
							tmi.setTop("" + (Integer.parseInt(tmi.getTop()) - default_offset));
							tmi.setLeft("" + (Integer.parseInt(tmi.getLeft()) - default_offset));
							if (tmi.getFieldId().equals("inv_line_total")) {// inv_line_item_code
								_inv_line_top = tmi.getTop();
								_inv_line_height = tmi.getHeight();
							}
							if (tmi.getFieldId().equals(ocrt.getLine_first_col_tmi())) {// inv_line_item_code
								inv_line_first_element = tmi;
							}
							if (tmi.getFieldId().equals(ocrt.getLine_last_col_tmi())) {// inv_line_item_code
								inv_line_last_element = tmi;
							}
						}
						for (OCRTemplateMapItem tmi : tmis) {
							if (tmi.getFieldId().startsWith("inv_line_")) {
								tmi.setTop(_inv_line_top);
								tmi.setHeight(_inv_line_height);
							}
							if (tmi.getFieldId().equals(ocrt.getLine_end_tmi())) {// if (tmi.getFieldId().equals("inv_line_price")) {// if(tmi.getFieldId().equals("inv_line_total")){
								line_end_tmi = tmi;
							}
							if (tmi.getFieldId().equals("line_height")) {// if(tmi.getFieldId().equals("inv_line_total")){
								inv_line_height = tmi;
							}
						}

						if (inv_line_height == null) {
							inv_line_height = new OCRTemplateMapItem();
							inv_line_height.setHeight("0");
						}

						if (line_end_tmi != null) {

							String _line_txt = "";
							do {
								_line_txt = "";

								BufferedImage bi_2 = bi.getSubimage(Integer.parseInt(line_end_tmi.getLeft()),
										(Integer.parseInt(line_end_tmi.getTop()) + (_line_cnt * (Integer.parseInt(line_end_tmi.getHeight()) + Integer
												.parseInt(inv_line_height.getHeight())))), Integer.parseInt(line_end_tmi.getWidth()), Integer.parseInt(line_end_tmi
												.getHeight()));
								String _inv_part_fn = _psfn.substring(0, _psfn.lastIndexOf(".")) + "_lf_" + _line_cnt + "_" + line_end_tmi.getFieldId() + ".tiff";
								ImageIO.write(bi_2, "tiff", new File(_inv_part_fn));

								_line_txt = doOcr2(_inv_part_fn, ocrt.getIsdotmatrix(), _tesslang, false);

								if (!(_line_txt != null && _line_txt.trim().length() > 0)) {
									_line_txt = doOcr2(_inv_part_fn, ocrt.getIsdotmatrix(), _tesslang, true);
								}

								if (_line_txt != null && _line_txt.trim().length() > 0) {
									_line_cnt++;
								}
							} while (_line_txt != null && _line_txt.trim().length() > 0);
						}
						if (_line_cnt == 0) {
							_line_cnt = 1;
						}
						if (_line_cnt > 0) {

							ocrTrainingLines = new ArrayList<OCRTrainingLine>();
							for (int i = 0; i < _line_cnt; i++) {
								ocrTrainingLines.add(new OCRTrainingLine());
							}

							// satirlar bolgesini kes hocr ile tara
							_inv_lines_area_bi = bi.getSubimage(Integer.parseInt(inv_line_first_element.getLeft()), Integer.parseInt(inv_line_first_element.getTop()) - 10,
									((Integer.parseInt(inv_line_last_element.getLeft()) + Integer.parseInt(inv_line_last_element.getWidth())) - Integer
											.parseInt(inv_line_first_element.getLeft())) + 10, (_line_cnt * (Integer.parseInt(inv_line_first_element.getHeight()))) + 10);

							String _inv_part_fn = _psfn.substring(0, _psfn.lastIndexOf(".")) + "_" + 0 + "_" + "inv_line_area" + ".tiff";
							ImageIO.write(_inv_lines_area_bi, "tiff", new File(_inv_part_fn));

							if (ocrt.getIsdotmatrix().equals("1"))
								OCRUtils.runIMScripts(_inv_part_fn, ocrt.getIsdotmatrix(), false);

							String _rt = OCREngineUtils.runTesseract(_inv_part_fn, "-l tur " + ((_tesslang != null) ? "-l " + _tesslang : "") + " -psm 6 hocr");
							FileUtils.writeStringToFile(new File(_inv_part_fn.replace(".tiff", ".out.html")), _rt);
							LineIterator it = FileUtils.lineIterator(new File(_inv_part_fn.replace(".tiff", ".out.html")), "UTF-8");
							int _lastii = 1;
							while (it.hasNext()) {
								String _line_txt = it.nextLine().trim();
								for (int _ii = 1; _ii <= _line_cnt; _ii++) {
									String _st_word = "span class='ocr_line' id='line_" + _ii + "'";
									if (_line_txt != null && _line_txt.indexOf(_st_word) > -1) {
										int _st1 = _line_txt.indexOf(_st_word) + _st_word.length();
										_st_word = _line_txt.substring(_st1 + 1, _line_txt.indexOf(">"));
										_st_word = _st_word.replace("\"", "");
										_st_word = _st_word.replace("'", "");
										_st_word = _st_word.replace("title=bbox ", "");
										String _tmp_arr[] = _st_word.split(Pattern.quote(" "));
										if (_tmp_arr != null && _tmp_arr.length == 4) {
											ocrTrainingLines.get(_ii - 1).setTop((Integer.parseInt(inv_line_first_element.getTop()) + Integer.parseInt(_tmp_arr[1])) - 10);
											ocrTrainingLines.get(_ii - 1).setHeight((Integer.parseInt(_tmp_arr[3]) - Integer.parseInt(_tmp_arr[1])) + 10);
											_lastii = _ii;
										}
									}
								}
							}
							LineIterator.closeQuietly(it);
							if (ocrTrainingLines != null && _lastii != ocrTrainingLines.size()) {
								ArrayList<OCRTrainingLine> _tmp_list = new ArrayList<OCRTrainingLine>();
								for (int _ii = 0; _ii < ocrTrainingLines.size(); _ii++) {
									if (_ii < _lastii) {
										_tmp_list.add(ocrTrainingLines.get(_ii));
									}
								}
								ocrTrainingLines = _tmp_list;
								_line_cnt = ocrTrainingLines.size();
							}
						}

						for (OCRTemplateMapItem tmi : tmis) {

							if (tmi.getFieldId() != null) {
								if (tmi.getFieldId().startsWith("inv_line_")) {

									for (int ii = 0; ii < _line_cnt; ii++) {
										ocrTrainingField = new OCRTrainingField();
										ocrTrainingLines.get(ii).setLineNo(ii + 1);
										if (ocrTrainingLines.get(ii).getOcrTrainingFields() == null) {
											ocrTrainingLines.get(ii).setOcrTrainingFields(new ArrayList<OCRTrainingField>());
										}
										// System.out.println(tmi.getLeft() + " " + ocrTrainingLines.get(ii).getTop() + " " + tmi.getWidth() + " "
										// + ocrTrainingLines.get(ii).getHeight());

										BufferedImage bi_2 = bi.getSubimage(Integer.parseInt(tmi.getLeft()), ocrTrainingLines.get(ii).getTop(),
												Integer.parseInt(tmi.getWidth()), ocrTrainingLines.get(ii).getHeight());

										String _inv_part_fn = _psfn.substring(0, _psfn.lastIndexOf(".")) + "_" + ii + "_" + tmi.getFieldId() + ".tiff";
										ImageIO.write(bi_2, "tiff", new File(_inv_part_fn));

										String _rt = OCREngineUtils.runTesseract(_inv_part_fn, "-l tur " + ((_tesslang != null) ? "-l " + _tesslang : "") + " -psm 6 hocr");
										FileUtils.writeStringToFile(new File(_inv_part_fn.replace(".tiff", ".out.html")), _rt);
										LineIterator it = FileUtils.lineIterator(new File(_inv_part_fn.replace(".tiff", ".out.html")), "UTF-8");
										boolean coordinationFounded = false;
										while (it.hasNext()) {
											String _line_txt = it.nextLine().trim();
											String _st_word = "span class='ocr_line' id='line_1'";
											if (_line_txt != null && _line_txt.indexOf(_st_word) > -1) {
												int _st1 = _line_txt.indexOf(_st_word) + _st_word.length();
												_st_word = _line_txt.substring(_st1 + 1, _line_txt.indexOf(">"));
												_st_word = _st_word.replace("\"", "");
												_st_word = _st_word.replace("'", "");
												_st_word = _st_word.replace("title=bbox ", "");
												String _tmp_arr[] = _st_word.split(Pattern.quote(" "));
												if (_tmp_arr != null && _tmp_arr.length == 4) {
													xPos = Integer.parseInt(_tmp_arr[0]);
													yPos = Integer.parseInt(_tmp_arr[1]);
													w = Integer.parseInt(_tmp_arr[2]);
													h = Integer.parseInt(_tmp_arr[3]);
													coordinationFounded = true;
													break;
												}
											}
										}
										LineIterator.closeQuietly(it);
										if (!coordinationFounded) {
											xPos = 0;
											yPos = 0;
											w = bi_2.getWidth();
											h = bi_2.getHeight();
										}
										// System.out.println(bi_2.getMinX() + " " + bi_2.getMinY() + " " + bi_2.getWidth() + " " + bi_2.getHeight());
										// System.out.println(xPos + " " + yPos + " " + w + " " + h);

										BufferedImage newbi_2 = bi_2.getSubimage(xPos, yPos, w - xPos, h - yPos);

										String new_inv_part_fn = _psfn.substring(0, _psfn.lastIndexOf(".")) + "_" + ii + "_" + tmi.getFieldId() + "_new.tiff";
										ImageIO.write(newbi_2, "tiff", new File(new_inv_part_fn));

										// to scale the image 50% of the original size
										Image tmp = newbi_2.getScaledInstance(newbi_2.getWidth() / 2, newbi_2.getHeight() / 2, Image.SCALE_SMOOTH);
										newbi_2 = new BufferedImage(newbi_2.getWidth() / 2, newbi_2.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
										Graphics2D g2d = newbi_2.createGraphics();
										g2d.drawImage(tmp, 0, 0, null);
										g2d.dispose();
										ImageIO.write(newbi_2, "png", new File(new_inv_part_fn.replace(".tiff", ".png")));

										ocrTrainingField.setFieldImgPath(new_inv_part_fn.replace(globalContextPath, "../../../DIM/").replace(".tiff", ".png"));

										if (tmi.getFieldId().equals("inv_line_item_code")) {
											ocrTrainingField.setFieldName("Item Code");
										}
										if (tmi.getFieldId().equals("inv_line_item")) {
											ocrTrainingField.setFieldName("Item Desc");
										}
										if (tmi.getFieldId().equals("inv_line_unit")) {
											ocrTrainingField.setFieldName("Unit Code");
										}
										if (tmi.getFieldId().equals("inv_line_qty")) {
											ocrTrainingField.setFieldName("Qty");
										}
										if (tmi.getFieldId().equals("inv_line_price")) {
											ocrTrainingField.setFieldName("Unit Price");
										}
										if (tmi.getFieldId().equals("inv_line_total")) {
											ocrTrainingField.setFieldName("Total");
										}

										if (ocrTrainingField.getFieldName() != null && ocrTrainingField.getFieldName().length() > 0)
											ocrTrainingLines.get(ii).getOcrTrainingFields().add(ocrTrainingField);
									}
								}
							}
						}

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dimsrv.close();
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return ocrTrainingLines;
	}
	*/
	//prepareOcrTraining disabled end
	
	
	//prepareOcrChosenTraining disabled start
	/*
	public List<OCRTrainingLine> prepareOcrChosenTraining(int templateId, String invoiceFilePath, int offsetTop, int offsetLeft, int offsetWidth, int offsetHeighht, String fieldId) {
		List<OCRTrainingLine> ocrTrainingLines = null;
		 
		int xPos = 0, yPos = 0, w = 0, h = 0;
		ImageIO.scanForPlugins();

		DimService dimsrv = null;
		FileInputStream fis = null;

		try {
			dimsrv = new DimService();

			// String _pre_dir = globalContextPath + "V2/" + _psfn.substring(0, _psfn.lastIndexOf("/") + 1);// xml/einvoice/COMP"+((Tfedf000)session.getAttribute("Company")).getComp()+"/ETC/";
			System.out.println("invoiceFilePath :" + invoiceFilePath);
			if (!new java.io.File(invoiceFilePath.replace(".pdf", ".tiff")).exists()) {
				OCREngineUtils.convertPDFToTiff(invoiceFilePath, "tiff");
			}

			if (new java.io.File(invoiceFilePath.replace(".pdf", ".tiff")).exists()) {
				fis = new FileInputStream(new java.io.File(invoiceFilePath.replace(".pdf", ".tiff")));
				BufferedImage bi = ImageIO.read(fis);

				Tfdim003 _tmpl = null;
				if (templateId > 0) {
					_tmpl = dimsrv.getTemplateById(templateId);
				}
				if (_tmpl != null) {
					String _tesslang = findInvoiceConfByTaxNo(_tmpl.getSuppltaxn(), "TESSLANG");

					OCRTemplateRequest ocrt = null;
					Gson gson = new Gson();
					ocrt = (OCRTemplateRequest) gson.fromJson(_tmpl.getTemplmap(), OCRTemplateRequest.class);
					gson = null;

					if (ocrt != null) {
						DecimalFormat df = new DecimalFormat();
						DecimalFormatSymbols symbols = new DecimalFormatSymbols();
						try {
							symbols.setDecimalSeparator(ocrt.getSep_decimal().charAt(0));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						try {
							symbols.setGroupingSeparator(ocrt.getSep_1000().charAt(0));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						df.setDecimalFormatSymbols(symbols);

						List<OCRTemplateMapItem> tmis = ocrt.getMapping();

						OCRTemplateMapItem line_end_tmi = null;
						OCRTemplateMapItem inv_line_height = null;
						OCRTemplateMapItem inv_line_first_element = null;
						OCRTemplateMapItem inv_line_last_element = null;
						BufferedImage _inv_lines_area_bi = null;
						OCRTrainingField ocrTrainingField = null;
						int _line_cnt = 0;

						int default_offset = 10;

						String _inv_line_top = "0";
						String _inv_line_height = "0"; 
 
						for (OCRTemplateMapItem tmi : tmis) {
							tmi.setTop("" + (Integer.parseInt(tmi.getTop()) - default_offset));
							tmi.setLeft("" + (Integer.parseInt(tmi.getLeft()) - default_offset));
							if (tmi.getFieldId().equals("inv_line_total")) {// inv_line_item_code
								_inv_line_top = tmi.getTop();
								_inv_line_height = tmi.getHeight();
							}
							if (tmi.getFieldId().equals(ocrt.getLine_first_col_tmi())) {// inv_line_item_code
								inv_line_first_element = tmi;
							}
							if (tmi.getFieldId().equals(ocrt.getLine_last_col_tmi())) {// inv_line_item_code
								inv_line_last_element = tmi;
							}
						}
						for (OCRTemplateMapItem tmi : tmis) {
							if (tmi.getFieldId().startsWith("inv_line_")) {
								tmi.setTop(_inv_line_top);
								tmi.setHeight(_inv_line_height);
							}
							if (tmi.getFieldId().equals(ocrt.getLine_end_tmi())) {// if (tmi.getFieldId().equals("inv_line_price")) {// if(tmi.getFieldId().equals("inv_line_total")){
								line_end_tmi = tmi;
							}
							if (tmi.getFieldId().equals("line_height")) {// if(tmi.getFieldId().equals("inv_line_total")){
								inv_line_height = tmi;
							}
						}

						if (inv_line_height == null) {
							inv_line_height = new OCRTemplateMapItem();
							inv_line_height.setHeight("0");
						}

						if (line_end_tmi != null) {

							String _line_txt = "";
							do {
								_line_txt = "";

								BufferedImage bi_2 = bi.getSubimage(Integer.parseInt(line_end_tmi.getLeft()),
										(Integer.parseInt(line_end_tmi.getTop()) + (_line_cnt * (Integer.parseInt(line_end_tmi.getHeight()) + Integer
												.parseInt(inv_line_height.getHeight())))), Integer.parseInt(line_end_tmi.getWidth()), Integer.parseInt(line_end_tmi
												.getHeight()));
								String _inv_part_fn = invoiceFilePath.substring(0, invoiceFilePath.lastIndexOf(".")) + "_lf_" + _line_cnt + "_" + line_end_tmi.getFieldId() + ".tiff";
								ImageIO.write(bi_2, "tiff", new File(_inv_part_fn));

								_line_txt = doOcr2(_inv_part_fn, ocrt.getIsdotmatrix(), _tesslang, false);

								if (!(_line_txt != null && _line_txt.trim().length() > 0)) {
									_line_txt = doOcr2(_inv_part_fn, ocrt.getIsdotmatrix(), _tesslang, true);
								}

								if (_line_txt != null && _line_txt.trim().length() > 0) {
									_line_cnt++;
								}
							} while (_line_txt != null && _line_txt.trim().length() > 0);
						}
						if (_line_cnt == 0) {
							_line_cnt = 1;
						}
						if (_line_cnt > 0) {

							ocrTrainingLines = new ArrayList<OCRTrainingLine>();
							for (int i = 0; i < _line_cnt; i++) {
								ocrTrainingLines.add(new OCRTrainingLine());
							}

							// satirlar bolgesini kes hocr ile tara
							_inv_lines_area_bi = bi.getSubimage(Integer.parseInt(inv_line_first_element.getLeft()), Integer.parseInt(inv_line_first_element.getTop()) - 5,
									((Integer.parseInt(inv_line_last_element.getLeft()) + Integer.parseInt(inv_line_last_element.getWidth())) - Integer
											.parseInt(inv_line_first_element.getLeft())) + 10, (_line_cnt * (Integer.parseInt(inv_line_first_element.getHeight()))) + 5);

							String _inv_part_fn = invoiceFilePath.substring(0, invoiceFilePath.lastIndexOf(".")) + "_" + 0 + "_" + "inv_line_area" + ".tiff";
							ImageIO.write(_inv_lines_area_bi, "tiff", new File(_inv_part_fn));

							if (ocrt.getIsdotmatrix().equals("1"))
								OCRUtils.runIMScripts(_inv_part_fn, ocrt.getIsdotmatrix(), false);

							String _rt = OCREngineUtils.runTesseract(_inv_part_fn, "-l tur " + ((_tesslang != null) ? "-l " + _tesslang : "") + " -psm 6 hocr");
							FileUtils.writeStringToFile(new File(_inv_part_fn.replace(".tiff", ".out.html")), _rt);
							LineIterator it = FileUtils.lineIterator(new File(_inv_part_fn.replace(".tiff", ".out.html")), "UTF-8");
							int _lastii = 1;
							while (it.hasNext()) {
								String _line_txt = it.nextLine().trim();
								for (int _ii = 1; _ii <= _line_cnt; _ii++) {
									String _st_word = "span class='ocr_line' id='line_" + _ii + "'";
									if (_line_txt != null && _line_txt.indexOf(_st_word) > -1) {
										int _st1 = _line_txt.indexOf(_st_word) + _st_word.length();
										_st_word = _line_txt.substring(_st1 + 1, _line_txt.indexOf(">"));
										_st_word = _st_word.replace("\"", "");
										_st_word = _st_word.replace("'", "");
										_st_word = _st_word.replace("title=bbox ", "");
										String _tmp_arr[] = _st_word.split(Pattern.quote(" "));
										if (_tmp_arr != null && _tmp_arr.length == 4) {
											ocrTrainingLines.get(_ii - 1).setTop((Integer.parseInt(inv_line_first_element.getTop()) + Integer.parseInt(_tmp_arr[1])) - 10);
											ocrTrainingLines.get(_ii - 1).setHeight((Integer.parseInt(_tmp_arr[3]) - Integer.parseInt(_tmp_arr[1])) + 10);
											_lastii = _ii;
										}
									}
								}
							}
							LineIterator.closeQuietly(it);
							if (ocrTrainingLines != null && _lastii != ocrTrainingLines.size()) {
								ArrayList<OCRTrainingLine> _tmp_list = new ArrayList<OCRTrainingLine>();
								for (int _ii = 0; _ii < ocrTrainingLines.size(); _ii++) {
									if (_ii < _lastii) {
										_tmp_list.add(ocrTrainingLines.get(_ii));
									}
								}
								ocrTrainingLines = _tmp_list;
								_line_cnt = ocrTrainingLines.size();
							}
						}

						for (OCRTemplateMapItem tmi : tmis) {

							if (tmi.getFieldId() != null) {
								if (tmi.getFieldId().startsWith("inv_line_")) {

									for (int ii = 0; ii < _line_cnt; ii++) {
										ocrTrainingField = new OCRTrainingField();
										ocrTrainingLines.get(ii).setLineNo(ii + 1);
										if (ocrTrainingLines.get(ii).getOcrTrainingFields() == null) {
											ocrTrainingLines.get(ii).setOcrTrainingFields(new ArrayList<OCRTrainingField>());
										} 
									 
										BufferedImage bi_2 = null;
										if (tmi.getFieldId().equals(fieldId)) {   
											bi_2 = bi.getSubimage(offsetLeft, ( ocrTrainingLines.get(ii).getTop() + (offsetTop - ocrTrainingLines.get(0).getTop())) ,
													offsetWidth, (offsetHeighht+10));
										}
										else{
											bi_2 = bi.getSubimage(Integer.parseInt(tmi.getLeft()), ocrTrainingLines.get(ii).getTop(),
												Integer.parseInt(tmi.getWidth()), ocrTrainingLines.get(ii).getHeight());
										}

										String _inv_part_fn = invoiceFilePath.substring(0, invoiceFilePath.lastIndexOf(".")) + "_" + ii + "_" + tmi.getFieldId() + ".tiff";
										ImageIO.write(bi_2, "tiff", new File(_inv_part_fn));

										String _rt = OCREngineUtils.runTesseract(_inv_part_fn, "-l tur " + ((_tesslang != null) ? "-l " + _tesslang : "") + " -psm 6 hocr");
										FileUtils.writeStringToFile(new File(_inv_part_fn.replace(".tiff", ".out.html")), _rt);
										LineIterator it = FileUtils.lineIterator(new File(_inv_part_fn.replace(".tiff", ".out.html")), "UTF-8");
										boolean coordinationFounded = false;
										while (it.hasNext()) {
											String _line_txt = it.nextLine().trim();
											String _st_word = "span class='ocr_line' id='line_1'";
											if (_line_txt != null && _line_txt.indexOf(_st_word) > -1) {
												int _st1 = _line_txt.indexOf(_st_word) + _st_word.length();
												_st_word = _line_txt.substring(_st1 + 1, _line_txt.indexOf(">"));
												_st_word = _st_word.replace("\"", "");
												_st_word = _st_word.replace("'", "");
												_st_word = _st_word.replace("title=bbox ", "");
												String _tmp_arr[] = _st_word.split(Pattern.quote(" "));
												if (_tmp_arr != null && _tmp_arr.length == 4) {
													xPos = Integer.parseInt(_tmp_arr[0]);
													yPos = Integer.parseInt(_tmp_arr[1]);
													w = Integer.parseInt(_tmp_arr[2]);
													h = Integer.parseInt(_tmp_arr[3]);
													coordinationFounded = true;
													break;
												}
											}
										}
										LineIterator.closeQuietly(it);
										if (!coordinationFounded) {
											xPos = 0;
											yPos = 0;
											w = bi_2.getWidth();
											h = bi_2.getHeight();
										}
										// System.out.println(bi_2.getMinX() + " " + bi_2.getMinY() + " " + bi_2.getWidth() + " " + bi_2.getHeight());
										// System.out.println(xPos + " " + yPos + " " + w + " " + h);
										
										BufferedImage newbi_2 = bi_2.getSubimage(xPos, yPos, w - xPos, h - yPos);

										String new_inv_part_fn = invoiceFilePath.substring(0, invoiceFilePath.lastIndexOf(".")) + "_" + ii + "_" + tmi.getFieldId() + "_new.tiff";
										ImageIO.write(newbi_2, "tiff", new File(new_inv_part_fn));

										// to scale the image 50% of the original size
										Image tmp = newbi_2.getScaledInstance(newbi_2.getWidth() / 2, newbi_2.getHeight() / 2, Image.SCALE_SMOOTH);
										newbi_2 = new BufferedImage(newbi_2.getWidth() / 2, newbi_2.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
										Graphics2D g2d = newbi_2.createGraphics();
										g2d.drawImage(tmp, 0, 0, null);
										g2d.dispose();
										ImageIO.write(newbi_2, "png", new File(new_inv_part_fn.replace(".tiff", ".png")));

										ocrTrainingField.setFieldImgPath(new_inv_part_fn.replace(globalContextPath, "../../../DIM/").replace(".tiff", ".png"));

										if (tmi.getFieldId().equals("inv_line_item_code")) {
											ocrTrainingField.setFieldName("Item Code");
										}
										if (tmi.getFieldId().equals("inv_line_item")) {
											ocrTrainingField.setFieldName("Item Desc");
										}
										if (tmi.getFieldId().equals("inv_line_unit")) {
											ocrTrainingField.setFieldName("Unit Code");
										}
										if (tmi.getFieldId().equals("inv_line_qty")) {
											ocrTrainingField.setFieldName("Qty");
										}
										if (tmi.getFieldId().equals("inv_line_price")) {
											ocrTrainingField.setFieldName("Unit Price");
										}
										if (tmi.getFieldId().equals("inv_line_total")) {
											ocrTrainingField.setFieldName("Total");
										}

										if (ocrTrainingField.getFieldName() != null && ocrTrainingField.getFieldName().length() > 0)
											ocrTrainingLines.get(ii).getOcrTrainingFields().add(ocrTrainingField);
									}
								}
							}
						}

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dimsrv.close();
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return ocrTrainingLines;
	}
	*/
	//prepareOcrChosenTraining disabled end
	
	//prepareOcrCropping disabled start
	/*
	public List<OCRFieldMember> prepareOcrCropping(int charsCount, String imgPath, String supplierTaxno) {
		List<OCRFieldMember> ocrFieldMembers = new ArrayList<OCRFieldMember>();
		OCRFieldMember ocrFieldMember = null;
		Axet axet = null;
		ImageIO.scanForPlugins();

		// File _pre_dir = new File(globalContextPath + "V2/" + invoiceFilePath.substring(0, invoiceFilePath.lastIndexOf("/") + 1));
		String _psfn = imgPath.substring(imgPath.lastIndexOf("/") + 1);
		imgPath = globalContextPath + "V2/files/dimdata/archive/ocrtraining/" + _psfn;
		FileInputStream fis = null;

		try {
			if (new java.io.File(imgPath).exists()) {
				
				String replacementStr = OCRUtils.findInvoiceConfByTaxNo(supplierTaxno, "REPLACEMENTSTR");
				String replacementListLenStr = OCRUtils.findInvoiceConfByTaxNo(supplierTaxno, "REPLACEMENTLISTLEN");
				int replacementListLen = (replacementListLenStr != null && replacementListLenStr.length() > 0) ? Integer.valueOf(replacementListLenStr).intValue() : 0;

				if (!(replacementListLen == 0)) {
					try {
						axet = new Axet("fonts", "font_" + supplierTaxno, replacementStr, replacementListLen, 0.65f);
					} catch (Exception e) {
					}
				}

				fis = new FileInputStream(new java.io.File(imgPath));
				BufferedImage bi = ImageIO.read(fis);
				int w = bi.getWidth() / charsCount;
				int x = 0;
				double baseDiff = (bi.getWidth() % charsCount) / ((double) charsCount);
				
				
				double xDiff = 0.0;
				for (int i = 1; i <= charsCount; i++) {
					ocrFieldMember = new OCRFieldMember();
					ocrFieldMember.setFMemberId(i); 
					ocrFieldMember.setAvarageWidth(w);
					
					ocrFieldMember.setMainPath(imgPath);
					ocrFieldMember.setCharsCount(charsCount);
					// BufferedImage bi_2 = bi.getSubimage((bi.getMinX() + ((i - 1) * w)), bi.getMinY(), w, bi.getHeight());
					xDiff += baseDiff;
					if (xDiff >= 1) {
						xDiff -= 1;
						x += 1;
					}
					x = (i == 1) ? 0 : x + w;
					ocrFieldMember.setStartPoint(x);
					  
					BufferedImage bi_2 = bi.getSubimage(x, bi.getMinY(), w, bi.getHeight());
					String _inv_part_fn = imgPath.substring(0, imgPath.lastIndexOf(".")) + "_" + i + ".png";
					ImageIO.write(bi_2, "png", new File(_inv_part_fn));
					ocrFieldMember.setImgPath(_inv_part_fn.replace(globalContextPath, "../../../DIM/"));
					if (axet != null)
						ocrFieldMember.setValue(doOcrAxet(bi_2, axet));
					else
						ocrFieldMember.setValue("");
					ocrFieldMembers.add(ocrFieldMember);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return ocrFieldMembers;
	}
	*/
	//prepareOcrCropping disabled end	
	
	// recrop edilmis resimler uzerinde belirtilen yonlerde kayd�rma
	//preparedoRebuiltCropImage disabled start
	/*
			public List<OCRFieldMember> preparedoRebuiltCropImage( String supplierTaxno, int avarageWidth, int startPoint, String pos, int counter,int charsCount,int fmemberId, String mainPath,List<OCRFieldMember> fieldMembers) {
				List<OCRFieldMember> ocrFieldMembers = new ArrayList<OCRFieldMember>();
				OCRFieldMember ocrFieldMember = null;
				Axet axet = null;
				ImageIO.scanForPlugins(); 
				
				BufferedImage bi = null;  
				String _inv_part_fn =""; 
				if(mainPath!=null || mainPath!=""){ 
					String _psfn = mainPath.substring(mainPath.lastIndexOf("/") + 1);
					mainPath = globalContextPath + "V2/files/dimdata/archive/ocrtraining/" + _psfn;
				}
				 
				try {
					    try { 
							bi = ImageIO.read(new File(mainPath));  
						} catch (Exception e) {
							// TODO: handle exception 
							 bi.flush();
						}
						finally{
							 
						} 
					
					if (mainPath!=null) { 
						String replacementStr = OCRUtils.findInvoiceConfByTaxNo(supplierTaxno, "REPLACEMENTSTR");
						String replacementListLenStr = OCRUtils.findInvoiceConfByTaxNo(supplierTaxno, "REPLACEMENTLISTLEN");
						int replacementListLen = (replacementListLenStr != null && replacementListLenStr.length() > 0) ? Integer.valueOf(replacementListLenStr).intValue() : 0;

						if (!(replacementListLen == 0)) {
							try {
								axet = new Axet("fonts", "font_" + supplierTaxno, replacementStr, replacementListLen, 0.65f);
							    } catch (Exception e) {
							}
						} 
						 
						int width=bi.getWidth();
						
						for (int i = 1; i <= fieldMembers.size(); i++) {
							ocrFieldMember = new OCRFieldMember();
							_inv_part_fn="";
							if(i==fmemberId){
								if(pos.equals("left")){ 
									startPoint+=counter;
									int newWidth=startPoint + avarageWidth;
									if(startPoint<=width && newWidth<=width){  
										BufferedImage bi_2 = bi.getSubimage(startPoint, bi.getMinY(),avarageWidth, bi.getHeight()); 
									    _inv_part_fn = mainPath.substring(0, mainPath.lastIndexOf(".")) + "_" + i + ".png";
										ImageIO.write(bi_2, "png", new File(_inv_part_fn)); 
										ocrFieldMember.setAvarageWidth(avarageWidth);
										ocrFieldMember.setStartPoint(startPoint);
										ocrFieldMember.setImgPath(_inv_part_fn.replace(globalContextPath, "../../../DIM/"));
										ocrFieldMember.setCharsCount(charsCount);
										ocrFieldMember.setMainPath(mainPath);
										ocrFieldMember.setFMemberId(i);
										if (axet != null)
											ocrFieldMember.setValue(doOcrAxet(bi_2, axet));
										else
											ocrFieldMember.setValue("");
										ocrFieldMembers.add(ocrFieldMember);
										
										bi_2.flush();
									}
									else{
										startPoint-=counter;
										BufferedImage bi_2 = bi.getSubimage(startPoint, bi.getMinY(),avarageWidth, bi.getHeight()); 
										_inv_part_fn = mainPath.substring(0, mainPath.lastIndexOf(".")) + "_" + i + ".png";
										ImageIO.write(bi_2, "png", new File(_inv_part_fn));
										
										ocrFieldMember.setAvarageWidth(avarageWidth);
										ocrFieldMember.setStartPoint(startPoint);
										ocrFieldMember.setImgPath(_inv_part_fn.replace(globalContextPath, "../../../DIM/"));
										ocrFieldMember.setCharsCount(charsCount);
										ocrFieldMember.setMainPath(mainPath);
										ocrFieldMember.setFMemberId(i);
										if (axet != null)
											ocrFieldMember.setValue(doOcrAxet(bi_2, axet));
										else
											ocrFieldMember.setValue("");
										ocrFieldMembers.add(ocrFieldMember);
										bi_2.flush();
									}
									//break; 
								}
								else if(pos.equals("right")){
									if(startPoint>0) 
										startPoint-=counter;
									else
										startPoint=fieldMembers.get(i-1).getStartPoint();
									
									BufferedImage bi_2 = bi.getSubimage(startPoint, bi.getMinY(), avarageWidth , bi.getHeight()); 
								    _inv_part_fn = mainPath.substring(0, mainPath.lastIndexOf(".")) + "_" + i + ".png";
								  
									ImageIO.write(bi_2, "png", new File(_inv_part_fn));
									
									ocrFieldMember.setAvarageWidth(avarageWidth);
									ocrFieldMember.setStartPoint(startPoint);
									ocrFieldMember.setImgPath(_inv_part_fn.replace(globalContextPath, "../../../DIM/"));
									ocrFieldMember.setCharsCount(charsCount);
									ocrFieldMember.setMainPath(mainPath);
									ocrFieldMember.setFMemberId(i);
									if (axet != null)
										ocrFieldMember.setValue(doOcrAxet(bi_2, axet));
									else
										ocrFieldMember.setValue("");
									ocrFieldMembers.add(ocrFieldMember);
									bi_2.flush();
									//break;
								}
								else if(pos.equals("width_plus")){  
									avarageWidth+=counter; 
									int sizeAbleWidth=startPoint+ avarageWidth;
									if(sizeAbleWidth>=width)
										avarageWidth-=counter;
									else
										avarageWidth+=counter;
									
									BufferedImage bi_2 = bi.getSubimage(startPoint, bi.getMinY(), avarageWidth, bi.getHeight()); 
									_inv_part_fn = mainPath.substring(0, mainPath.lastIndexOf(".")) + "_" + i + ".png";
									ImageIO.write(bi_2, "png", new File(_inv_part_fn));
									
									ocrFieldMember.setAvarageWidth(avarageWidth);
									ocrFieldMember.setStartPoint(startPoint);
									ocrFieldMember.setImgPath(_inv_part_fn.replace(globalContextPath, "../../../DIM/"));
									ocrFieldMember.setCharsCount(charsCount);
									ocrFieldMember.setMainPath(mainPath);
									ocrFieldMember.setFMemberId(i);
									if (axet != null)
										ocrFieldMember.setValue(doOcrAxet(bi_2, axet));
									else
										ocrFieldMember.setValue("");
									ocrFieldMembers.add(ocrFieldMember);
									bi_2.flush();
									//break;
								}
							else if(pos.equals("width_minus")){
									avarageWidth-=counter;
									
									BufferedImage bi_2 = bi.getSubimage(startPoint, bi.getMinY(), avarageWidth, bi.getHeight()); 
									_inv_part_fn = mainPath.substring(0, mainPath.lastIndexOf(".")) + "_" + i + ".png";
									ImageIO.write(bi_2, "png", new File(_inv_part_fn));
									
									ocrFieldMember.setAvarageWidth(avarageWidth);
									ocrFieldMember.setStartPoint(startPoint);
									ocrFieldMember.setImgPath(_inv_part_fn.replace(globalContextPath, "../../../DIM/"));
									ocrFieldMember.setCharsCount(charsCount);
									ocrFieldMember.setMainPath(mainPath);
									ocrFieldMember.setFMemberId(i);
									if (axet != null)
										ocrFieldMember.setValue(doOcrAxet(bi_2, axet));
									else
										ocrFieldMember.setValue("");
									ocrFieldMembers.add(ocrFieldMember);
									bi_2.flush();
								}
							}
							else {
								//BufferedImage bi_2 = bi.getSubimage(fieldMembers.get(i-1).getStartPoint(), bi.getMinY(), fieldMembers.get(i-1).getAvarageWidth(), bi.getHeight());
								//_inv_part_fn = mainPath.substring(0, mainPath.lastIndexOf(".")) + "_" + i + ".png";
								 
								//ImageIO.write(bi_2, "png", new File(_inv_part_fn));
				 
								ocrFieldMember.setAvarageWidth(fieldMembers.get(i-1).getAvarageWidth());
								ocrFieldMember.setStartPoint(fieldMembers.get(i-1).getStartPoint());
								ocrFieldMember.setImgPath(fieldMembers.get(i-1).getImgPath());
								ocrFieldMember.setCharsCount(charsCount);
								ocrFieldMember.setMainPath(mainPath);
								ocrFieldMember.setValue(fieldMembers.get(i-1).getValue());
								ocrFieldMember.setFMemberId(i);
								ocrFieldMembers.add(ocrFieldMember); 
								//if (axet != null)
								//ocrFieldMember.setValue(doOcrAxet(bi_2, axet));
								//else
								//ocrFieldMember.setValue("");
									//ocrFieldMembers.add(ocrFieldMember); 
								//bi_2.flush();
							}  
						}
					}  
					bi.flush();
					Thread.sleep(3000);
					 
				} catch (Exception e) {
					e.printStackTrace();  
				} finally { 
				    
				} 
				return ocrFieldMembers;
	 } 
	*/
	//preparedoRebuiltCropImage disabled end
	//doOcrAxet disabled start
	/*
	private String doOcrAxet(BufferedImage _inv_part_fn, Axet axet) {
		String str = "";
		str = axet.ConvertFiletoString(_inv_part_fn, "");
		return str;
	}
	*/
	//doOcrAxet disabled end
	//saveOcrTrainedImgs disabled start
	/*
	public String saveOcrTrainedImgs(String imgPath, String supplierTaxno, List<OCRFieldMember> fieldMembers) {
		String r = "0";
		try {
			String _psfn = imgPath.substring(imgPath.lastIndexOf("/") + 1);
			imgPath = globalContextPath + "V2/files/dimdata/archive/ocrtraining/" + _psfn;

			if (new java.io.File(imgPath).exists()) {
				String replacementListLenStr = OCRUtils.findInvoiceConfByTaxNo(supplierTaxno, "REPLACEMENTLISTLEN");
				int replacementListLen = (replacementListLenStr != null && replacementListLenStr.length() > 0) ? Integer.valueOf(replacementListLenStr).intValue() : 1;
				boolean addSupplierConfig = (replacementListLen == 1);

				String localPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();
				String parentPath = localPath.substring(0, localPath.lastIndexOf(File.separator));
				String fontsFolderPath = parentPath + "/axet/lookup/fonts/font_" + supplierTaxno;
				if (!(new File(fontsFolderPath).exists())) {
					new File(fontsFolderPath).mkdirs();
				}
				File imgFile = null;
				String newImgPath = "";

				for (OCRFieldMember fieldMember : fieldMembers) {
					if (fieldMember.getValue() != null && fieldMember.getValue().trim().length() > 0) {
						String _inv_part_fn = imgPath.substring(0, imgPath.lastIndexOf(".")) + "_" + fieldMember.getFMemberId() + ".png";
						imgFile = new File(_inv_part_fn);
						if (imgFile.exists()) {
							newImgPath = globalContextPath + "V2/files/dimdata/archive/ocrtraining/" + fieldMember.getValue().trim() + "-AA" + (replacementListLen++)
									+ "-.png";
							imgFile.renameTo(new File(newImgPath));
							imgFile = new File(newImgPath);

							try {
								FileUtils.moveFile(imgFile, new File(fontsFolderPath + "/" + imgFile.getName()));
							} catch (IOException e) {
								e.printStackTrace();
								r = "0";
							}
						}
					}
				}

				// add new line to the DimInvConfig file if new supplier
				OCRUtils.persistConfParameterValue("REPLACEMENTLISTLEN", replacementListLen + "", addSupplierConfig, supplierTaxno);
			}
			r = "1";
		} catch (Exception e) {
			e.printStackTrace();
			r = "0";
		}

		return r;
	}
	*/
	//saveOcrTrainedImgs disabled end
	public String recropFieldImg(String imgPath, int top, int left, int width, int height) {
		String r = "0";
		
		FileInputStream fis = null;
		try {
			String _psfn = imgPath.substring(imgPath.lastIndexOf("/") + 1);
			imgPath = globalContextPath + "V2/files/dimdata/archive/ocrtraining/" + _psfn;

			if (new java.io.File(imgPath).exists()) {
				fis = new FileInputStream(new java.io.File(imgPath));
				BufferedImage bi = ImageIO.read(fis);
				BufferedImage bi_2 = bi.getSubimage(left, top, width, height);
				ImageIO.write(bi_2, "png", new File(imgPath));
			}
			r = "1";
		} catch (Exception e) {
			e.printStackTrace();
			r = "0";
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return r;
	}
	
	//generateRecropFieldImg disabled start
	/*
	public  List<OCRTrainingLine> generateRecropFieldImg(int templateId, String invoiceFilePath, String mainField, String fieldName, String fieldId, int offsetTop, int offsetLeft, int offsetWidth, int offsetHeight) {
	 
		
		List<OCRTrainingLine> ocrTrainingLines = null;
		int xPos = 0, yPos = 0, w = 0, h = 0;
		ImageIO.scanForPlugins();

		DimService dimsrv = null;
		FileInputStream fis = null;

		try {
			dimsrv = new DimService(); 			

			if (!new java.io.File(invoiceFilePath.replace(".pdf", ".tiff")).exists()) {
				OCREngineUtils.convertPDFToTiff(invoiceFilePath, "tiff");
			}

			if (new java.io.File(invoiceFilePath.replace(".pdf", ".tiff")).exists()) {
				fis = new FileInputStream(new java.io.File(invoiceFilePath.replace(".pdf", ".tiff")));
				BufferedImage bi = ImageIO.read(fis);

				Tfdim003 _tmpl = null;
				if (templateId > 0) {
					_tmpl = dimsrv.getTemplateById(templateId);
				}
				if (_tmpl != null) {
					String _tesslang = findInvoiceConfByTaxNo(_tmpl.getSuppltaxn(), "TESSLANG");

					OCRTemplateRequest ocrt = null;
					Gson gson = new Gson();
					ocrt = (OCRTemplateRequest) gson.fromJson(_tmpl.getTemplmap(), OCRTemplateRequest.class);
					gson = null;

					if (ocrt != null) {
						DecimalFormat df = new DecimalFormat();
						DecimalFormatSymbols symbols = new DecimalFormatSymbols();
						try {
							symbols.setDecimalSeparator(ocrt.getSep_decimal().charAt(0));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						try {
							symbols.setGroupingSeparator(ocrt.getSep_1000().charAt(0));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						df.setDecimalFormatSymbols(symbols);

						List<OCRTemplateMapItem> tmis = ocrt.getMapping();

						OCRTemplateMapItem line_end_tmi = null;
						OCRTemplateMapItem inv_line_height = null;
						OCRTemplateMapItem inv_line_first_element = null;
						OCRTemplateMapItem inv_line_last_element = null;
						BufferedImage _inv_lines_area_bi = null;
						OCRTrainingField ocrTrainingField = null;
						int _line_cnt = 0;

						int default_offset = 10;

						String _inv_line_top = "0";
						String _inv_line_height = "0";  
							
						for (OCRTemplateMapItem tmi : tmis) {
							tmi.setTop("" + (Integer.parseInt(tmi.getTop()) - default_offset));
							tmi.setLeft("" + (Integer.parseInt(tmi.getLeft()) - default_offset));
							if (tmi.getFieldId().equals("inv_line_total")) {// inv_line_item_code
								_inv_line_top = tmi.getTop();
								_inv_line_height = tmi.getHeight();
							}
							if (tmi.getFieldId().equals(ocrt.getLine_first_col_tmi())) {// inv_line_item_code
								inv_line_first_element = tmi;
							}
							if (tmi.getFieldId().equals(ocrt.getLine_last_col_tmi())) {// inv_line_item_code
								inv_line_last_element = tmi;
							}
						}
						for (OCRTemplateMapItem tmi : tmis) {
							if (tmi.getFieldId().startsWith("inv_line_")) {
								tmi.setTop(_inv_line_top);
								tmi.setHeight(_inv_line_height);
							}
							if (tmi.getFieldId().equals(ocrt.getLine_end_tmi())) {// if (tmi.getFieldId().equals("inv_line_price")) {// if(tmi.getFieldId().equals("inv_line_total")){
								line_end_tmi = tmi;
							}
							if (tmi.getFieldId().equals("line_height")) {// if(tmi.getFieldId().equals("inv_line_total")){
								inv_line_height = tmi;
							}
						}

						if (inv_line_height == null) {
							inv_line_height = new OCRTemplateMapItem();
							inv_line_height.setHeight("0");
						}

						if (line_end_tmi != null) {

							String _line_txt = "";
							do {
								_line_txt = "";

								BufferedImage bi_2 = bi.getSubimage(Integer.parseInt(line_end_tmi.getLeft()),
										(Integer.parseInt(line_end_tmi.getTop()) + (_line_cnt * (Integer.parseInt(line_end_tmi.getHeight()) + Integer
												.parseInt(inv_line_height.getHeight())))), Integer.parseInt(line_end_tmi.getWidth()), Integer.parseInt(line_end_tmi
												.getHeight()));
								String _inv_part_fn = invoiceFilePath.substring(0, invoiceFilePath.lastIndexOf(".")) + "_lf_" + _line_cnt + "_" + line_end_tmi.getFieldId() + ".tiff";
								ImageIO.write(bi_2, "tiff", new File(_inv_part_fn));

								_line_txt = doOcr2(_inv_part_fn, ocrt.getIsdotmatrix(), _tesslang, false);

								if (!(_line_txt != null && _line_txt.trim().length() > 0)) {
									_line_txt = doOcr2(_inv_part_fn, ocrt.getIsdotmatrix(), _tesslang, true);
								}

								if (_line_txt != null && _line_txt.trim().length() > 0) {
									_line_cnt++;
								}
							} while (_line_txt != null && _line_txt.trim().length() > 0);
						}
						if (_line_cnt == 0) {
							_line_cnt = 1;
						}
						if (_line_cnt > 0) {

							ocrTrainingLines = new ArrayList<OCRTrainingLine>();
							for (int i = 0; i < _line_cnt; i++) {
								ocrTrainingLines.add(new OCRTrainingLine());
							}

							// satirlar bolgesini kes hocr ile tara
							_inv_lines_area_bi = bi.getSubimage(Integer.parseInt(inv_line_first_element.getLeft()), Integer.parseInt(inv_line_first_element.getTop()) - 10,
									((Integer.parseInt(inv_line_last_element.getLeft()) + Integer.parseInt(inv_line_last_element.getWidth())) - Integer
											.parseInt(inv_line_first_element.getLeft())) + 10, (_line_cnt * (Integer.parseInt(inv_line_first_element.getHeight()))) + 10);

							String _inv_part_fn = invoiceFilePath.substring(0, invoiceFilePath.lastIndexOf(".")) + "_" + 0 + "_" + "inv_line_area" + ".tiff";
							ImageIO.write(_inv_lines_area_bi, "tiff", new File(_inv_part_fn));

							if (ocrt.getIsdotmatrix().equals("1"))
								OCRUtils.runIMScripts(_inv_part_fn, ocrt.getIsdotmatrix(), false);

							String _rt = OCREngineUtils.runTesseract(_inv_part_fn, "-l tur " + ((_tesslang != null) ? "-l " + _tesslang : "") + " -psm 6 hocr");
							FileUtils.writeStringToFile(new File(_inv_part_fn.replace(".tiff", ".out.html")), _rt);
							LineIterator it = FileUtils.lineIterator(new File(_inv_part_fn.replace(".tiff", ".out.html")), "UTF-8");
							int _lastii = 1;
							while (it.hasNext()) {
								String _line_txt = it.nextLine().trim();
								for (int _ii = 1; _ii <= _line_cnt; _ii++) {
									String _st_word = "span class='ocr_line' id='line_" + _ii + "'";
									if (_line_txt != null && _line_txt.indexOf(_st_word) > -1) {
										int _st1 = _line_txt.indexOf(_st_word) + _st_word.length();
										_st_word = _line_txt.substring(_st1 + 1, _line_txt.indexOf(">"));
										_st_word = _st_word.replace("\"", "");
										_st_word = _st_word.replace("'", "");
										_st_word = _st_word.replace("title=bbox ", "");
										String _tmp_arr[] = _st_word.split(Pattern.quote(" "));
										if (_tmp_arr != null && _tmp_arr.length == 4) {
											ocrTrainingLines.get(_ii - 1).setTop((Integer.parseInt(inv_line_first_element.getTop()) + Integer.parseInt(_tmp_arr[1])) - 10);
											ocrTrainingLines.get(_ii - 1).setHeight((Integer.parseInt(_tmp_arr[3]) - Integer.parseInt(_tmp_arr[1])) + 10);
											_lastii = _ii;
										}
									}
								}
							}
							LineIterator.closeQuietly(it);
							if (ocrTrainingLines != null && _lastii != ocrTrainingLines.size()) {
								ArrayList<OCRTrainingLine> _tmp_list = new ArrayList<OCRTrainingLine>();
								for (int _ii = 0; _ii < ocrTrainingLines.size(); _ii++) {
									if (_ii < _lastii) {
										_tmp_list.add(ocrTrainingLines.get(_ii));
									}
								}
								ocrTrainingLines = _tmp_list;
								_line_cnt = ocrTrainingLines.size();
							}
						} 
						 
						int lineNumber=0;
						int coloumnNumber=0;
						String[] fields=fieldId.split("_");
						if(fields!=null && fields.length>0){
							lineNumber = Integer.parseInt(fields[0].trim());
							coloumnNumber = Integer.parseInt(fields[1].trim());
						}
						for (OCRTemplateMapItem tmi : tmis) { 
							if (tmi.getFieldId() != null) {
							//	if (tmi.getFieldId().equals(mainField))  { 

									for (int ii = 0; ii < _line_cnt; ii++) {
										ocrTrainingField = new OCRTrainingField();
										ocrTrainingLines.get(ii).setLineNo(ii + 1);
										if (ocrTrainingLines.get(ii).getOcrTrainingFields() == null) {
											ocrTrainingLines.get(ii).setOcrTrainingFields(new ArrayList<OCRTrainingField>());
										} 
										
										BufferedImage bi_2 = null; 
										boolean detected=false;
										if(lineNumber==ocrTrainingLines.get(ii).getLineNo() && tmi.getFieldId().equals(fieldName)){
											System.out.println("Tespit Edildi");
											bi_2 = bi.getSubimage(offsetLeft, ( ocrTrainingLines.get(ii).getTop() + (offsetTop - ocrTrainingLines.get(ii).getTop())) ,
													offsetWidth, (offsetHeight+10));
											detected=true;
										}
										else{
										bi_2 = bi.getSubimage(Integer.parseInt(tmi.getLeft()), ocrTrainingLines.get(ii).getTop(),
												Integer.parseInt(tmi.getWidth()), ocrTrainingLines.get(ii).getHeight());
										}

										String _inv_part_fn = invoiceFilePath.substring(0, invoiceFilePath.lastIndexOf(".")) + "_" + ii + "_" + tmi.getFieldId() + ".tiff";
										ImageIO.write(bi_2, "tiff", new File(_inv_part_fn));

										String _rt = OCREngineUtils.runTesseract(_inv_part_fn, "-l tur " + ((_tesslang != null) ? "-l " + _tesslang : "") + " -psm 6 hocr");
										FileUtils.writeStringToFile(new File(_inv_part_fn.replace(".tiff", ".out.html")), _rt);
										LineIterator it = FileUtils.lineIterator(new File(_inv_part_fn.replace(".tiff", ".out.html")), "UTF-8");
										boolean coordinationFounded = false;
										while (it.hasNext()) {
											String _line_txt = it.nextLine().trim();
											String _st_word = "span class='ocr_line' id='line_1'";
											if (_line_txt != null && _line_txt.indexOf(_st_word) > -1) {
												int _st1 = _line_txt.indexOf(_st_word) + _st_word.length();
												_st_word = _line_txt.substring(_st1 + 1, _line_txt.indexOf(">"));
												_st_word = _st_word.replace("\"", "");
												_st_word = _st_word.replace("'", "");
												_st_word = _st_word.replace("title=bbox ", "");
												String _tmp_arr[] = _st_word.split(Pattern.quote(" "));
												if (_tmp_arr != null && _tmp_arr.length == 4) {
													xPos = Integer.parseInt(_tmp_arr[0]);
													yPos = Integer.parseInt(_tmp_arr[1]);
													w = Integer.parseInt(_tmp_arr[2]);
													h = Integer.parseInt(_tmp_arr[3]);
													coordinationFounded = true;
													break;
												}
											}
										}
										LineIterator.closeQuietly(it);
										if (!coordinationFounded) {
											xPos = 0;
											yPos = 0;
											w = bi_2.getWidth();
											h = bi_2.getHeight();
										} 
										
										BufferedImage newbi_2 =null;
										// Teseract tarafndan tespit edilen yukseklik eger bizim cizdigimiz yuksekligin 2 kat�ndan buyukse bizim cizdigimiz yuksekligi referans al
										int checkHeight=offsetHeight-h;
										if(checkHeight>10 && detected){ // offsetHeight
											newbi_2 = bi_2.getSubimage(xPos, yPos, w - xPos, offsetHeight - yPos); 
										}
										else{
											newbi_2 = bi_2.getSubimage(xPos, yPos, w - xPos, h - yPos);
										}
											

										String new_inv_part_fn = invoiceFilePath.substring(0, invoiceFilePath.lastIndexOf(".")) + "_" + ii + "_" + tmi.getFieldId() + "_new.tiff";
										ImageIO.write(newbi_2, "tiff", new File(new_inv_part_fn));

										// to scale the image 50% of the original size
										Image tmp = newbi_2.getScaledInstance(newbi_2.getWidth() / 2, newbi_2.getHeight() / 2, Image.SCALE_SMOOTH);
										newbi_2 = new BufferedImage(newbi_2.getWidth() / 2, newbi_2.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
										Graphics2D g2d = newbi_2.createGraphics();
										g2d.drawImage(tmp, 0, 0, null);
										g2d.dispose();
										ImageIO.write(newbi_2, "png", new File(new_inv_part_fn.replace(".tiff", ".png"))); 
										
										ocrTrainingField.setFieldImgPath(new_inv_part_fn.replace(globalContextPath, "../../../DIM/").replace(".tiff", ".png"));

										 
										if (tmi.getFieldId().equals("inv_line_item_code")) {
											ocrTrainingField.setFieldName("Item Code");
										}
										if (tmi.getFieldId().equals("inv_line_item")) {
											ocrTrainingField.setFieldName("Item Desc");
										}
										if (tmi.getFieldId().equals("inv_line_unit")) {
											ocrTrainingField.setFieldName("Unit Code");
										}
										if (tmi.getFieldId().equals("inv_line_qty")) {
											ocrTrainingField.setFieldName("Qty");
										}
										if (tmi.getFieldId().equals("inv_line_price")) {
											ocrTrainingField.setFieldName("Unit Price");
										}
										if (tmi.getFieldId().equals("inv_line_total")) {
											ocrTrainingField.setFieldName("Total");
										}

										if (ocrTrainingField.getFieldName() != null && ocrTrainingField.getFieldName().length() > 0)
											ocrTrainingLines.get(ii).getOcrTrainingFields().add(ocrTrainingField);
										
										
									}
							//	} if end  
							}
						}

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			ocrTrainingLines=null;
		} finally {
			dimsrv.close();
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return ocrTrainingLines;
	}
	*/
	//saveOcrTrainedImgs disabled end
	public String[] readVatNoTypingTypes()  
    {
        List<String> lines;
        try {
			FileReader fileReader = new FileReader(
					DimConfig.getInstance().getPropertyValue("VAT_NO_TYPING_TYPES_PATH"));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			lines = new ArrayList<String>();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
		} catch (Exception e) {
			return null;
		}
		return lines.toArray(new String[lines.size()]);
    }

	
	public GenericReturnTO parseSelectedArea(String fid, String templateId, String servlet_context_path,
			int top, int left, int width, int height) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
