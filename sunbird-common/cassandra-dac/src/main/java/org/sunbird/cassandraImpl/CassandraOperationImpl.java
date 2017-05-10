package org.sunbird.cassandraImpl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.common.CassandraQuery;
import org.sunbird.common.Constants;
import org.sunbird.helper.CassandraConnectionManager;
import org.sunbird.model.Content;
import org.sunbird.model.ContentList;
import org.sunbird.model.Course;
import org.sunbird.model.CourseList;

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
	public boolean insertCourse(Course course) {
		PreparedStatement statement = CassandraConnectionManager.getSession().prepare(CassandraQuery.Course.INSERT_COURSE);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet result=null;
		try {
		result = CassandraConnectionManager.getSession().execute(boundStatement.bind(course.getCourseId(), course.getCourseName(),course.getUserId(),
			course.getEnrolledDate(),course.getDescription(),course.getTocUrl(),course.getCourseProgressStatus(),course.isActive(),course.getDeltaMap()));
			LOGGER.debug(result.toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return result.wasApplied();
		
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
	     Clause clause = QueryBuilder.eq(Constants.COURSE_ID, courseId);
	     selectWhere.and(clause);
	     LOGGER.debug(selectQuery);
		 ResultSet result  = CassandraConnectionManager.getSession().execute(selectQuery);
		 MappingManager manager = new MappingManager(CassandraConnectionManager.getSession());
		 Mapper<Course> m = manager.mapper(Course.class);
		 course= m.map(result).one();
		}catch(Exception e){
			LOGGER.error(e.getMessage());
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
		 ResultSet result  = CassandraConnectionManager.getSession().execute(delete);
		 LOGGER.debug(result.toString());
		 return result.wasApplied();
		}

	/*
	 * (non-Javadoc)
	 * @see org.sunbird.cassandra.CassandraOperation#getUserEnrolledCourse(String)
	 * @param courseId
	 * used to retrieve list of enrolled course information based on user id
	 */
	@Override
	public CourseList getUserEnrolledCourse(String userId) {
		Select selectQuery = QueryBuilder.select().all().from(CassandraQuery.KEY_SPACE_NAME, CassandraQuery.Course.COURSE_TABLE_NAME);
	    Where selectWhere = selectQuery.where();
	    Clause clause = QueryBuilder.eq(Constants.USER_ID, userId);
	    selectWhere.and(clause);
		ResultSet result  = CassandraConnectionManager.getSession().execute(selectQuery);
		List<Course> list = new ArrayList<Course>();
		CourseList courseList = new CourseList();
		while (!result.isExhausted()) {
			MappingManager manager = new MappingManager(CassandraConnectionManager.getSession());
			Mapper<Course> m = manager.mapper(Course.class);
			list.add(m.map(result).one());
		}
		
		courseList.setCourseList(list);
		return courseList;
	}
		
	
	/*
	 * (non-Javadoc)
	 * @see org.sunbird.cassandra.CassandraOperation#insertContent(org.sunbird.model.Content)
	 * @param course
	 * this method insert content info to db
	 */
	public boolean insertContent(Content content) {
		PreparedStatement statement = CassandraConnectionManager.getSession().prepare(CassandraQuery.Content.INSERT_CONTENT);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet result =null;
		try {
		result = CassandraConnectionManager.getSession().execute(boundStatement.bind(content.getContentId(),content.getViewCount(),content.getLastAccessTime(),content.getCompletedCount(),
				content.getProgressstatus(),content.getUserId(),content.getCourseId(),content.getLastUpdatedTime(),content.getDeviceId(),content.getViewPosition()));
		LOGGER.debug(result.toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return result.wasApplied();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sunbird.cassandra.CassandraOperation#getCourseById(String,List<String>)
	 * @param courseId
	 * used to fetch course information based on course id
	 */
	public Content getContentById(String contentId){
		Content content=null;
		try{
			Select selectQuery = QueryBuilder.select().all().from(CassandraQuery.KEY_SPACE_NAME, CassandraQuery.Content.CONTENT_TABLE_NAME);
		    Where selectWhere = selectQuery.where();
		    Clause clause = QueryBuilder.eq(Constants.CONTENT_ID, contentId);
		    selectWhere.and(clause);
			ResultSet results  = CassandraConnectionManager.getSession().execute(selectQuery);
			MappingManager manager = new MappingManager(CassandraConnectionManager.getSession());
			Mapper<Content> m = manager.mapper(Content.class);
			content= m.map(results).one();
		}catch(Exception e){
			LOGGER.error(e.getMessage());
		}
		 return content;
		}

	/*
	 * (non-Javadoc)
	 * @see org.sunbird.cassandra.CassandraOperation#deleteContentById(String)
	 * @param courseId
	 * used to delete content information based on content id
	 */
	@Override
	public boolean deleteContentById(String contentId) {
		Delete.Where delete = QueryBuilder.delete().from(CassandraQuery.KEY_SPACE_NAME, CassandraQuery.Content.CONTENT_TABLE_NAME)
				.where(eq(Constants.CONTENT_ID, contentId));
		 ResultSet result  = CassandraConnectionManager.getSession().execute(delete);
		 LOGGER.debug(result.toString());
		 return result.wasApplied();
	}

	/*
	 * (non-Javadoc)
	 * @see org.sunbird.cassandra.CassandraOperation#getContentState(String,List<String>)
	 * @param courseId
	 * used to retrieve list of enrolled course information based on user id and list of content id
	 */
	@Override
	public ContentList getContentState(String userId, List<String> contentIdList) {
		Select selectQuery = QueryBuilder.select().all().from(CassandraQuery.KEY_SPACE_NAME, CassandraQuery.Content.CONTENT_TABLE_NAME);
	    Where selectWhere = selectQuery.where();
	    Clause clause1 = QueryBuilder.eq(Constants.USER_ID, userId);
	    //Clause clause2 = QueryBuilder.in(Constants.CONTENT_ID, contentIdList);
	    selectWhere.and(clause1);
	    //selectWhere.and(clause2);
		ResultSet result  = CassandraConnectionManager.getSession().execute(selectQuery);
		List<Content> list = new ArrayList<Content>();
		Map<String ,Content> map = new HashMap<String ,Content>();
		while (!result.isExhausted()) {
			MappingManager manager = new MappingManager(CassandraConnectionManager.getSession());
			Mapper<Content> m = manager.mapper(Content.class);
			Content content =m.map(result).one();
			map.put(content.getContentId(), content);
		}
		for(String contentId : contentIdList){
			Content content = map.get(contentId);
			if(null != content){
				list.add(content);
			}
		}
		
		ContentList contentList= new ContentList();
		contentList.setContentList(list);
		map=null;
		return contentList;
	}
}

