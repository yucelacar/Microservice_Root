package com.intecon.documentreading.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection = "OutputMapTemplate")
public class OutputMapTemplate {


	@Id
	String _id;
	
	public String   sourceClass, targetClass;
	
	public OutputMapTemplate() {
		
	}

	public OutputMapTemplate(String _id, String sourceClass, String targetClass) {
		this._id = _id;
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getSourceClass() {
		return sourceClass;
	}

	public void setSourceClass(String sourceClass) {
		this.sourceClass = sourceClass;
	}

	public String getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	
}
