package org.sunbird.cassandraimpl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.common.CassandraUtil;
import org.sunbird.common.Constants;
import org.sunbird.common.models.response.Response;
import org.sunbird.helper.CassandraConnectionManager;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;

/**
 * 
 * @author Amit Kumar
 * 
 * this class contains methods to interact with cassandra DB
 * 
 */
public class CassandraOperationImpl implements CassandraOperation{

	private final static Logger LOGGER = Logger.getLogger(CassandraOperationImpl.class.getName());
	
	
	@Override
	public Response insertRecord(String keyspaceName, String tableName, Map<String, Object> request) {
		Response response = new Response();
		String query = CassandraUtil.getPreparedStatement(keyspaceName,tableName,request);
		PreparedStatement statement = CassandraConnectionManager.getSession(keyspaceName).prepare(query);
		BoundStatement boundStatement = new BoundStatement(statement);
		ResultSet result=null;
		try {
			Iterator<Object> iterator = request.values().iterator(); 
			Object [] array =  new Object[request.keySet().size()];
			  int i=0;
			while (iterator.hasNext()) {
				array[i++] = iterator.next();
			}
			System.out.println(Arrays.toString(array));
		   	result = CassandraConnectionManager.getSession(keyspaceName).execute(boundStatement.bind(array));
			LOGGER.debug(result.toString());
			response.put("response", "SUCCESS");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			response.put("response", "FAILURE");
		}
		
		
		
		return response;
	}


	@Override
	public Response updateRecord(String keyspaceName, String tableName, Map<String, Object> request,
			String identifier) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Response deleteRecord(String keyspaceName, String tableName, String identifier) {
		Response response = new Response();
		try{
		Delete.Where delete = QueryBuilder.delete().from(keyspaceName, tableName)
				.where(eq(Constants.CONTENT_ID, identifier));
				//.where(eq(Constants.IDENTIFIER, identifier));
		 ResultSet result  = CassandraConnectionManager.getSession(keyspaceName).execute(delete);
		 LOGGER.debug(result.toString());
		 response.put("response", "SUCCESS");
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			response.put("response", "FAILURE");
		}
		 return response;
	}


	@Override
	public Response getRecordById(String keyspaceName, String tableName, String identifier) {
		Response response = new Response();
		try{
			Select selectQuery = QueryBuilder.select().all().from(keyspaceName, tableName);
		    Where selectWhere = selectQuery.where();
		  //  Clause clause = QueryBuilder.eq(Constants.IDENTIFIER, identifier);
		    Clause clause = QueryBuilder.eq(Constants.CONTENT_ID, identifier);
		    selectWhere.and(clause);
			ResultSet results  = CassandraConnectionManager.getSession(keyspaceName).execute(selectQuery);
			response = CassandraUtil.createResponse(results);
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			response.put("response", "FAILURE");
		}
		 return response;
	}


	@Override
	public Response getRecordsByProperty(String keyspaceName, String tableName, String propertyName, String propertyValue) {
		Response response = new Response();
		try{
			Select selectQuery = QueryBuilder.select().all().from(keyspaceName, tableName);
		    Where selectWhere = selectQuery.where();
		  //  Clause clause = QueryBuilder.eq(Constants.IDENTIFIER, identifier);
		    Clause clause = QueryBuilder.eq(propertyName, propertyValue);
		    selectWhere.and(clause);
			ResultSet results  = CassandraConnectionManager.getSession(keyspaceName).execute(selectQuery);
			response = CassandraUtil.createResponse(results);
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			response.put("response", "FAILURE");
		}
		 return response;
	}

	@Override
	public Response getRecordsByProperty(String keyspaceName, String tableName, String propertyName,
			List<String> propertyValueList) {
		Response response = new Response();
		try{
			Select selectQuery = QueryBuilder.select().all().from(keyspaceName, tableName);
		    Where selectWhere = selectQuery.where();
		    //Clause clause = QueryBuilder.eq(Constants.IDENTIFIER, identifier);
		    Clause clause = QueryBuilder.in(propertyName, propertyValueList);
		    selectWhere.and(clause);
			ResultSet results  = CassandraConnectionManager.getSession(keyspaceName).execute(selectQuery);
			response = CassandraUtil.createResponse(results);
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			response.put("response", "FAILURE");
		}
		 return response;
	}


	@Override
	public Response getRecordsByProperties(String keyspaceName, String tableName, Map<String, Object> propertyMap) {
		Response response = new Response();
		try{
			Select selectQuery = QueryBuilder.select().all().from(keyspaceName, tableName);
		    Where selectWhere = selectQuery.where();
		    for (Entry<String, Object> entry : propertyMap.entrySet())
		    {
		    	if(entry.getValue() instanceof String){
			    	Clause clause = QueryBuilder.eq(entry.getKey(), entry.getValue());
				    selectWhere.and(clause);
		    	}else if(entry.getValue() instanceof List){
		    		Clause clause = QueryBuilder.in(entry.getKey(), entry.getValue());
				    selectWhere.and(clause);
		    	}
		    }
		    System.out.println(selectQuery);
			ResultSet results  = CassandraConnectionManager.getSession(keyspaceName).execute(selectQuery);
			response = CassandraUtil.createResponse(results);
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			response.put("response", "FAILURE");
		}
		 return response;
	}

}

