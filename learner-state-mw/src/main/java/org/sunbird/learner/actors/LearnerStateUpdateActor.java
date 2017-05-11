/**
 *
 */
package org.sunbird.learner.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import org.sunbird.bean.ActorMessage;
import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.responsecode.HeaderResponseCode;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.learner.util.ActorUtility;
import org.sunbird.model.Content;
import org.sunbird.common.request.Request;

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
                Object obj = actorMessage.getRequest().get(actorMessage.getRequest().keySet().toArray()[0]);
                if (obj instanceof Content) {
                    Content content = (Content) obj;
                    ActorUtility.DbInfo dbInfo = ActorUtility.dbInfoMap.get(LearnerStateOperation.ADD_CONTENT.getValue());
                    Response result = cassandraOperation.insertRecord(dbInfo.getKeySpace(),dbInfo.getTableName() , null);
                    sender().tell(result, self());
                } else {
                    logger.info("LearnerStateUpdateActor message Mismatch");
                    ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode(), ResponseCode.invalidRequestData.getErrorMessage(), HeaderResponseCode.CLIENT_ERROR.code());
                    sender().tell(exception, ActorRef.noSender());
                }
            } else {
                logger.info("UNSUPPORTED OPERATION");
                ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidOperationName.getErrorCode(), ResponseCode.invalidOperationName.getErrorMessage(), HeaderResponseCode.CLIENT_ERROR.code());
                sender().tell(exception, ActorRef.noSender());
            }
        } else {
            logger.info("UNSUPPORTED MESSAGE");
            ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode(), ResponseCode.invalidRequestData.getErrorMessage(), HeaderResponseCode.CLIENT_ERROR.code());
            sender().tell(exception, ActorRef.noSender());
        }
    }

}
