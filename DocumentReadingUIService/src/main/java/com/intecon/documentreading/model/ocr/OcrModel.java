
package com.intecon.documentreading.model.ocr;

import java.util.List;

public class OcrModel {

    private Integer id;
    private String title;
    private String istotalfixed;
    private String sep_1000;
    private String sep_decimal;
    private String date_format;
    private List<Mapping> mapping = null;
    private String taxno;
    private String sfilepath;
    private String isdotmatrix;
    private String line_end_tmi;
    private String line_first_col_tmi;
    private String line_last_col_tmi;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIstotalfixed() {
        return istotalfixed;
    }

    public void setIstotalfixed(String istotalfixed) {
        this.istotalfixed = istotalfixed;
    }

    public String getSep_1000() {
		return sep_1000;
	}

	public void setSep_1000(String sep_1000) {
		this.sep_1000 = sep_1000;
	}

	public String getSep_decimal() {
		return sep_decimal;
	}

	public void setSep_decimal(String sep_decimal) {
		this.sep_decimal = sep_decimal;
	}

	public String getDate_format() {
		return date_format;
	}

	public void setDate_format(String date_format) {
		this.date_format = date_format;
	}

	public List<Mapping> getMapping() {
        return mapping;
    }

    public void setMapping(List<Mapping> mapping) {
        this.mapping = mapping;
    }

    public String getTaxno() {
        return taxno;
    }

    public void setTaxno(String taxno) {
        this.taxno = taxno;
    }

    public String getSfilepath() {
        return sfilepath;
    }

    public void setSfilepath(String sfilepath) {
        this.sfilepath = sfilepath;
    }

    public String getIsdotmatrix() {
        return isdotmatrix;
    }

    public void setIsdotmatrix(String isdotmatrix) {
        this.isdotmatrix = isdotmatrix;
    }


    public String getLine_end_tmi() {
		return line_end_tmi;
	}

	public void setLine_end_tmi(String line_end_tmi) {
		this.line_end_tmi = line_end_tmi;
	}

	public String getLine_first_col_tmi() {
		return line_first_col_tmi;
	}

	public void setLine_first_col_tmi(String line_first_col_tmi) {
		this.line_first_col_tmi = line_first_col_tmi;
	}

	public String getLine_last_col_tmi() {
		return line_last_col_tmi;
	}

	public void setLine_last_col_tmi(String line_last_col_tmi) {
		this.line_last_col_tmi = line_last_col_tmi;
	}

	
}
