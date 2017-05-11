package org.sunbird.common.models.response;

import java.io.Serializable;
import java.util.List;
/**
 * This class will wrap course list inside CourseList
 * 
 * @author Manzarul
 *
 */
public class CourseList implements Serializable{
	private static final long serialVersionUID = -1972876254382299493L;
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
