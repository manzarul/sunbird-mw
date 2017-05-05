/**
 * 
 */
package org.sunbird.learner.actors;

import org.sunbird.bean.ActorMessage;

import akka.actor.UntypedActor;
import org.sunbird.common.CassandraUtil;

/**
 * This class is responsible to merger the 
 * learner state.
 * @author Manzarul
 *
 */
public class LearnerStateUpdateActor extends UntypedActor{

	private CassandraUtil cassandraUtil;

	@Override
	public void onReceive(Object message) throws Throwable {
		// TODO Auto-generated method stub
		if(message instanceof ActorMessage) {
			//TODO check the operation type and handle it.
			// call to cassndra for tupdate learner state
			//cassandraUtil.
		}
	}

}
