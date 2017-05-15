package org.sunbird.helper;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.CassandraUtil;
import org.sunbird.common.models.response.Response;

import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class App {

	public static void main(String[] args) {
		boolean bool= CassandraConnectionManager.createConnection("127.0.0.1", "9042", "cassandra", "password", "cassandraKeySpace");
		Map<String,Object> coursemap = new HashMap<>();
		if(bool){
			CassandraOperation operation= new CassandraOperationImpl();
			/*Map<String,Object> contentmap = new HashMap<>();
			//Map<String,Object> coursemap = new HashMap<>();
				    	   
	    	contentmap.put("contentId", "contentId1");
	   		contentmap.put("viewCount", "viewCount 1");
	   		contentmap.put("completedCount", "completedCount1");
	   		contentmap.put("status", 1);
	   		contentmap.put("userId", "userId2");
	   		contentmap.put("lastUpdatedTime", new Timestamp(System.currentTimeMillis()) );
	   		contentmap.put("lastAccessTime", new Timestamp(System.currentTimeMillis()));
	   		contentmap.put("lastCompletedTime", new Timestamp(System.currentTimeMillis()));
	   		contentmap.put("viewPosition", "viewPosition 1");
	   		contentmap.put("id", "contentId1##userId2");
	   		
	   		coursemap.put("courseName", "courseName1");
	   		coursemap.put("userId", "userId2");
	   		coursemap.put("courseId", "courseId2");
	   		coursemap.put("enrolledDate", new Timestamp(System.currentTimeMillis()));
	   		coursemap.put("description", "description");
	   		coursemap.put("tocUrl", "tocUrl");
	   		coursemap.put("status", "1");
	   		coursemap.put("active", true);
	   		coursemap.put("delta", "delta as json string");
	   		coursemap.put("id", "courseId2##userId2");
	    	   
	   		operation.insertRecord("cassandraKeySpace", "course_enrollment", coursemap);
	   		operation.insertRecord("cassandraKeySpace", "content_consumption", contentmap);
	   		operation.getRecordById("cassandraKeySpace", "course_enrollment", "courseId1##userId1");
	   		operation.getRecordById("cassandraKeySpace", "content_consumption", "contentId1234567890##userId2");
			//Response response=operation.getRecordById("cassandraKeySpace", "content", "contentId1##userId24");
			//Response response=operation.getRecordsByProperty("cassandraKeySpace", "content", "courseid", "courseId 1");
			//Response response=operation.getRecordsByProperties("cassandraKeySpace", "content", map);
			//Response response=operation.deleteRecord("cassandraKeySpace", "content", "content Id 1234567890");
			//System.out.println(response.getResult().get("response"));
	    	   //Response response=operation.insertRecord("cassandraKeySpace", "content", map);
			//Map<String, Object> map1=response.getResult();
			List<Map<String, Object>> list = (List<Map<String, Object>>) map1.get("response");
			for(int i=0;i<list.size();i++){
				Map<String, Object> map3 = list.get(i);
			for (Entry<String, Object> entry : map3.entrySet())
		    {
		    	//Clause clause = QueryBuilder.eq(entry.getKey(), entry.getValue());
			   // selectWhere.and(clause);
		        System.out.println(entry.getKey() + "/" + entry.getValue());
		        
		    }System.out.println("----------------------------------------------------------------------------------------");
			}
			
			//System.out.println(response.get("response"));
			//operation.insertRecord("cassandraKeySpace", "content", map);
		
			coursemap.put("courseName", "courseName1");
	   		coursemap.put("userId", "userId2");
	   		coursemap.put("courseId", "courseId2");
	   		coursemap.put("enrolledDate", new Timestamp(System.currentTimeMillis()));
	   		coursemap.put("description", "description");
	   		coursemap.put("tocUrl", "tocUrl");
	   		coursemap.put("status", "1");
	   		coursemap.put("active", true);
	   		coursemap.put("delta", "delta as json string");
			String str= CassandraUtil.getUpdateQueryStatement("cassandraKeySpace", "course_enrollment",  coursemap);
		    System.out.println(str);
		    */
			// AND status=1; userId='userId2' AND
		    CassandraConnectionManager.getSession("cassandraKeySpace").execute( "SELECT * FROM cassandraKeySpace.course_enrollment WHERE userId='userId2';");
		   Response res= operation.getRecordsByProperty("cassandraKeySpace", "course_enrollment", "userId", "userId2");
		   System.out.println(res.get("response"));
		}

	}

}
