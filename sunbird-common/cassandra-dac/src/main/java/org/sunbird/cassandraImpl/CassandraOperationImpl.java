package org.sunbird.cassandraImpl;

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
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

public class CassandraOperationImpl implements CassandraOperation{

	private final static Logger LOGGER = Logger.getLogger(CassandraOperationImpl.class.getName());
	
	
	public void insertCourse(Course course) {
		PreparedStatement statement = CassandraConnectionManager.getSession().prepare(CassandraQuery.Course.INSERT_COURSE);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet result = CassandraConnectionManager.getSession().execute(boundStatement.bind(course.getCourseId(), course.getCourseName(),course.getUserId(),
			course.getEnrolledDate(),course.getDescription(),course.getTocUrl(),course.getCourseProgressStatus(),course.isActive(),course.getDeltaMap()));
		LOGGER.info(result.toString());
	}
	
	public Course getCourseById(int courseId){
		 Select selectQuery = QueryBuilder.select().all().from(CassandraQuery.KEY_SPACE_NAME, CassandraQuery.Course.COURSE_TABLE_NAME);
	     Where selectWhere = selectQuery.where();
	     Clause rkClause = QueryBuilder.eq(Constants.COURSE_ID, courseId);
	     selectWhere.and(rkClause);
		 ResultSet results  = CassandraConnectionManager.getSession().execute(selectQuery);
		 MappingManager manager = new MappingManager(CassandraConnectionManager.getSession());
		 Mapper<Course> m = manager.mapper(Course.class);
		 Course course= m.map(results).one();
		 LOGGER.info(course.toString());
		 return course;
		}
	
}

