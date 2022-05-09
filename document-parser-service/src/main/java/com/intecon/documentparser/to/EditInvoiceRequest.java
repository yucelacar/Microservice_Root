package com.intecon.documentparser.to;

import java.util.List;


public class EditInvoiceRequest {
	private String invoice_id;
	private String erp_id;
	private String customer_taxn;
	private String customer_name;
	private String file_name;
	private String profile_id;
	private String invoice_type;
	private List<GenericDataItem> despatch_list;
	private GenericDataItem order_reference;
	private List<GenericDataItem> line_list;
	private List<GenericDataItem> note_list;
	//private List<Tfdim010> tax_list;
	private String issue_date;
	private String acp_contact_email;
	private String cust_street;
	private String cust_bno;
	private String cust_district;
	private String cust_city;
	private String cust_country;
	private String cust_zipc;
	private String cust_taxo;
	private String cust_phone;
	private String cust_fax;
	private String cust_web;
	private String despno;
	private String desp_date;
	
	private String currcode;
	private String sapcurrcode;
	private double currrate;
	
	private double total_amnt;
	private double total_disc;
	private double total_kdv;
	private double total_tevbamnt;
	private double total_tevkdv;
	private double total_tev;
	private double total_taxincl;
	private double total_final;
	private double kdvrate;
	
	private String delivery_type;
	private String is_online_sale;
	private String web_address;
	private String payment_type;
	private String payment_comp;
	private String payment_date;
	private String delivery_date;
	private String delivery_name;
	private String delivery_id;
	private String is_despatch;
	private int report_id;
	private String earchive_email_address;
	private String tevk_code;
	private String kdv0_code;
	
	private String e_type;
	
	private String ret_msg;
	
	private String pp_npp; // Husam Added
	private String compERP;
	private String taxCountry;
	private String postingDate;
	private String fncYear;
	private String fncPeriod;
	private String taxYear;
	private String taxPeriod;
	private int poType;
	private String erpSupplierId;
	private String isservicerequest;
	
	public EditInvoiceRequest(){
		setCust_city("");
		setCust_street("");
		setInvoice_id("");
	}
	
