package org.sunbird.cassandra;

import java.util.List;
import java.util.Map;

import org.sunbird.common.models.response.Response;
import org.sunbird.model.Content;
import org.sunbird.model.ContentList;
import org.sunbird.model.Course;
import org.sunbird.model.CourseList;

public interface CassandraOperation {
	/*
	 * @param Course course
	 * 
	 */
	public boolean insertCourse(Course course);
	
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
	public CourseList getUserEnrolledCourse(String userId);
	

	/*
	 * @param String userId,List<String> contentIdList
	 * 
	 */
	public ContentList getContentState(String userId,List<String> contentIdList);
	
	/*
	 * @param String contentId
	 * 
	 */
	public Content getContentById(String contentId);
	
	/* 
	 * @param Content contentId
	 * 
	 */
	public boolean insertContent(Content content);
	
	/*
	 * @param String contentId
	 * 
	 */
	public boolean deleteContentById(String contentId);
	/**
	 * used to insert record in cassandra db 
	 * @param keyspaceName
	 * @param tableName
	 * @param request
	 * @return Response
	 */
	public Response insertRecord(String keyspaceName,String tableName,Map<String,Object> request);

	/**
	 * used to update record in cassandra db 
	 * @param keyspaceName
	 * @param tableName
	 * @param request
	 * @param identifier
	 * @return Response
	 */
	public Response updateRecord(String keyspaceName,String tableName,Map<String,Object> request,String identifier);
	
	/**
	 * used to delete record in cassandra db
	 * @param keyspaceName
	 * @param tableName
	 * @param identifier
	 * @return Response
	 */
	public Response deleteRecord(String keyspaceName,String tableName,String identifier);
	
	/**
	 * used to fetch record based on primary key
	 * @param keyspaceName
	 * @param tableName
	 * @param identifier
	 * @return Response 
	 */
	public Response  getById(String keyspaceName,String tableName,String identifier);
	
	/**
	 * used to fetch record based on given parameter and it's value
	 * @param keyspaceName
	 * @param tableName
	 * @param propertyName
	 * @param propertyValue
	 * @return Response
	 */
	public Response  getByProperty(String keyspaceName,String tableName,String propertyName,String propertyValue);
	
	/**
	 * used to fetch record based on given parameter list and their values
	 * @param keyspaceName
	 * @param tableName
	 * @param propertyMap
	 * @return Response
	 */
	public Response  getByProperties(String keyspaceName,String tableName,Map<String,String> propertyMap);
}
