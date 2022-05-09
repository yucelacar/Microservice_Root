package com.intecon.documentparser.to;

public class GenericReturnTO {
	private String retval;
	private String errorCode;
	private String errorDesc;
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public String getRetval() {
		return retval;
	}
	public void setRetval(String retval) {
		this.retval = retval;
	}
}
