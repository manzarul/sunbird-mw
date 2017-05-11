package org.sunbird.learner.util;

import org.sunbird.bean.LearnerStateOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arvind on 11/5/17.
 */
public class ActorUtility {

    public static Map<String , DbInfo> dbInfoMap = new HashMap<String , DbInfo>();

    static{
        dbInfoMap.put(LearnerStateOperation.ADD_CONTENT.getValue(), new DbInfo("cassandraKeySpace" , "content"));
        dbInfoMap.put(LearnerStateOperation.GET_CONTENT.getValue(), new DbInfo("cassandraKeySpace" , "content"));
        dbInfoMap.put(LearnerStateOperation.GET_COURSE_BY_ID.getValue(), new DbInfo("cassandraKeySpace" , "course"));
        dbInfoMap.put(LearnerStateOperation.UPDATE_TOC.getValue(), new DbInfo("cassandraKeySpace" , "content"));
        dbInfoMap.put(LearnerStateOperation.ADD_COURSE.getValue(), new DbInfo("cassandraKeySpace" , "course"));
        dbInfoMap.put(LearnerStateOperation.GET_COURSE.getValue(), new DbInfo("cassandraKeySpace" , "course"));
    }

    public static class DbInfo{
        String keySpace;
        String tableName;

        DbInfo(String keySpace  , String tableName){
            this.keySpace = keySpace;
            this.tableName = tableName;
        }

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
    }
}
