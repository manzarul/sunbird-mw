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
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.responsecode.HeaderResponseCode;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.model.Content;

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
        if (message instanceof ActorMessage) {
            logger.debug("LearnerStateUpdateActor onReceive called");
            ActorMessage actorMessage = (ActorMessage) message;

            if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.ADD_CONTENT.getValue())) {
                Object obj = actorMessage.getData().get(actorMessage.getData().keySet().toArray()[0]);
                if (obj instanceof Content) {
                    Content content = (Content) obj;
                    boolean flag = cassandraOperation.insertContent(content);
                    String result = flag ? "CONTENT ADDED SUCCESSFULLY" : "CONTENT ADDITION FAILED";
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
