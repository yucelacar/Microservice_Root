package com.intecon.documentreading.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection = "DocumentHeader")
public class DocumentHeader {

	@Id
	String _id;
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long DOCID;
	
	private String COMPID, ID, ISSUEDATE, SENDERID, SENDERNAME, RECVID, RECVNAME, DOCTYPE, DOCSTATUS, DOCPATH, CRTTST, ISACTIVE;
	
	public DocumentHeader() {
		
	}

	public DocumentHeader(String _id, long dOCID, String cOMPID, String iD, String iSSUEDATE, String sENDERID, String sENDERNAME,
			String rECVID, String rECVNAME, String dOCTYPE, String dOCSTATUS, String dOCPATH, String cRTTST,
			String iSACTIVE) {
		this._id = _id;
		COMPID = cOMPID;
		ID = iD;
		ISSUEDATE = iSSUEDATE;
		SENDERID = sENDERID;
		SENDERNAME = sENDERNAME;
		RECVID = rECVID;
		RECVNAME = rECVNAME;
		DOCTYPE = dOCTYPE;
		DOCSTATUS = dOCSTATUS;
		DOCPATH = dOCPATH;
		CRTTST = cRTTST;
		ISACTIVE = iSACTIVE;
	}
	

	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}

	public long getDOCID() {
		return DOCID;
	}

	public void setDOCID(long dOCID) {
		DOCID = dOCID;
	}

	public String getCOMPID() {
		return COMPID;
	}

	public void setCOMPID(String cOMPID) {
		COMPID = cOMPID;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getISSUEDATE() {
		return ISSUEDATE;
	}

	public void setISSUEDATE(String iSSUEDATE) {
		ISSUEDATE = iSSUEDATE;
	}

	public String getSENDERID() {
		return SENDERID;
	}

	public void setSENDERID(String sENDERID) {
		SENDERID = sENDERID;
	}

	public String getSENDERNAME() {
		return SENDERNAME;
	}

	public void setSENDERNAME(String sENDERNAME) {
		SENDERNAME = sENDERNAME;
	}

	public String getRECVID() {
		return RECVID;
	}

	public void setRECVID(String rECVID) {
		RECVID = rECVID;
	}

	public String getRECVNAME() {
		return RECVNAME;
	}

	public void setRECVNAME(String rECVNAME) {
		RECVNAME = rECVNAME;
	}

	public String getDOCTYPE() {
		return DOCTYPE;
	}

	public void setDOCTYPE(String dOCTYPE) {
		DOCTYPE = dOCTYPE;
	}

	public String getDOCSTATUS() {
		return DOCSTATUS;
	}

	public void setDOCSTATUS(String dOCSTATUS) {
		DOCSTATUS = dOCSTATUS;
	}

	public String getDOCPATH() {
		return DOCPATH;
	}

	public void setDOCPATH(String dOCPATH) {
		DOCPATH = dOCPATH;
	}

	public String getCRTTST() {
		return CRTTST;
	}

	public void setCRTTST(String cRTTST) {
		CRTTST = cRTTST;
	}

	public String getISACTIVE() {
		return ISACTIVE;
	}

	public void setISACTIVE(String iSACTIVE) {
		ISACTIVE = iSACTIVE;
	}
	
	
	
}
