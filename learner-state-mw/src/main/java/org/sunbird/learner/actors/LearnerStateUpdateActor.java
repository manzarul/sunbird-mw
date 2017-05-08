/**
 * 
 */
package org.sunbird.learner.actors;

import org.sunbird.bean.ActorMessage;
import org.sunbird.cassandra.CassandraOperation;
import org.sunbird.cassandraImpl.CassandraOperationImpl;

import akka.actor.UntypedAbstractActor;

/**
 * This class is responsible to merge the
 * learner state.
 * @author Manzarul
 *
 */
public class LearnerStateUpdateActor extends UntypedAbstractActor{

	private CassandraOperation cassandraOperation = new CassandraOperationImpl();

	@Override
	public void onReceive(Object message) throws Throwable {
		// TODO Auto-generated method stub
		if(message instanceof ActorMessage) {
			//TODO
		}
	}

}
