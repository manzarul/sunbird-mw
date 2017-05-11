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
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.responsecode.HeaderResponseCode;
import org.sunbird.common.responsecode.ResponseCode;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arvind on 8/5/17.
 * Class to select the appropriate actor on the basis of message type .
 */
public class RequestRouterActor extends UntypedAbstractActor {

    private Logger logger = Logger.getLogger(RequestRouterActor.class.getName());
    private ActorRef courseEnrollmentActorRouter;
    private ActorRef learnerStateActorRouter;
    private ActorRef learnerStateUpdateActorRouter;
    private ExecutionContext ec;
    Map<String, ActorRef> routerMap = new HashMap<>();

    // constructor to initialize router actor with child actor pool
    public RequestRouterActor() {
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
        initializeRouterMap();
    }

    /**
     * Initialize the map with operation as key and corresponding router as value.
     */
    private void initializeRouterMap() {
        routerMap.put(LearnerStateOperation.ADD_COURSE.getValue() ,courseEnrollmentActorRouter);
        routerMap.put(LearnerStateOperation.GET_COURSE.getValue() , learnerStateActorRouter);
        routerMap.put(LearnerStateOperation.UPDATE_TOC.getValue() ,learnerStateUpdateActorRouter);
        routerMap.put(LearnerStateOperation.GET_CONTENT.getValue() ,learnerStateActorRouter);
        routerMap.put(LearnerStateOperation.ADD_CONTENT.getValue() ,learnerStateUpdateActorRouter);
    }


    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof ActorMessage) {
            logger.debug("Actor selector onReceive called");
            ActorMessage actorMessage = (ActorMessage) message;
            ActorRef ref = routerMap.get(actorMessage.getOperation().getValue());
            if(null != ref){
                route(ref , actorMessage);
            }else {
                logger.info("UNSUPPORTED OPERATION TYPE");
                ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidOperationName.getErrorCode(), ResponseCode.invalidOperationName.getErrorMessage(), HeaderResponseCode.CLIENT_ERROR.code());
                sender().tell(exception, ActorRef.noSender());
            }
        } else {
            logger.info("UNSUPPORTED MESSAGE");
            ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode(), ResponseCode.invalidRequestData.getErrorMessage(), HeaderResponseCode.SERVER_ERROR.code());
            sender().tell(exception, ActorRef.noSender());
        }

    }

    /**
     * method will route the message to corresponding router pass into the argument .
     *
     * @param router
     * @param message
     * @return
     */
    private boolean route(ActorRef router, ActorMessage message) {

        Timeout timeout = new Timeout(Duration.create(5, "seconds"));
        Future<Object> future = Patterns.ask(router, message, timeout);
        ActorRef parent = sender();
        future.onComplete(new OnComplete<Object>() {
            @Override
            public void onComplete(Throwable failure, Object result) {
                if (failure != null) {
                    //We got a failure, handle it here
                    ProjectCommonException exception = new ProjectCommonException(ResponseCode.internalError.getErrorCode(), ResponseCode.internalError.getErrorMessage(), HeaderResponseCode.CLIENT_ERROR.code());
                    parent.tell(exception, ActorRef.noSender());
                } else {
                    logger.info("PARENT RESULT IS " + result);
                    // We got a result, handle it
                    parent.tell(result, ActorRef.noSender());
                }
            }
        }, ec);
        return true;
    }
}