	public String getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}
	public String getErp_id() {
		return erp_id;
	}
	public void setErp_id(String erp_id) {
		this.erp_id = erp_id;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getProfile_id() {
		return profile_id;
	}
	public void setProfile_id(String profile_id) {
		this.profile_id = profile_id;
	}
	public String getInvoice_type() {
		return invoice_type;
	}
	public void setInvoice_type(String invoice_type) {
		this.invoice_type = invoice_type;
	}
	public List<GenericDataItem> getDespatch_list() {
		return despatch_list;
	}
	public void setDespatch_list(List<GenericDataItem> despatch_list) {
		this.despatch_list = despatch_list;
	}
	public GenericDataItem getOrder_reference() {
		return order_reference;
	}
	public void setOrder_reference(GenericDataItem order_reference) {
		this.order_reference = order_reference;
	}
	public List<GenericDataItem> getLine_list() {
		return line_list;
	}
	public void setLine_list(List<GenericDataItem> line_list) {
		this.line_list = line_list;
	}
	public List<GenericDataItem> getNote_list() {
		return note_list;
	}
	public void setNote_list(List<GenericDataItem> note_list) {
		this.note_list = note_list;
	}
	public String getCustomer_taxn() {
		return customer_taxn;
	}
	public void setCustomer_taxn(String customer_taxn) {
		this.customer_taxn = customer_taxn;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getIssue_date() {
		return issue_date;
	}
	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}
	public String getAcp_contact_email() {
		return acp_contact_email;
	}
	public void setAcp_contact_email(String acp_contact_email) {
		this.acp_contact_email = acp_contact_email;
	}
	public String getCurrcode() {
		return currcode;
	}
	public void setCurrcode(String currcode) {
		this.currcode = currcode;
	}
	public double getCurrrate() {
		return currrate;
	}
	public void setCurrrate(double currrate) {
		this.currrate = currrate;
	}
	public String getCust_street() {
		return cust_street;
	}
	public void setCust_street(String cust_street) {
		this.cust_street = cust_street;
	}
	public String getCust_bno() {
		return cust_bno;
	}
	public void setCust_bno(String cust_bno) {
		this.cust_bno = cust_bno;
	}
	public String getCust_district() {
		return cust_district;
	}
	public void setCust_district(String cust_district) {
		this.cust_district = cust_district;
	}
	public String getCust_city() {
		return cust_city;
	}
	public void setCust_city(String cust_city) {
		this.cust_city = cust_city;
	}
	public String getCust_zipc() {
		return cust_zipc;
	}
	public void setCust_zipc(String cust_zipc) {
		this.cust_zipc = cust_zipc;
	}
	public String getCust_taxo() {
		return cust_taxo;
	}
	public void setCust_taxo(String cust_taxo) {
		this.cust_taxo = cust_taxo;
	}
	public String getCust_phone() {
		return cust_phone;
	}
	public void setCust_phone(String cust_phone) {
		this.cust_phone = cust_phone;
	}
	public String getCust_fax() {
		return cust_fax;
	}
	public void setCust_fax(String cust_fax) {
		this.cust_fax = cust_fax;
	}
	public String getCust_web() {
		return cust_web;
	}
	public void setCust_web(String cust_web) {
		this.cust_web = cust_web;
	}
	public double getTotal_amnt() {
		return total_amnt;
	}
	public void setTotal_amnt(double total_amnt) {
		this.total_amnt = total_amnt;
	}
	public double getTotal_disc() {
		return total_disc;
	}
	public void setTotal_disc(double total_disc) {
		this.total_disc = total_disc;
	}
	public double getTotal_kdv() {
		return total_kdv;
	}
	public void setTotal_kdv(double total_kdv) {
		this.total_kdv = total_kdv;
	}
	public double getTotal_tevbamnt() {
		return total_tevbamnt;
	}
	public void setTotal_tevbamnt(double total_tevbamnt) {
		this.total_tevbamnt = total_tevbamnt;
	}
	public double getTotal_tevkdv() {
		return total_tevkdv;
	}
	public void setTotal_tevkdv(double total_tevkdv) {
		this.total_tevkdv = total_tevkdv;
	}
	public double getTotal_tev() {
		return total_tev;
	}
	public void setTotal_tev(double total_tev) {
		this.total_tev = total_tev;
	}
	public double getTotal_taxincl() {
		return total_taxincl;
	}
	public void setTotal_taxincl(double total_taxincl) {
		this.total_taxincl = total_taxincl;
	}
	public double getTotal_final() {
		return total_final;
	}
	public void setTotal_final(double total_final) {
		this.total_final = total_final;
	}
	public String getDelivery_type() {
		return delivery_type;
	}
	public void setDelivery_type(String delivery_type) {
		this.delivery_type = delivery_type;
	}
	public String getIs_online_sale() {
		return is_online_sale;
	}
	public void setIs_online_sale(String is_online_sale) {
		this.is_online_sale = is_online_sale;
	}
	public String getWeb_address() {
		return web_address;
	}
	public void setWeb_address(String web_address) {
		this.web_address = web_address;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getPayment_comp() {
		return payment_comp;
	}
	public void setPayment_comp(String payment_comp) {
		this.payment_comp = payment_comp;
	}
	public String getPayment_date() {
		return payment_date;
	}
	public void setPayment_date(String payment_date) {
		this.payment_date = payment_date;
	}
	public String getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(String delivery_date) {
		this.delivery_date = delivery_date;
	}
	public String getDelivery_name() {
		return delivery_name;
	}
	public void setDelivery_name(String delivery_name) {
		this.delivery_name = delivery_name;
	}
	public String getDelivery_id() {
		return delivery_id;
	}
	public void setDelivery_id(String delivery_id) {
		this.delivery_id = delivery_id;
	}
	public String getIs_despatch() {
		return is_despatch;
	}
	public void setIs_despatch(String is_despatch) {
		this.is_despatch = is_despatch;
	}
	public int getReport_id() {
		return report_id;
	}
	public void setReport_id(int report_id) {
		this.report_id = report_id;
	}
	public String getEarchive_email_address() {
		return earchive_email_address;
	}
	public void setEarchive_email_address(String earchive_email_address) {
		this.earchive_email_address = earchive_email_address;
	}
	public String getCust_country() {
		return cust_country;
	}
	public void setCust_country(String cust_country) {
		this.cust_country = cust_country;
	}
	public String getTevk_code() {
		return tevk_code;
	}
	public void setTevk_code(String tevk_code) {
		this.tevk_code = tevk_code;
	}
	public String getKdv0_code() {
		return kdv0_code;
	}
	public void setKdv0_code(String kdv0_code) {
		this.kdv0_code = kdv0_code;
	}
	public String getCompERP() {
		return compERP;
	}

	public void setCompERP(String compERP) {
		this.compERP = compERP;
	}

	/**
	 * @return EINVOICE,EARCHIVE
	 */
	public String getE_type() {
		return e_type;
	}
	/**
	 * @param e_type EINVOICE,EARCHIVE
	 */
	public void setE_type(String e_type) {
		this.e_type = e_type;
	}
	/**
	 * @return response to request
	 */
	public String getRet_msg() {
		return ret_msg;
	}
	/**
	 * @param ret_msg the ret_msg to set
	 */
	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}
	public String getDespno() {
		return despno;
	}
	public void setDespno(String despno) {
		this.despno = despno;
	}
	public String getDesp_date() {
		return desp_date;
	}
	public void setDesp_date(String desp_date) {
		this.desp_date = desp_date;
	}
	public double getKdvrate() {
		return kdvrate;
	}
	public void setKdvrate(double kdvrate) {
		this.kdvrate = kdvrate;
	}
	// Husam Start
	public String getPp_npp() {
		return pp_npp;
	}
	public void setPp_npp(String pp_npp) {
		this.pp_npp = pp_npp;
	}
	// Husam End 

	public String getSapcurrcode() {
		return sapcurrcode;
	}

	public void setSapcurrcode(String sapcurrcode) {
		this.sapcurrcode = sapcurrcode;
	}

//	public List<Tfdim010> getTax_list() {
//		return tax_list;
//	}
//
//	public void setTax_list(List<Tfdim010> tax_list) {
//		this.tax_list = tax_list;
//	}

	public String getTaxCountry() {
		return taxCountry;
	}

	public void setTaxCountry(String taxCountry) {
		this.taxCountry = taxCountry;
	}

	public String getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}

	public String getFncYear() {
		return fncYear;
	}

	public void setFncYear(String fncYear) {
		this.fncYear = fncYear;
	}

	public String getFncPeriod() {
		return fncPeriod;
	}

	public void setFncPeriod(String fncPeriod) {
		this.fncPeriod = fncPeriod;
	}

	public String getTaxYear() {
		return taxYear;
	}

	public void setTaxYear(String taxYear) {
		this.taxYear = taxYear;
	}

	public String getTaxPeriod() {
		return taxPeriod;
	}

	public void setTaxPeriod(String taxPeriod) {
		this.taxPeriod = taxPeriod;
	}

	public int getPoType() {
		return poType;
	}

	public void setPoType(int poType) {
		this.poType = poType;
	}

	public String getErpSupplierId() {
		return erpSupplierId;
	}

	public void setErpSupplierId(String erpSupplierId) {
		this.erpSupplierId = erpSupplierId;
	}

	public String getIsservicerequest() {
		return isservicerequest;
	}

	public void setIsservicerequest(String isservicerequest) {
		this.isservicerequest = isservicerequest;
	}
}
