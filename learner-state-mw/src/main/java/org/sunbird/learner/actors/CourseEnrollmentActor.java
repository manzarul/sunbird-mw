/**
 *
 */
package org.sunbird.learner.actors;

import akka.actor.UntypedAbstractActor;
import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.learner.util.ActorUtility;
import org.sunbird.common.request.Request;

/**
 * This actor will handle course enrollment operation .
 *
 * @author Manzarul
 */
public class CourseEnrollmentActor extends UntypedAbstractActor {
    private LogHelper logger = LogHelper.getInstance(CourseEnrollmentActor.class.getName());

    private CassandraOperation cassandraOperation = new CassandraOperationImpl();

    /**
     * Receives the actor message and perform the course enrollment operation .
     *
     * @param message
     * @throws Throwable
     */
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Request) {
            logger.info("CourseEnrollmentActor onReceive called");
            Request actorMessage = (Request) message;

            if (actorMessage.getOperation().equalsIgnoreCase(LearnerStateOperation.ADD_COURSE.getValue())) {
                Object obj = actorMessage.getRequest().get(actorMessage.getRequest().keySet().toArray()[0]);
                    ActorUtility.DbInfo dbInfo = ActorUtility.dbInfoMap.get(LearnerStateOperation.ADD_COURSE.getValue());
                    Response result = cassandraOperation.insertRecord(dbInfo.getKeySpace(),dbInfo.getTableName(),actorMessage.getRequest());
                    sender().tell(result, getSelf());
            } else {
                logger.info("UNSUPPORTED OPERATION");
                ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidOperationName.getErrorCode(), ResponseCode.invalidOperationName.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
                sender().tell(exception, self());
            }
        } else {
            // Throw exception as message body
            logger.info("UNSUPPORTED MESSAGE");
            ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode(), ResponseCode.invalidRequestData.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
            sender().tell(exception, self());
        }
    }
}
