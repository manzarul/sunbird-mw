package org.sunbird.learner.actors;


import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;

import java.util.List;

import org.apache.log4j.Logger;
import org.sunbird.bean.ActorMessage;
import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraImpl.CassandraOperationImpl;
import org.sunbird.common.exception.ProjectException;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.model.ContentList;
import org.sunbird.model.Course;
import org.sunbird.model.CourseList;

/**
 * This actor will provide learner TOC state.
 *
 * @author Manzarul
 */
public class LearnerStateActor extends UntypedAbstractActor {

    private CassandraOperation cassandraOperation = new CassandraOperationImpl();
    private Logger logger = Logger.getLogger(LearnerStateActor.class.getName());

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ActorMessage) {
            logger.debug("LearnerStateActor onReceive called");
            ActorMessage actorMessage = (ActorMessage) message;
            if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.GET_COURSE.getValue())) {
                Object obj = actorMessage.getData().get(actorMessage.getData().keySet().toArray()[0]);
                if (obj instanceof String) {
                    String userId = (String) obj;
                    CourseList courseList = cassandraOperation.getUserEnrolledCourse(userId);
                    sender().tell(courseList, self());
                } else {
                    logger.debug("LearnerStateActor message Mis match");
                    ProjectException exception = new ProjectException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() );
                    sender().tell(exception , ActorRef.noSender());
                }
            } else if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.GET_COURSE_BY_ID.getValue())) {
                logger.info("OP type match" + actorMessage.getData().size());
                Object obj = actorMessage.getData().get(actorMessage.getData().keySet().toArray()[0]);
                if (obj instanceof String) {
                    String courseId = (String) obj;
                    cassandraOperation.getCourseById(courseId);
                    sender().tell("SUCCESS", getSelf());
                } else {
                    logger.info("LearnerStateActor message Mismatch");
                    ProjectException exception = new ProjectException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() );
                    sender().tell(exception , ActorRef.noSender());
                }
            }else if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.GET_CONTENT.getValue())) {
            	logger.info("op type get Content");
                Object obj = actorMessage.getData().keySet().toArray()[0];
                if (obj instanceof String) {
                	logger.info("obj type String");
                    String userId = (String) obj;
                    List<String> contentList = (List<String>)actorMessage.getData().get(userId);
                    logger.info(contentList);
                    ContentList result = cassandraOperation.getContentState(userId , contentList);
                    logger.info(result);
                    sender().tell(result, self());
                } else {
                    logger.info("LearnerStateUpdateActor message Mismatch");
                    ProjectException exception = new ProjectException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() );
                    sender().tell(exception , ActorRef.noSender());
                }
            }
            else{
                logger.info("UNSUPPORTED OPERATION");
                ProjectException exception = new ProjectException(ResponseCode.invalidOperationName.getErrorCode() ,ResponseCode.invalidOperationName.getErrorMessage() );
                sender().tell(exception , ActorRef.noSender());
            }

        }else{
            logger.info("UNSUPPORTED MESSAGE");
            ProjectException exception = new ProjectException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() );
            sender().tell(exception , ActorRef.noSender());
        }

    }

}