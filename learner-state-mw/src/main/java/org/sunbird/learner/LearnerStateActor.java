package org.sunbird.learner;


import akka.actor.UntypedActor;
import org.sunbird.bean.ActorMessage;
/**
 * This actor will provide learner TOC state.
 * @author Manzarul
 *
 */
public class LearnerStateActor extends UntypedActor{
	
	@Override
    public void onReceive(Object message) throws Exception {
		if(message instanceof ActorMessage) {
			//TODO 
		}
	  
    }

}