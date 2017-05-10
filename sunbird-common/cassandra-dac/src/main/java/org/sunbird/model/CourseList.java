package org.sunbird.model;

import java.io.Serializable;
import java.util.List;

public class CourseList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6434093724441891099L;
	
	private List<Course> courseList;

	/**
	 * @return the courseList
	 */
	public List<Course> getCourseList() {
		return courseList;
	}

	/**
	 * @param courseList the courseList to set
	 */
	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}
	
	

}
