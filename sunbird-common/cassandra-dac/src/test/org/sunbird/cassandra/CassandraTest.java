package org.sunbird.cassandra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraImpl.CassandraOperationImpl;
import org.sunbird.helper.CassandraConnectionManager;
import org.sunbird.model.Course;
import org.sunbird.model.Delta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CassandraTest {
	
	CassandraOperation cassandraOperation = null;
	
	@Before
	public void init(){
		cassandraOperation= new CassandraOperationImpl();
	}
	
	@Test
	public void testConnection() {
	    assertNotNull(CassandraConnectionManager.getSession());
	}
	@Test
	public void testCourseInsertion() {
		
		//courseId,courseName,userId, enrolledDate, description, tocUrl,courseProgressStatus,active,deltaMap
    	Course course= new Course();
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
    	cassandraOperation.insertCourse(course);
	}

	@Test
	public void testGetCourse() {
		Course course = cassandraOperation.getCourseById("Course 1211");
		assertEquals(course.getDescription(),"Teacher training Course Material");
	}
	
	@Test
	public void testdeleteCourse() {
		boolean deleted=cassandraOperation.deleteCourseById("Course 1211");
		assertEquals(deleted,true);
	}

}
