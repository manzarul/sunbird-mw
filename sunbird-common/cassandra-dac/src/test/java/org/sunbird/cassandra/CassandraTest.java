package org.sunbird.cassandra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.models.response.Response;
import org.sunbird.helper.CassandraConnectionManager;


public class CassandraTest {/*
	
	static CassandraOperation cassandraOperation = null;
	static CassandraOperation operation= new CassandraOperationImpl();
	Map<String,Object> map = new HashMap<>();
	@Before
	public void init(){
		cassandraOperation= new CassandraOperationImpl();
		
		
		//map2.put("userId", "user Id 2");
		///map2.put("courseid", "courseId 1");
		
    	   map.put("contentId", "content Id 1234567890");
    	   map.put("viewCount", "viewCount 1");
    	   map.put("completedCount", "completedCount1");
    	   map.put("progressstatus", "progressstatus 1");
    	   map.put("userId", "user Id 2");
    	   map.put("courseId", "courseId 1");
    	   map.put("lastUpdatedTime", "2013-10-15 16:16:39");
    	   map.put("deviceId", "deviceId 1");
    	   map.put("viewPosition", "viewPosition 1");
    	   
    	   
   		//map1.put("userId", "user Id 2");
   		//map1.put("courseid", "courseId 1");
	}
	
	@Before
	public void testConnection() {
		boolean bool= CassandraConnectionManager.createConnection("127.0.0.1", "9042", "cassandra", "password", "cassandraKeySpace");
		assertEquals(true,bool);
	}
	@Test
	public void testInsertion() {
		Response response=operation.insertRecord("cassandraKeySpace", "content", map);
    	assertEquals("SUCCESS", response.get("response"));
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetRecordById() {
		Response response=operation.getRecordById("cassandraKeySpace", "content", "content Id 1234567890");
		assertEquals(1,((List<Map<String, Object>>)(response.get("response"))).size());
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testgetRecordByProperty() {
		Response response=operation.getRecordsByProperty("cassandraKeySpace", "content", "courseid", "courseId 1");
		assertTrue(((List<Map<String, Object>>)(response.get("response"))).size()>0);
	}
	
	
	
	@AfterClass
	public static void delete() {
		operation.deleteRecord("cassandraKeySpace", "content", "content Id 1234567890");
    }


*/}
