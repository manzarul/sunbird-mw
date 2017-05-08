package org.sunbird.common;

public interface CassandraQuery {
	public static final String KEY_SPACE_NAME="cassandraKeySpace";


	public interface Course{
		public static final String COURSE_TABLE_NAME ="course";
		public static final String INSERT_COURSE="INSERT INTO cassandraKeySpace.course" + "(courseId,courseName,userId, enrolledDate, description, tocUrl,courseProgressStatus,active,deltaMap)"
				+ "VALUES (?,?,?,?,?,?,?,?,?);";
	}
}
