package org.sunbird.cassandra;

import org.sunbird.model.Course;

public interface CassandraOperation {
	/*
	 * @param Course course
	 * 
	 */
	public void insertCourse(Course course);
	
	/*
	 * @param String courseId
	 * 
	 */
	public Course getCourseById(String courseId);
	
	/*
	 * @param String courseId
	 * 
	 */
	public boolean deleteCourseById(String courseId);

}
