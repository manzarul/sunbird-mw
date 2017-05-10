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
import org.sunbird.model.Content;

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
					cassandraOperation.insertContent(content);
					sender().tell("SUCCESS", self());
				} else {
					logger.info("LearnerStateUpdateActor message Mismatch");
					sender().tell("UNSUPPORTED COURSE OBJECT", self());
				}
			}else if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.GET_CONTENT.getValue())) {
				Object obj = actorMessage.getData().get(actorMessage.getData().keySet().toArray()[0]);
				if (obj instanceof String) {

					String contentId = (String) obj;
					cassandraOperation.getContentById(contentId);
					sender().tell("SUCCESS", self());
				} else {
					logger.info("LearnerStateUpdateActor message Mismatch");
					sender().tell("UNSUPPORTED COURSE OBJECT", self());
				}
			}else{
				logger.info("UNSUPPORTED OPERATION");
				RuntimeException exception = new RuntimeException("UNSUPPORTED OPERATION");
				sender().tell(exception , ActorRef.noSender());
			}
		} else{
			logger.info("UNSUPPORTED MESSAGE");
			RuntimeException exception = new RuntimeException("UNSUPPORTED MESSAGE");
			sender().tell(exception , ActorRef.noSender());
		}
	}

}
