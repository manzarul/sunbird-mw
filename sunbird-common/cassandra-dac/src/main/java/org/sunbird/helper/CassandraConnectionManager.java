/**
 * 
 */
package org.sunbird.helper;

import org.apache.log4j.Logger;
import org.sunbird.cassandraImpl.CassandraOperationImpl;
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
public class CassandraConnectionManager {
	private final static Logger LOGGER = Logger.getLogger(CassandraOperationImpl.class.getName());
	private static Cluster cluster;
    private static Session session;
    
    /*
     * creating cassandra connection and session
     */
	static{
		try{
			   PropertiesCache instance = PropertiesCache.getInstance();
			   PoolingOptions poolingOptions = new PoolingOptions();
			   poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL,  Integer.parseInt(instance.getProperty(Constants.CORE_CONNECTIONS_PER_HOST_FOR_LOCAL)));
			   poolingOptions.setMaxConnectionsPerHost( HostDistance.LOCAL, Integer.parseInt(instance.getProperty(Constants.MAX_CONNECTIONS_PER_HOST_FOR_LOCAl)));
			   poolingOptions.setCoreConnectionsPerHost(HostDistance.REMOTE, Integer.parseInt(instance.getProperty(Constants.CORE_CONNECTIONS_PER_HOST_FOR_REMOTE)));
			   poolingOptions.setMaxConnectionsPerHost( HostDistance.REMOTE, Integer.parseInt(instance.getProperty(Constants.MAX_CONNECTIONS_PER_HOST_FOR_REMOTE)));
			   poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL, Integer.parseInt(instance.getProperty(Constants.MAX_REQUEST_PER_CONNECTION)));
			   poolingOptions.setHeartbeatIntervalSeconds(Integer.parseInt(instance.getProperty(Constants.HEARTBEAT_INTERVAL)));
			   poolingOptions.setPoolTimeoutMillis(Integer.parseInt(instance.getProperty(Constants.POOL_TIMEOUT)));
		        cluster = Cluster
		        		.builder()
		        		.addContactPoint(instance.getProperty(Constants.CONTACT_POINT))
		        		.withPort(Integer.parseInt(instance.getProperty(Constants.PORT)))
		        		.withProtocolVersion(ProtocolVersion.V3)
		        		.withRetryPolicy(DefaultRetryPolicy.INSTANCE)
		        		.withTimestampGenerator(new AtomicMonotonicTimestampGenerator())
		        		.withPoolingOptions(poolingOptions)
		        		.withCredentials(instance.getProperty(Constants.CASSANDRA_USERNAME), instance.getProperty(Constants.CASSANDRA_PASSWORD))
		        		.build();
		        QueryLogger queryLogger = QueryLogger.builder().withConstantThreshold(Integer.parseInt(instance.getProperty(Constants.QUERY_LOGGER_THRESHOLD))).build();
		        cluster.register(queryLogger);
		        session = cluster.connect();
			   }catch(Exception e){
				   LOGGER.error(e);
			   }
		        final Metadata metadata = cluster.getMetadata();
		        String msg = String.format("Connected to cluster: %s", metadata.getClusterName());
		        LOGGER.info(msg);
		         
		        for (final Host host : metadata.getAllHosts()){
			        msg = String.format("Datacenter: %s; Host: %s; Rack: %s",
			        host.getDatacenter(),
			        host.getAddress(),
			        host.getRack());
			        LOGGER.info(msg);
		        }
	}
	
	/*
	 * exposing session for application 
	 */
	public static Session getSession() {
        return CassandraConnectionManager.session;
    }
 

}
