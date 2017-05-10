/**
 * 
 */
package org.sunbird.learner.actors;

import akka.actor.ActorRef;
import org.apache.log4j.Logger;
import org.sunbird.bean.ActorMessage;
import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraImpl.CassandraOperationImpl;

import akka.actor.UntypedAbstractActor;
import org.sunbird.common.exception.ProjectException;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.model.Content;
import org.sunbird.model.ContentList;

import java.util.List;

/**
 * This class is responsible to merge the
 * learner state.
 * @author Manzarul
 *
 */
public class LearnerStateUpdateActor extends UntypedAbstractActor{

	private CassandraOperation cassandraOperation = new CassandraOperationImpl();
	Logger logger = Logger.getLogger(LearnerStateUpdateActor.class.getName());

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof ActorMessage) {
			logger.debug("LearnerStateUpdateActor onReceive called");
			ActorMessage actorMessage = (ActorMessage) message;

			if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.ADD_CONTENT.getValue())) {
				Object obj = actorMessage.getData().get(actorMessage.getData().keySet().toArray()[0]);
				if (obj instanceof Content) {
					Content content = (Content) obj;
					boolean flag =cassandraOperation.insertContent(content);
					String result = flag?"CONTENT ADDED SUCCESSFULLY":"CONTENT ADDITION FAILED";
					sender().tell(result, self());
				} else {
					logger.info("LearnerStateUpdateActor message Mismatch");
					ProjectException exception = new ProjectException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() );
					sender().tell(exception , ActorRef.noSender());
				}
			}else if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.GET_CONTENT.getValue())) {
				Object obj = actorMessage.getData().keySet().toArray()[0];
				if (obj instanceof String) {
					String userId = (String) obj;
					List<String> contentList = (List<String>)actorMessage.getData().get(userId);
					ContentList result = cassandraOperation.getContentState(userId , contentList);
					sender().tell(result, self());
				} else {
					logger.info("LearnerStateUpdateActor message Mismatch");
					ProjectException exception = new ProjectException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() );
					sender().tell(exception , ActorRef.noSender());
				}
			}else{
				logger.info("UNSUPPORTED OPERATION");
				ProjectException exception = new ProjectException(ResponseCode.invalidOperationName.getErrorCode() ,ResponseCode.invalidOperationName.getErrorMessage() );
				sender().tell(exception , ActorRef.noSender());
			}
		} else{
			logger.info("UNSUPPORTED MESSAGE");
			ProjectException exception = new ProjectException(ResponseCode.invalidRequestData.getErrorCode() ,ResponseCode.invalidRequestData.getErrorMessage() );
			sender().tell(exception , ActorRef.noSender());
		}
	}

}
