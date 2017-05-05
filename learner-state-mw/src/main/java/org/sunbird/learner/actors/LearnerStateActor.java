package org.sunbird.learner.actors;


import akka.actor.UntypedActor;
import org.sunbird.bean.ActorMessage;
import org.sunbird.common.CassandraUtil;

/**
 * This actor will provide learner TOC state.
 * @author Manzarul
 *
 */
public class LearnerStateActor extends UntypedActor{

	private CassandraUtil cassandraUtil;
	
	@Override
    public void onReceive(Object message) throws Exception {
		if(message instanceof ActorMessage) {
			//TODO
			// call to cassndra for the learner state
			//cassandraUtil.
		}
	  
    }

}