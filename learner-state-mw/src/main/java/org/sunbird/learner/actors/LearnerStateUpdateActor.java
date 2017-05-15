/**
 *
 */
package org.sunbird.learner.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.learner.util.ActorUtility;
import org.sunbird.common.request.Request;

import java.util.Map;

/**
 * This actor will handle learner's state update operation .
 *
 * @author Manzarul
 */
public class LearnerStateUpdateActor extends UntypedAbstractActor {

    private CassandraOperation cassandraOperation = new CassandraOperationImpl();
    private LogHelper logger = LogHelper.getInstance(LearnerStateUpdateActor.class.getName());

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Request) {
            logger.debug("LearnerStateUpdateActor onReceive called");
            Request actorMessage = (Request) message;
            if (actorMessage.getOperation().equalsIgnoreCase(LearnerStateOperation.ADD_CONTENT.getValue())) {
                    ActorUtility.DbInfo dbInfo = ActorUtility.dbInfoMap.get(LearnerStateOperation.ADD_CONTENT.getValue());
                    generateandAppendPrimaryKey(actorMessage.getRequest());
                    Response result = cassandraOperation.insertRecord(dbInfo.getKeySpace(),dbInfo.getTableName() , actorMessage.getRequest());
                    sender().tell(result, self());
            } else {
                logger.info("UNSUPPORTED OPERATION");
                ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidOperationName.getErrorCode(), ResponseCode.invalidOperationName.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
                sender().tell(exception, ActorRef.noSender());
            }
        } else {
            logger.info("UNSUPPORTED MESSAGE");
            ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode(), ResponseCode.invalidRequestData.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
            sender().tell(exception, ActorRef.noSender());
        }
    }

    private void generateandAppendPrimaryKey(Map<String , Object> req){
        String userId = (String)req.get(JsonKey.USER_ID);
        String contentId = (String)req.get(JsonKey.CONTENT_ID);
        String  id = contentId+"##"+userId;
        req.put(JsonKey.ID,id);

    }

}
