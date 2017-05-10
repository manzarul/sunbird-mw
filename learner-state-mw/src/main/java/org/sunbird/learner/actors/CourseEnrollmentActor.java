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
                    cassandraOperation.insertCourse(course);
                    sender().tell("SUCCESS", getSelf());
                } else {
                    logger.info("Course Object not match");
                    RuntimeException exception = new RuntimeException("UNSUPPORTED COURSE OBJECT");
                    sender().tell(exception, self());
                }
            } else {
                logger.info("UNSUPPORTED OPERATION");
                RuntimeException exception = new RuntimeException("UNSUPPORTED OPERATION");
                sender().tell(exception, self());
            }
        } else {
            // Throw exception as message body not as per expected
            logger.info("UNSUPPORTED MESSAGE");
            RuntimeException exception = new RuntimeException("UNSUPPORTED MESSAGE");
            sender().tell(exception, self());
        }
    }
}
