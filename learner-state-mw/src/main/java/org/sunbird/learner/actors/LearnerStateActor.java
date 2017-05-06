package org.sunbird.learner.actors;


import org.sunbird.bean.ActorMessage;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraImpl.CassandraOperationImpl;

import akka.actor.UntypedAbstractActor;

/**
 * This actor will provide learner TOC state.
 * @author Manzarul
 *
 */
public class LearnerStateActor extends UntypedAbstractActor{

	private CassandraOperation cassandraOperation = new CassandraOperationImpl();

	@Override
    public void onReceive(Object message) throws Exception {
		if(message instanceof ActorMessage) {
			//TODO
			// call to cassndra for the learner state
			//cassandraUtil.



		}
	  
    }

}