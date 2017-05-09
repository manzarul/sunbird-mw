package org.sunbird.common;

/*
 * @author Amit Kumar
 */
public interface CassandraQuery {
	public static final String KEY_SPACE_NAME="cassandraKeySpace";


	public interface Course{
		public static final String COURSE_TABLE_NAME ="course";
		public static final String INSERT_COURSE="INSERT INTO cassandraKeySpace.course" + "(courseId,courseName,userId, enrolledDate, description, tocUrl,courseProgressStatus,active,deltaMap)"
				+ "VALUES (?,?,?,?,?,?,?,?,?);";
	}
	
	public interface Content{
		public static final String COURSE_TABLE_NAME ="content";
		public static final String INSERT_CONTENT="INSERT INTO cassandraKeySpace.content" + "(contentId,viewCount,lastAccessTime, completedCount, progressstatus, userId,courseId,lastUpdatedTime,deviceId,viewPosition)"
				+ "VALUES (?,?,?,?,?,?,?,?,?,?);";
	}
}
