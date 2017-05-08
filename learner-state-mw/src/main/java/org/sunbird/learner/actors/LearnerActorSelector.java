package org.sunbird.learner.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import org.apache.log4j.Logger;
import org.sunbird.bean.ActorMessage;
import org.sunbird.bean.LearnerStateOperation;

/**
 * Created by arvind on 8/5/17.
 * Class to select the appropriate actor on the basis of message type .
 */
public class LearnerActorSelector extends UntypedAbstractActor {

    Logger logger = Logger.getLogger(LearnerActorSelector.class.getName());

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof ActorMessage) {
            logger.info("onReceive called");
            //TODO check the operation type and handle it.
            ActorMessage actorMessage = (ActorMessage) message;
            if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.ADD_COURSE.getValue())) {

                ActorRef courseEnrollmentActorRef = getContext().actorOf(Props.create(CourseEnrollmentActor.class),
                        "CourseEnrollmentActor");
                logger.info("actor path :"+courseEnrollmentActorRef.path()+" and reference is "+courseEnrollmentActorRef.toString());
                courseEnrollmentActorRef.tell(message, self());

            } else if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.GET_COURSE.getValue())) {

                ActorRef learnerActorSelectorRef = getContext().actorOf(Props.create(LearnerActorSelector.class),
                        "LearnerActorSelector");
                learnerActorSelectorRef.tell(message, self());

            } else if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.UPDATE_TOC.getValue())) {

                ActorRef learnerStateUpdateActorRef = getContext().actorOf(Props.create(LearnerStateUpdateActor.class),
                        "LearnerStateUpdateActor");

                learnerStateUpdateActorRef.tell(message, self());

            }


        }

    }
}
