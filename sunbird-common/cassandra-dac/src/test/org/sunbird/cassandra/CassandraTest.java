package org.sunbird.cassandra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraImpl.CassandraOperationImpl;
import org.sunbird.helper.CassandraConnectionManager;
import org.sunbird.model.Content;
import org.sunbird.model.Course;
import org.sunbird.model.Delta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class CassandraTest {
	
	static CassandraOperation cassandraOperation = null;
	static Course course=null;
	static Content content  = null;
	
	@Before
	public void init(){
		cassandraOperation= new CassandraOperationImpl();
		
		// create dummy course object
		course= new Course();
    	course.setCourseId("Course 1211");
    	course.setCourseName("Course Name");
    	course.setUserId("user ID 1");
    	course.setEnrolledDate("2017-05-05");
    	course.setDescription("Teacher training Course Material");
    	course.setCourseProgressStatus("Not Started");
    	course.setActive(true);
    	Delta delta = new Delta();
    	delta.setContentId("content Id 3");
    	delta.setCreatedBy("created By Id");
    	delta.setCreatedDate("2017-05-05");
    	delta.setDescription("delta description");
    	delta.setNextContentId("next Content Id");
    	delta.setPrevContentId("previous content Id");
    	ObjectMapper mapper = new ObjectMapper();
    	String jsonInString;
		try {
			jsonInString = mapper.writeValueAsString(delta);
	    	Map<String,String> deltaMap= new HashMap<String,String>();
	    	deltaMap.put("contentId100", jsonInString);
	    	course.setDeltaMap(deltaMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		//create dummy content object
		   content = new Content();
	 	   content.setContentId("content Id 1");
	 	   content.setCourseId("courseId 1");
	 	   content.setUserId("user Id 1");
	 	   content.setDeviceId("deviceId 1");
	 	   content.setViewCount("viewCount 1");
	 	   content.setViewPosition("viewPosition 1");
	 	   content.setLastAccessTime("2013-10-15 16:16:39");
	 	   content.setLastUpdatedTime("2013-10-15 16:16:39");
	 	   content.setCompletedCount("completedCount");
	 	   content.setProgressstatus("not started");
	}
	
	@Test
	public void testConnection() {
	    assertNotNull(CassandraConnectionManager.getSession());
	}
	@Test
	public void testCourseInsertion() {
		boolean result = cassandraOperation.insertCourse(course);
    	assertEquals(true, result);
	}

	@Test
	public void testInsertContent(){
	  boolean result = cassandraOperation.insertContent(content);
 	  assertEquals(true, result);
	}
	
	@Test
	public void testGetCourse() {
		Course result = cassandraOperation.getCourseById(course.getCourseId());
		assertEquals(result.getDescription(),course.getDescription());
	}
	
	@Test
	public void testGetContent() {
		Content result = cassandraOperation.getContentById(content.getContentId());
		assertEquals(result.getViewPosition(),content.getViewPosition());
	}
	
	//@Test
	public void testdeleteContent() {
		boolean deleted=cassandraOperation.deleteContentById(content.getContentId());
		assertEquals(true,deleted);
	}
	//@Test
	public void testdeleteCourse() {
		boolean deleted=cassandraOperation.deleteCourseById(course.getCourseId());
		assertEquals(true,deleted);
	}
	@AfterClass
	public static void delete() {
		boolean result1=cassandraOperation.deleteCourseById(course.getCourseId());
		assertEquals(true,result1);
		boolean result2=cassandraOperation.deleteContentById(content.getContentId());
		assertEquals(true,result2);
    }


}
