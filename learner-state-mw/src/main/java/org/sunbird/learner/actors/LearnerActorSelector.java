package org.sunbird.learner.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import akka.routing.FromConfig;
import akka.util.Timeout;
import org.apache.log4j.Logger;
import org.sunbird.bean.ActorMessage;
import org.sunbird.bean.LearnerStateOperation;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

/**
 * Created by arvind on 8/5/17.
 * Class to select the appropriate actor on the basis of message type .
 */
public class LearnerActorSelector extends UntypedAbstractActor {

    Logger logger = Logger.getLogger(LearnerActorSelector.class.getName());
    ActorRef courseEnrollmentActorRouter;
    ActorRef learnerStateActorRouter;
    ActorRef learnerStateUpdateActorRouter;
    private ExecutionContext ec;

    // constructor to initialize router actor with child actor pool
    public LearnerActorSelector() {
        courseEnrollmentActorRouter =
                getContext().actorOf(FromConfig.getInstance().props(Props.create(CourseEnrollmentActor.class)),
                        "router1");
        learnerStateActorRouter =
                getContext().actorOf(FromConfig.getInstance().props(Props.create(LearnerStateActor.class)),
                        "router2");
        learnerStateUpdateActorRouter =
                getContext().actorOf(FromConfig.getInstance().props(Props.create(LearnerStateUpdateActor.class)),
                        "router3");
        ec = getContext().dispatcher();
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof ActorMessage) {
            logger.info("onReceive called");
            //TODO check the operation type and handle it.
            ActorMessage actorMessage = (ActorMessage) message;
            if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.ADD_COURSE.getValue())) {
            	
            	ActorRef parent = sender();

                Timeout timeout = new Timeout(Duration.create(5, "seconds"));
                Future<Object> future = Patterns.ask(courseEnrollmentActorRouter, message, timeout);

                future.onComplete(new OnComplete<Object>() {
                    @Override
                    public void onComplete(Throwable failure, Object result) {
                        if (failure != null) {
                            //We got a failure, handle it here
                        } else {
                            logger.info("PARENT RESULT IS " + result);
                            // We got a result, do something with it
                            parent.tell("SUCCESS", ActorRef.noSender());
                        }
                    }
                }, ec);

                //courseEnrollmentActorRouter.tell(message, self());

            } else if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.GET_COURSE.getValue())) {

                Timeout timeout = new Timeout(Duration.create(5, "seconds"));
                Future<Object> future = Patterns.ask(learnerStateActorRouter, message, timeout);
                ActorRef parent = sender();
                future.onComplete(new OnComplete<Object>() {
                    @Override
                    public void onComplete(Throwable failure, Object result) {
                        if (failure != null) {
                            //We got a failure, handle it here
                        } else {
                            logger.info("PARENT RESULT IS " + result);
                            // We got a result, do something with it
                            parent.tell(result, ActorRef.noSender());
                        }
                    }
                }, ec);

                //learnerStateActorRouter.tell(message, self());

            } else if (actorMessage.getOperation().getValue().equalsIgnoreCase(LearnerStateOperation.UPDATE_TOC.getValue())) {

                Timeout timeout = new Timeout(Duration.create(5, "seconds"));
                Future<Object> future = Patterns.ask(learnerStateUpdateActorRouter, message, timeout);

                future.onComplete(new OnComplete<Object>() {
                    @Override
                    public void onComplete(Throwable failure, Object result) {
                        if (failure != null) {
                            //We got a failure, handle it here
                        } else {
                            logger.info("PARENT RESULT IS ");
                            // We got a result, do something with it
                            sender().tell("SUCCESS", ActorRef.noSender());
                        }
                    }
                }, ec);

                //learnerStateUpdateActorRouter.tell(message, self());

            }


        }

    }
}
