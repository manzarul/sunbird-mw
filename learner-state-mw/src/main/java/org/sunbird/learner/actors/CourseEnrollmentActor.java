/**
 *
 */
package org.sunbird.learner.actors;

import akka.actor.UntypedAbstractActor;
import org.apache.log4j.Logger;
import org.sunbird.bean.ActorMessage;
import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraImpl.CassandraOperationImpl;
import org.sunbird.common.exception.ProjectException;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.model.Course;

/**
 * This class will handle course enrollment
 * details.
 *
 * @author Manzarul
 */
public class CourseEnrollmentActor extends UntypedAbstractActor {
    private Logger logger = Logger.getLogger(CourseEnrollmentActor.class.getName());

    private CassandraOperation cassandraOperation = new CassandraOperationImpl();

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof ActorMessage) {
            logger.debug("CourseEnrollmentActor onReceive called");
            ActorMessage actorMessage = (ActorMessage) message;

            if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.ADD_COURSE.getValue())) {
                Object obj = actorMessage.getData().get(actorMessage.getData().keySet().toArray()[0]);
                if (obj instanceof Course) {
                    Course course = (Course) obj;
                    boolean result = cassandraOperation.insertCourse(course);
                    String response = (result ? "ENROLLMENT SUCCESS":"ENROLLMENT FAILED");
                    sender().tell(response, getSelf());
                } else {
                    logger.info("Course Object not match");
                    ProjectException exception = new ProjectException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() );
                    sender().tell(exception, self());
                }
            } else {
                logger.info("UNSUPPORTED OPERATION");
                ProjectException exception = new ProjectException(ResponseCode.invalidOperationName.getErrorCode() ,ResponseCode.invalidOperationName.getErrorMessage() );
                sender().tell(exception, self());
            }
        } else {
            // Throw exception as message body not as per expected
            logger.info("UNSUPPORTED MESSAGE");
            ProjectException exception = new ProjectException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() );
            sender().tell(exception, self());
        }
    }
}
