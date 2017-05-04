/**
 * 
 */
package org.sunbird.learner;

import org.sunbird.bean.ActorMessage;

import akka.actor.UntypedActor;
import org.sunbird.common.*;

/**
 * This class will handle course enrollment
 * details. 
 * @author Manzarul
 *
 */
public class CourseEnrollmentActor  extends UntypedActor{
     private CassandraUtil cassandraUtil;
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof ActorMessage) {
			//TODO check the operation type and handle it.
		}
		
	}

}
