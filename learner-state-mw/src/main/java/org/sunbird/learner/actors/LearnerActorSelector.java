package org.sunbird.learner.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.routing.FromConfig;
import org.apache.log4j.Logger;
import org.sunbird.bean.ActorMessage;
import org.sunbird.bean.LearnerStateOperation;

/**
 * Created by arvind on 8/5/17.
 * Class to select the appropriate actor on the basis of message type .
 */
public class LearnerActorSelector extends UntypedAbstractActor {

    Logger logger = Logger.getLogger(LearnerActorSelector.class.getName());
    ActorRef courseEnrollmentActorRouter;
    ActorRef learnerStateActorRouter;
    ActorRef learnerStateUpdateActorRouter;

    public LearnerActorSelector(){
        courseEnrollmentActorRouter =
                getContext().actorOf(FromConfig.getInstance().props(Props.create(CourseEnrollmentActor.class)),
                        "router1");
        learnerStateActorRouter =
                getContext().actorOf(FromConfig.getInstance().props(Props.create(LearnerStateActor.class)),
                        "router2");
        learnerStateUpdateActorRouter =
                getContext().actorOf(FromConfig.getInstance().props(Props.create(LearnerStateUpdateActor.class)),
                        "router3");
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof ActorMessage) {
            logger.info("onReceive called");
            //TODO check the operation type and handle it.
            ActorMessage actorMessage = (ActorMessage) message;
            if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.ADD_COURSE.getValue())) {

                courseEnrollmentActorRouter.tell(message, self());

            } else if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.GET_COURSE.getValue())) {

                learnerStateActorRouter.tell(message, self());

            } else if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.UPDATE_TOC.getValue())) {

                learnerStateUpdateActorRouter.tell(message, self());

            }


        }

    }
}
