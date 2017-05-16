package org.sunbird.learner.actors;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.request.Request;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.learner.util.ActorUtility;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;

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
                Object obj = actorMessage.getRequest().get(JsonKey.USER_ID);
                if (obj instanceof String) {
                    String userId = (String) obj;
                    ActorUtility.DbInfo dbInfo = ActorUtility.dbInfoMap.get(LearnerStateOperation.GET_COURSE.getValue());
                    Response result = cassandraOperation.getRecordsByProperty(dbInfo.getKeySpace() , dbInfo.getTableName(),JsonKey.USER_ID,userId);
                    sender().tell(result, self());
                } else {
                    logger.debug("LearnerStateActor message Mis match");
                    ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() , ResponseCode.CLIENT_ERROR.getResponseCode());
                    sender().tell(exception , ActorRef.noSender());
                }
            }
            else if (actorMessage.getOperation().equalsIgnoreCase(LearnerStateOperation.GET_CONTENT.getValue())) {
            	logger.info("op type get Content");
                Object obj = actorMessage.getRequest().get(JsonKey.USER_ID);
                if (obj instanceof String) {
                	logger.info("obj type String");
                    String userId = (String) obj;
                    createListForGetContent(actorMessage.getRequest());
                    ActorUtility.DbInfo dbInfo = ActorUtility.dbInfoMap.get(LearnerStateOperation.GET_CONTENT.getValue());
                    Response result = cassandraOperation.getRecordsByProperty(dbInfo.getKeySpace() , dbInfo.getTableName() , JsonKey.ID,(List)actorMessage.getRequest().get(JsonKey.CONTENT_IDS));
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

    private void createListForGetContent(Map<String, Object> req) {

        String userId = (String) req.get(JsonKey.USER_ID);
        List<String> contentList = (List<String>)req.get(JsonKey.CONTENT_IDS);
        List<String> modifiedList = new ArrayList<String>();
        if(!(contentList.isEmpty())){
            for(String contentid : contentList){
                modifiedList.add(contentid+"##"+userId);
            }
        }
        //overriding the content list with modified list
        req.put(JsonKey.CONTENT_IDS , modifiedList);

    }

}