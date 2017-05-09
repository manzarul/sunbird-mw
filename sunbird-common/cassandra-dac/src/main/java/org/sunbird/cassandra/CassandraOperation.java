package org.sunbird.cassandra;

import java.util.List;

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
	
	/*
	 * @param String userId
	 * 
	 */
	public List<Course> getUserEnrolledCourse(String userId);

}
