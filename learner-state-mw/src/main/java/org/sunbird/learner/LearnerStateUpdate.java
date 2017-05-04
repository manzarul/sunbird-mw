/**
 * 
 */
package org.sunbird.learner;

import org.sunbird.bean.ActorMessage;

import akka.actor.UntypedActor;

/**
 * This class is responsible to merger the 
 * learner state.
 * @author Manzarul
 *
 */
public class LearnerStateUpdate  extends UntypedActor{

	@Override
	public void onReceive(Object message) throws Throwable {
		// TODO Auto-generated method stub
		if(message instanceof ActorMessage) {
			//TODO check the operation type and handle it.
		}
	}

}
