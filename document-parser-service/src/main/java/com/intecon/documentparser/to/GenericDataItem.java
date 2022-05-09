package com.intecon.documentparser.to;

public class GenericDataItem {
	private int id;
	private String value;
	private String label;
	private String item;
	private String mancode;
	private String desc;
	private int ind;
	private String unit;
	private double qty;
	private double price;
	private double discp;
	private double disc;
	private double amnt;
	private double taxp;
	private double tax;
	private double taxdiscp;
	private double taxdisc;
	private double total;
	private String taxexempt;
	private String taxCode;
	private int top;
	private int height;
    private String priceCurrencyCode; // Husam added
    private String erpitemcode;
    
    public GenericDataItem(){
    	setItem("");
    	setMancode("");
    	setErpitemcode("");
    	setDesc("");
    	setUnit("");
    }
	
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getMancode() {
		return mancode;
	}
	public void setMancode(String mancode) {
		this.mancode = mancode;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getInd() {
		return ind;
	}
	public void setInd(int ind) {
		this.ind = ind;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getDiscp() {
		return discp;
	}
	public void setDiscp(double discp) {
		this.discp = discp;
	}
	public double getDisc() {
		return disc;
	}
	public void setDisc(double disc) {
		this.disc = disc;
	}
	public double getAmnt() {
		return amnt;
	}
	public void setAmnt(double amnt) {
		this.amnt = amnt;
	}
	public double getTaxp() {
		return taxp;
	}
	public void setTaxp(double taxp) {
		this.taxp = taxp;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public double getTaxdiscp() {
		return taxdiscp;
	}
	public void setTaxdiscp(double taxdiscp) {
		this.taxdiscp = taxdiscp;
	}
	public double getTaxdisc() {
		return taxdisc;
	}
	public void setTaxdisc(double taxdisc) {
		this.taxdisc = taxdisc;
	}
	public String getTaxexempt() {
		return taxexempt;
	}
	public void setTaxexempt(String taxexempt) {
		this.taxexempt = taxexempt;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getPriceCurrencyCode() {
		return priceCurrencyCode;
	}
	public void setPriceCurrencyCode(String priceCurrencyCode) {
		this.priceCurrencyCode = priceCurrencyCode;
	}

	public String getErpitemcode() {
		return erpitemcode;
	}

	public void setErpitemcode(String erpitemcode) {
		this.erpitemcode = erpitemcode;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
}
