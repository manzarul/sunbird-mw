package org.sunbird.cassandra;

import org.sunbird.model.Course;

public interface CassandraOperation {
	
	public void insertCourse(Course course);
	
	public Course getCourseById(int id);

}
