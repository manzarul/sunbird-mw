/**
 * 
 */
package org.sunbird.learner.actors;

import org.sunbird.bean.ActorMessage;

import akka.actor.UntypedActor;
import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraImpl.CassandraOperationImpl;
import org.sunbird.common.*;
import org.sunbird.model.Course;
import org.apache.log4j.*;

/**
 * This class will handle course enrollment
 * details. 
 * @author Manzarul
 *
 */
public class CourseEnrollmentActor  extends UntypedActor{
	 Logger logger = Logger.getLogger(CourseEnrollmentActor.class.getName());

	private CassandraOperation cassandraOperation = new CassandraOperationImpl();
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof ActorMessage) {
			logger.info("onReceive called");
			//TODO check the operation type and handle it.
			ActorMessage actorMessage = (ActorMessage)message;

			if(actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.ADD_COURSE.getValue())){
				logger.info("OP type match"+actorMessage.getData().size());
				Object obj = actorMessage.getData().get("Course 1");
				//Object obj = actorMessage.getData().keySet().toArray()[0];
				if(obj instanceof Course) {
					logger.info("Obj match");
					Course course = (Course) obj;
					logger.info(course.toString());
					cassandraOperation.insertCourse(course);
					sender().tell("SUCCESS", getSelf());
				}else{
					logger.info("Mis match");
					sender().tell("UNSUPPORTED COURSE OBJECT",self());
				}
			}
		}
	}
}
