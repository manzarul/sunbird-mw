package org.sunbird.learner.actors;


import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
/*import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.request.Request;
import org.sunbird.common.responsecode.ResponseCode;*/
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.request.Request;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.learner.util.ActorUtility;

/**
 * This actor will handle leaner's state operation like get course , get content etc.
 * @author Manzarul
 */
public class LearnerStateActor extends UntypedAbstractActor {

    private CassandraOperation cassandraOperation = new CassandraOperationImpl();
    private LogHelper logger = LogHelper.getInstance(LearnerStateActor.class.getName());

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Request) {
            logger.debug("LearnerStateActor onReceive called");
            Request actorMessage = (Request) message;
            if (actorMessage.getOperation().equalsIgnoreCase(LearnerStateOperation.GET_COURSE.getValue())) {
                Object obj = actorMessage.getRequest().get(actorMessage.getRequest().keySet().toArray()[0]);
                if (obj instanceof String) {
                    String userId = (String) obj;
                    ActorUtility.DbInfo dbInfo = ActorUtility.dbInfoMap.get(LearnerStateOperation.GET_COURSE.getValue());
                    Response result = cassandraOperation.getRecordById(dbInfo.getKeySpace() , dbInfo.getTableName(),userId);
                    sender().tell(result, self());
                } else {
                    logger.debug("LearnerStateActor message Mis match");
                    ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() , ResponseCode.CLIENT_ERROR.getResponseCode());
                    sender().tell(exception , ActorRef.noSender());
                }
            } else if (actorMessage.getOperation().equalsIgnoreCase(LearnerStateOperation.GET_COURSE_BY_ID.getValue())) {
                logger.info("OP type match" + actorMessage.getRequest().size());
                Object obj = actorMessage.getRequest().get(actorMessage.getRequest().keySet().toArray()[0]);
                if (obj instanceof String) {
                    String courseId = (String) obj;
                    ActorUtility.DbInfo dbInfo = ActorUtility.dbInfoMap.get(LearnerStateOperation.GET_COURSE_BY_ID.getValue());
                    Response result=cassandraOperation.getRecordsByProperty(dbInfo.getKeySpace() , dbInfo.getTableName() , "courseId", courseId);
                    sender().tell(result, getSelf());
                } else {
                    logger.info("LearnerStateActor message Mismatch");
                    ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() , ResponseCode.CLIENT_ERROR.getResponseCode());
                    sender().tell(exception , ActorRef.noSender());
                }
            }else if (actorMessage.getOperation().equalsIgnoreCase(LearnerStateOperation.GET_CONTENT.getValue())) {
            	logger.info("op type get Content");
                Object obj = actorMessage.getRequest().keySet().toArray()[0];
                if (obj instanceof String) {
                	logger.info("obj type String");
                    String userId = (String) obj;
                    List<String> contentList = (List<String>)actorMessage.getRequest().get(userId);
                    ActorUtility.DbInfo dbInfo = ActorUtility.dbInfoMap.get(LearnerStateOperation.GET_CONTENT.getValue());
                    logger.info(contentList.toString());
                    Map<String , Object> requestMap = new HashMap<String , Object>();
                    requestMap.put("userId" , userId);
                    requestMap.put("contentIds" , contentList);
                    Response result = cassandraOperation.getRecordsByProperties(dbInfo.getKeySpace() , dbInfo.getTableName() , requestMap);
                    logger.info(result.toString());
                    sender().tell(result, self());
                } else {
                    logger.info("LearnerStateUpdateActor message Mismatch");
                    ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode() );
                    sender().tell(exception , ActorRef.noSender());
                }
            }
            else{
                logger.info("UNSUPPORTED OPERATION");
                ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidOperationName.getErrorCode() ,ResponseCode.invalidOperationName.getErrorMessage() , ResponseCode.CLIENT_ERROR.getResponseCode());
                sender().tell(exception , ActorRef.noSender());
            }

        }else{
            logger.info("UNSUPPORTED MESSAGE");
            ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode() );
            sender().tell(exception , ActorRef.noSender());
        }

    }

}