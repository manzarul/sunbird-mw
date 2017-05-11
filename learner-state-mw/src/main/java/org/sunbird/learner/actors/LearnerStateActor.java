package org.sunbird.learner.actors;


import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import java.util.List;
import org.sunbird.bean.ActorMessage;
import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraimpl.CassandraOperationImpl;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.responsecode.HeaderResponseCode;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.model.ContentList;
import org.sunbird.model.CourseList;

/**
 * This actor will handle leaner's state operation like get course , get content etc.
 * @author Manzarul
 */
public class LearnerStateActor extends UntypedAbstractActor {

    private CassandraOperation cassandraOperation = new CassandraOperationImpl();
    private LogHelper logger = LogHelper.getInstance(LearnerStateActor.class.getName());

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
                    ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() , HeaderResponseCode.CLIENT_ERROR.code());
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
                    ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() , HeaderResponseCode.CLIENT_ERROR.code());
                    sender().tell(exception , ActorRef.noSender());
                }
            }else if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.GET_CONTENT.getValue())) {
            	logger.info("op type get Content");
                Object obj = actorMessage.getData().keySet().toArray()[0];
                if (obj instanceof String) {
                	logger.info("obj type String");
                    String userId = (String) obj;
                    List<String> contentList = (List<String>)actorMessage.getData().get(userId);
                    logger.info(contentList.toString());
                    ContentList result = cassandraOperation.getContentState(userId , contentList);
                    logger.info(result.toString());
                    sender().tell(result, self());
                } else {
                    logger.info("LearnerStateUpdateActor message Mismatch");
                    ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage(), HeaderResponseCode.CLIENT_ERROR.code() );
                    sender().tell(exception , ActorRef.noSender());
                }
            }
            else{
                logger.info("UNSUPPORTED OPERATION");
                ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidOperationName.getErrorCode() ,ResponseCode.invalidOperationName.getErrorMessage() , HeaderResponseCode.CLIENT_ERROR.code());
                sender().tell(exception , ActorRef.noSender());
            }

        }else{
            logger.info("UNSUPPORTED MESSAGE");
            ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage(), HeaderResponseCode.CLIENT_ERROR.code() );
            sender().tell(exception , ActorRef.noSender());
        }

    }

}