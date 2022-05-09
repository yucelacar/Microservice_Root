package com.intecon.documentreading.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection = "DocumentDetail")
public class DocumentDetail {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long DOCDETAILID;
	public String  DOCID, CLASSNAME, CLASSVALUE, CLASSSEQ, COORDX, COORDY, COORDW, COORDH, ISCORRECT;
	
	public DocumentDetail() {
		
	}

	public DocumentDetail(long dOCDETAILID, String dOCID, String cLASSNAME, String cLASSVALUE, String cLASSSEQ,
			String cOORDX, String cOORDY, String cOORDW, String cOORDH, String iSCORRECT) {
		super();
		DOCDETAILID = dOCDETAILID;
		DOCID = dOCID;
		CLASSNAME = cLASSNAME;
		CLASSVALUE = cLASSVALUE;
		CLASSSEQ = cLASSSEQ;
		COORDX = cOORDX;
		COORDY = cOORDY;
		COORDW = cOORDW;
		COORDH = cOORDH;
		ISCORRECT = iSCORRECT;
	}

	public long getDOCDETAILID() {
		return DOCDETAILID;
	}

	public void setDOCDETAILID(long dOCDETAILID) {
		DOCDETAILID = dOCDETAILID;
	}

	public String getDOCID() {
		return DOCID;
	}

	public void setDOCID(String dOCID) {
		DOCID = dOCID;
	}

	public String getCLASSNAME() {
		return CLASSNAME;
	}

	public void setCLASSNAME(String cLASSNAME) {
		CLASSNAME = cLASSNAME;
	}

	public String getCLASSVALUE() {
		return CLASSVALUE;
	}

	public void setCLASSVALUE(String cLASSVALUE) {
		CLASSVALUE = cLASSVALUE;
	}

	public String getCLASSSEQ() {
		return CLASSSEQ;
	}

	public void setCLASSSEQ(String cLASSSEQ) {
		CLASSSEQ = cLASSSEQ;
	}

	public String getCOORDX() {
		return COORDX;
	}

	public void setCOORDX(String cOORDX) {
		COORDX = cOORDX;
	}

	public String getCOORDY() {
		return COORDY;
	}

	public void setCOORDY(String cOORDY) {
		COORDY = cOORDY;
	}

	public String getCOORDW() {
		return COORDW;
	}

	public void setCOORDW(String cOORDW) {
		COORDW = cOORDW;
	}

	public String getCOORDH() {
		return COORDH;
	}

	public void setCOORDH(String cOORDH) {
		COORDH = cOORDH;
	}

	public String getISCORRECT() {
		return ISCORRECT;
	}

	public void setISCORRECT(String iSCORRECT) {
		ISCORRECT = iSCORRECT;
	}

}
