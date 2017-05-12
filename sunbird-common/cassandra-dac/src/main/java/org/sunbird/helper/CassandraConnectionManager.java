/**
 * 
 */
package org.sunbird.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.Constants;
import org.sunbird.common.PropertiesCache;

import com.datastax.driver.core.AtomicMonotonicTimestampGenerator;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.QueryLogger;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DefaultRetryPolicy;

/**
 * This class will handle cassandra database 
 * connection and session.
 * @author Manzarul
 * @author Amit Kumar
 */
public final class CassandraConnectionManager {
	private final static Logger LOGGER = Logger.getLogger(CassandraOperationImpl.class.getName());
	//Map<keySpaceName,Session>
    private static Map<String,Session> cassandraSessionMap = new HashMap<>();
    private static Map<String,Cluster> cassandraclusterMap = new HashMap<>();
    
    /**
     * @author Amit Kumar
     * @param ip
     * @param port
     * @param userName
     * @param password
     * @return boolean
     * 
     */
	public static boolean createConnection(String ip,String port,String userName,String password,String keyspace){
		boolean connection = false;
		Cluster cluster= null;
		Session session = null;
		try{
			   PropertiesCache cache = PropertiesCache.getInstance();
			   PoolingOptions poolingOptions = new PoolingOptions();
			   poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL,  Integer.parseInt(cache.getProperty(Constants.CORE_CONNECTIONS_PER_HOST_FOR_LOCAL)));
			   poolingOptions.setMaxConnectionsPerHost( HostDistance.LOCAL, Integer.parseInt(cache.getProperty(Constants.MAX_CONNECTIONS_PER_HOST_FOR_LOCAl)));
			   poolingOptions.setCoreConnectionsPerHost(HostDistance.REMOTE, Integer.parseInt(cache.getProperty(Constants.CORE_CONNECTIONS_PER_HOST_FOR_REMOTE)));
			   poolingOptions.setMaxConnectionsPerHost( HostDistance.REMOTE, Integer.parseInt(cache.getProperty(Constants.MAX_CONNECTIONS_PER_HOST_FOR_REMOTE)));
			   poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL, Integer.parseInt(cache.getProperty(Constants.MAX_REQUEST_PER_CONNECTION)));
			   poolingOptions.setHeartbeatIntervalSeconds(Integer.parseInt(cache.getProperty(Constants.HEARTBEAT_INTERVAL)));
			   poolingOptions.setPoolTimeoutMillis(Integer.parseInt(cache.getProperty(Constants.POOL_TIMEOUT)));
			   cluster = Cluster
		        		.builder()
		        		.addContactPoint(ip)
		        		.withPort(Integer.parseInt(port))
		        		.withProtocolVersion(ProtocolVersion.V3)
		        		.withRetryPolicy(DefaultRetryPolicy.INSTANCE)
		        		.withTimestampGenerator(new AtomicMonotonicTimestampGenerator())
		        		.withPoolingOptions(poolingOptions)
		        		.withCredentials(userName,password)
		        		.build();
		        QueryLogger queryLogger = QueryLogger.builder().withConstantThreshold(Integer.parseInt(cache.getProperty(Constants.QUERY_LOGGER_THRESHOLD))).build();
		        cluster.register(queryLogger);
		        session = cluster.connect(keyspace);
		        
		        if(null != session){
		        	connection = true;
		        	cassandraSessionMap.put(keyspace, session);
		        	cassandraclusterMap.put(keyspace, cluster);
		        }
			   }catch(Exception e){
				   LOGGER.error(e);
			   }
		        final Metadata metadata = cluster.getMetadata();
		        String msg = String.format("Connected to cluster: %s", metadata.getClusterName());
		        LOGGER.debug(msg);
		         
		        for (final Host host : metadata.getAllHosts()){
			        msg = String.format("Datacenter: %s; Host: %s; Rack: %s",
			        host.getDatacenter(),
			        host.getAddress(),
			        host.getRack());
			        LOGGER.debug(msg);
		        }
		        return connection;
	}
	
	/**
	 * @author Amit Kumar
	 * @param ip
	 * @return Session
	 */
	public static Session getSession(String keyspaceName) {
        return cassandraSessionMap.get(keyspaceName);
    }
 
	/**
	 * @author Amit Kumar
	 * 
	 * this method will close all the session and cluster
	 */
	public static void  shutdownhook() {
		for(String key: cassandraSessionMap.keySet()){
			cassandraSessionMap.get(key).close();
			cassandraclusterMap.get(key).close();
		}
    }

}
