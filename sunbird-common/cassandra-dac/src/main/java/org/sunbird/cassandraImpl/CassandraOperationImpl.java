package org.sunbird.cassandraImpl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.common.CassandraQuery;
import org.sunbird.common.Constants;
import org.sunbird.helper.CassandraConnectionManager;
import org.sunbird.model.Course;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

public class CassandraOperationImpl implements CassandraOperation{

	private final static Logger LOGGER = Logger.getLogger(CassandraOperationImpl.class.getName());
	
	/*
	 * (non-Javadoc)
	 * @see org.sunbird.cassandra.CassandraOperation#insertCourse(org.sunbird.model.Course)
	 * @param course
	 * this method insert course info to db
	 */
	public void insertCourse(Course course) {
		LOGGER.info("method started==");
		PreparedStatement statement = CassandraConnectionManager.getSession().prepare(CassandraQuery.Course.INSERT_COURSE);
		BoundStatement boundStatement = new BoundStatement(statement);
		try {
	    LOGGER.info("trying to insert in Cassandra=="+ statement.getQueryString());	
		ResultSet result = CassandraConnectionManager.getSession().execute(boundStatement.bind(course.getCourseId(), course.getCourseName(),course.getUserId(),
			course.getEnrolledDate(),course.getDescription(),course.getTocUrl(),course.getCourseProgressStatus(),course.isActive(),course.getDeltaMap()));
		LOGGER.info(result.toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sunbird.cassandra.CassandraOperation#getCourseById(String)
	 * @param courseId
	 * used to fetch course information based on course id
	 */
	public Course getCourseById(String courseId){
		Course course=null;
		try{
		 Select selectQuery = QueryBuilder.select().all().from(CassandraQuery.KEY_SPACE_NAME, CassandraQuery.Course.COURSE_TABLE_NAME);
	     Where selectWhere = selectQuery.where();
	     Clause rkClause = QueryBuilder.eq(Constants.COURSE_ID, courseId);
	     selectWhere.and(rkClause);
	     LOGGER.info(selectQuery);
		 ResultSet results  = CassandraConnectionManager.getSession().execute(selectQuery);
		 MappingManager manager = new MappingManager(CassandraConnectionManager.getSession());
		 Mapper<Course> m = manager.mapper(Course.class);
		 course= m.map(results).one();
		// LOGGER.info(course.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		 return course;
		}
	
	/*
	 * (non-Javadoc)
	 * @see org.sunbird.cassandra.CassandraOperation#getCourseById(String)
	 * @param courseId
	 * used to delete course information based on course id
	 */
	public boolean deleteCourseById(String courseId){
		Delete.Where delete = QueryBuilder.delete().from(CassandraQuery.KEY_SPACE_NAME, CassandraQuery.Course.COURSE_TABLE_NAME)
				.where(eq(Constants.COURSE_ID, courseId));
		 ResultSet results  = CassandraConnectionManager.getSession().execute(delete);
		 LOGGER.info(results.toString());
		 return results.isExhausted();
		}

	/*
	 * (non-Javadoc)
	 * @see org.sunbird.cassandra.CassandraOperation#getUserEnrolledCourse(String)
	 * @param courseId
	 * used to retrieve list of enrolled course information based on user id
	 */
	@Override
	public List<Course> getUserEnrolledCourse(String userId) {
		Select selectQuery = QueryBuilder.select().all().from(CassandraQuery.KEY_SPACE_NAME, CassandraQuery.Course.COURSE_TABLE_NAME);
	    Where selectWhere = selectQuery.where();
	    Clause rkClause = QueryBuilder.eq(Constants.USER_ID, userId);
	    selectWhere.and(rkClause);
		ResultSet results  = CassandraConnectionManager.getSession().execute(selectQuery);
		List<Course> courseList = new ArrayList<Course>();
		while (!results.isExhausted()) {
			MappingManager manager = new MappingManager(CassandraConnectionManager.getSession());
			Mapper<Course> m = manager.mapper(Course.class);
			courseList.add(m.map(results).one());
		}
		return courseList;
	}
		
}

