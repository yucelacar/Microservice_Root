package com.intecon.outputmap.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection = "OutputMapTemplate")
public class OutputMapTemplate {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long mapId,compId;
	public String   sourceClass, targerClass, parentClass, 	isMultiple, isMandatory, docType, crt_tst, isActive;
	
	public OutputMapTemplate() {
		
	}

	public OutputMapTemplate(long mapId, long compId, String sourceClass, String targerClass, String parentClass,
			String isMultiple, String isMandatory, String docType, String crt_tst, String isActive) {
		super();
		this.mapId = mapId;
		this.compId = compId;
		this.sourceClass = sourceClass;
		this.targerClass = targerClass;
		this.parentClass = parentClass;
		this.isMultiple = isMultiple;
		this.isMandatory = isMandatory;
		this.docType = docType;
		this.crt_tst = crt_tst;
		this.isActive = isActive;
	}

	public long getMapId() {
		return mapId;
	}

	public void setMapId(long mapId) {
		this.mapId = mapId;
	}

	public long getCompId() {
		return compId;
	}

	public void setCompId(long compId) {
		this.compId = compId;
	}

	public String getSourceClass() {
		return sourceClass;
	}

	public void setSourceClass(String sourceClass) {
		this.sourceClass = sourceClass;
	}

	public String getTargerClass() {
		return targerClass;
	}

	public void setTargerClass(String targerClass) {
		this.targerClass = targerClass;
	}

	public String getParentClass() {
		return parentClass;
	}

	public void setParentClass(String parentClass) {
		this.parentClass = parentClass;
	}

	public String getIsMultiple() {
		return isMultiple;
	}

	public void setIsMultiple(String isMultiple) {
		this.isMultiple = isMultiple;
	}

	public String getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getCrt_tst() {
		return crt_tst;
	}

	public void setCrt_tst(String crt_tst) {
		this.crt_tst = crt_tst;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
}
