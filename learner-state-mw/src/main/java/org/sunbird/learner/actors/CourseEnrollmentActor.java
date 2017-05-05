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

/**
 * This class will handle course enrollment
 * details. 
 * @author Manzarul
 *
 */
public class CourseEnrollmentActor  extends UntypedActor{

	private CassandraOperation cassandraOperation = new CassandraOperationImpl();
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof ActorMessage) {
			//TODO check the operation type and handle it.
			ActorMessage actorMessage = (ActorMessage)message;

			if(actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.ADD_COURSE.getValue())){
				Object obj = actorMessage.getData().keySet().toArray()[0];
				if(obj instanceof Course) {
					Course course = (Course) obj;
					cassandraOperation.insertCourse(course);
					sender().tell("SUCCESS", self());
				}else{
					sender().tell("UNSUPPORTED COURSE OBJECT",self());
				}
			}
		}
	}
}
