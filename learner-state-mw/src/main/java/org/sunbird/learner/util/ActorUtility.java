package org.sunbird.learner.util;

import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.helper.CassandraConnectionManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Utility class for actors
 * Created by arvind on 11/5/17.
 */
public class ActorUtility {

    public static Map<String, DbInfo> dbInfoMap = new HashMap<String, DbInfo>();
    private static LogHelper logger = LogHelper.getInstance(ActorUtility.class.getName());

    private static Properties prop = new Properties();

    static {
        loadPropertiesFile();

        dbInfoMap.put(LearnerStateOperation.ADD_CONTENT.getValue(), getDbInfoObject(LearnerStateOperation.ADD_CONTENT.getValue()));
        dbInfoMap.put(LearnerStateOperation.GET_CONTENT.getValue(), getDbInfoObject(LearnerStateOperation.GET_CONTENT.getValue()));
        dbInfoMap.put(LearnerStateOperation.ADD_COURSE.getValue(), getDbInfoObject(LearnerStateOperation.ADD_COURSE.getValue()));
        dbInfoMap.put(LearnerStateOperation.GET_COURSE.getValue(), getDbInfoObject(LearnerStateOperation.GET_COURSE.getValue()));

        //checkCassandraDbConnections();
    }

    public static void checkCassandraDbConnections() {

        for (Map.Entry<String, DbInfo> entry : dbInfoMap.entrySet()) {

            try {
                DbInfo dbInfo = entry.getValue();
                boolean result = CassandraConnectionManager.createConnection(dbInfo.getIp(), dbInfo.getPort(), dbInfo.getUserName(),
                        dbInfo.getPassword(), dbInfo.getKeySpace());
                if(result) {
                    logger.info("CONNECTION CREATED SUCCESSFULLY FOR IP: " + dbInfo.getIp() + " : KEYSPACE :" + dbInfo.getKeySpace());
                }else{
                    logger.info("CONNECTION CREATION FAILED FOR IP: " + dbInfo.getIp() + " : KEYSPACE :" + dbInfo.getKeySpace());
                }
            } catch (ProjectCommonException ex) {
                logger.error(ex);
            }
        }

    }

    private static void loadPropertiesFile() {

        InputStream input = null;

        try {
            input = new FileInputStream("dbconfig.properties");
            // load a properties file
            prop.load(input);
            System.out.println("PROPERTIES FILE UPLOADED SUCCESSFULLY");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static DbInfo getDbInfoObject(String value) {

        DbInfo dbInfo = new DbInfo();

        String configDetails = prop.getProperty(value);
        String[] details = configDetails.split(",");

        dbInfo.setIp(details[0]);
        dbInfo.setPort(details[1]);
        dbInfo.setUserName(details[2]);
        dbInfo.setPassword(details[3]);
        dbInfo.setKeySpace(details[4]);
        dbInfo.setTableName(details[5]);

        return dbInfo;
    }

    /**
     * class to hold cassandra db info.
     */
    public static class DbInfo {
        String keySpace;
        String tableName;
        String userName;
        String password;
        String ip;
        String port;

        /**
         * @param keySpace
         * @param tableName
         * @param userName
         * @param password
         */
        DbInfo(String keySpace, String tableName, String userName, String password, String ip, String port) {
            this.keySpace = keySpace;
            this.tableName = tableName;
            this.userName = userName;
            this.password = password;
            this.ip = ip;
            this.port = port;
        }

        /**
         * No-arg constructor
         */
        DbInfo() {
        }

        public String getKeySpace() {
            return keySpace;
        }

        public void setKeySpace(String keySpace) {
            this.keySpace = keySpace;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }
    }
}
