package org.sunbird.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.models.response.Response;

import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class App {

	public static void main(String[] args) {
		boolean bool= CassandraConnectionManager.createConnection("127.0.0.1", "9042", "cassandra", "password", "cassandraKeySpace");
		if(bool){
			CassandraOperation operation= new CassandraOperationImpl();
			Map<String,Object> map = new HashMap<>();
			//map.put("userId", "user Id 2");
			//map.put("courseid", "courseId 1");
			
	    	   map.put("contentId", "content Id 1234567890");
	    	   map.put("viewCount", "viewCount 1");
	    	   map.put("completedCount", "completedCount1");
	    	   map.put("progressstatus", "progressstatus 1");
	    	   map.put("userId", "user Id 2");
	    	   map.put("courseId", "courseId 1");
	    	   map.put("lastUpdatedTime", "2013-10-15 16:16:39");
	    	   map.put("deviceId", "deviceId 1");
	    	   map.put("viewPosition", "viewPosition 1");
			//Response response=operation.getRecordById("cassandraKeySpace", "content", "content Id 1234567890");
			//Response response=operation.getRecordsByProperty("cassandraKeySpace", "content", "courseid", "courseId 1");
			//Response response=operation.getRecordsByProperties("cassandraKeySpace", "content", map);
			//Response response=operation.deleteRecord("cassandraKeySpace", "content", "content Id 1234567890");
			//System.out.println(response.getResult().get("response"));
	    	   Response response=operation.insertRecord("cassandraKeySpace", "content", map);
			/*Map<String, Object> map1=response.getResult();
			List<Map<String, Object>> list = (List<Map<String, Object>>) map1.get("response");
			for(int i=0;i<list.size();i++){
				Map<String, Object> map3 = list.get(i);
			for (Entry<String, Object> entry : map3.entrySet())
		    {
		    	//Clause clause = QueryBuilder.eq(entry.getKey(), entry.getValue());
			   // selectWhere.and(clause);
		        System.out.println(entry.getKey() + "/" + entry.getValue());
		        
		    }System.out.println("----------------------------------------------------------------------------------------");
			}*/
			
			System.out.println(response.get("response"));
			//operation.insertRecord("cassandraKeySpace", "content", map);
		}

	}

}
