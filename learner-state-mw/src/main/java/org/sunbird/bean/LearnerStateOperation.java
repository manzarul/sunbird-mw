package org.sunbird.bean;
/**
 * This enum will contains different operation
 *  for a learner {addCourse, getCourse, update , getContent}
 * @author Manzarul
 *
 */
public enum LearnerStateOperation {
	ADD_COURSE("addCourse"),GET_COURSE("getCourse"),UPDATE_TOC("updateToc"),GET_COURSE_BY_ID("getCourseById"),ADD_CONTENT("addContent"),
	GET_CONTENT("getContent");

	private String value;

	/**
	 * constructor
	 * @param value
	 */
	LearnerStateOperation(String value){
		this.value=value;
	}

	/**
	 * returns the enum value
	 * @return
	 */
	public String getValue(){
		return this.value;
	}
}
