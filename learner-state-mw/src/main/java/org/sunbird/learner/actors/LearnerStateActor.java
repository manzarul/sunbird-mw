package org.sunbird.learner.actors;


import akka.actor.UntypedAbstractActor;

import java.util.List;

import org.apache.log4j.Logger;
import org.sunbird.bean.ActorMessage;
import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraImpl.CassandraOperationImpl;
import org.sunbird.model.Course;

/**
 * This actor will provide learner TOC state.
 *
 * @author Manzarul
 */
public class LearnerStateActor extends UntypedAbstractActor {

    private CassandraOperation cassandraOperation = new CassandraOperationImpl();
    Logger logger = Logger.getLogger(LearnerStateActor.class.getName());

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ActorMessage) {
            logger.info("onReceive called");
            ActorMessage actorMessage = (ActorMessage) message;
            if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.GET_COURSE.getValue())) {
                logger.info("OP type match" + actorMessage.getData().size());
                Object obj = actorMessage.getData().get(actorMessage.getData().keySet().toArray()[0]);
                if (obj instanceof String) {
                    logger.info("Obj match");
                    String userId = (String) obj;
                    //TODO: add course by user id at Cassandra-Operation
                    //cassandraOperation.get
                    List<Course> courseList = cassandraOperation.getUserEnrolledCourse(userId);
                    sender().tell(courseList, self());
                } else {
                    logger.info("Mis match");
                    sender().tell("UNSUPPORTED COURSE OBJECT", self());
                }
            } else if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.GET_COURSE_BY_ID.getValue())) {
                logger.info("OP type match" + actorMessage.getData().size());
                Object obj = actorMessage.getData().get(actorMessage.getData().keySet().toArray()[0]);
                if (obj instanceof String) {
                    logger.info("Obj match");
                    String courseId = (String) obj;
                    cassandraOperation.getCourseById(courseId);
                    sender().tell("SUCCESS", getSelf());
                } else {
                    logger.info("Mis match");
                    sender().tell("UNSUPPORTED COURSE OBJECT", self());
                }
            }

        }

    }

}