package org.sunbird.learner.util;

import org.sunbird.bean.LearnerStateOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for actors
 * Created by arvind on 11/5/17.
 */
public class ActorUtility {

    public static Map<String , DbInfo> dbInfoMap = new HashMap<String , DbInfo>();

    static{
        dbInfoMap.put(LearnerStateOperation.ADD_CONTENT.getValue(), new DbInfo("cassandraKeySpace" , "content_consumption" ,"username","password","ip","port"));
        dbInfoMap.put(LearnerStateOperation.GET_CONTENT.getValue(), new DbInfo("cassandraKeySpace" , "content_consumption","username","password","ip","port"));
        dbInfoMap.put(LearnerStateOperation.GET_COURSE_BY_ID.getValue(), new DbInfo("cassandraKeySpace" , "course_enrollment","username","password","ip","port"));
        dbInfoMap.put(LearnerStateOperation.UPDATE_TOC.getValue(), new DbInfo("cassandraKeySpace" , "content_consumption","username","password","ip","port"));
        dbInfoMap.put(LearnerStateOperation.ADD_COURSE.getValue(), new DbInfo("cassandraKeySpace" , "course_enrollment","username","password","ip","port"));
        dbInfoMap.put(LearnerStateOperation.GET_COURSE.getValue(), new DbInfo("cassandraKeySpace" , "course_enrollment","username","password","ip","port"));
    }

    /**
     * class to hold cassandra db info.
     */
    public static class DbInfo{
        String keySpace;
        String tableName;
        String userName;
        String password;
        String ip;
        String port;

        /**
         *
         * @param keySpace
         * @param tableName
         * @param userName
         * @param password
         */
        DbInfo(String keySpace  , String tableName, String userName , String password, String ip , String port){
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
        DbInfo(){
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
