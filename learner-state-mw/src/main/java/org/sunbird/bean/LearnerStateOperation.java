package org.sunbird.bean;
/**
 * This enum will contains different state
 *  for a learner {addCourse, getCourse, update}
 * @author Manzarul
 *
 */
public enum LearnerStateOperation {
	ADD_COURSE("addCourse"),GET_COURSE("getCourse"),UPDATE_TOC("updateToc");

	private String value;

	LearnerStateOperation(String value){
		this.value=value;
	}

	public String getValue(){
		return this.value;
	}
}
