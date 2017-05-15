package org.sunbird.learner.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import akka.routing.FromConfig;
import akka.util.Timeout;
import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.request.Request;
import org.sunbird.common.responsecode.ResponseCode;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by arvind on 8/5/17.
 * Class to select the appropriate actor on the basis of message type .
 */
public class RequestRouterActor extends UntypedAbstractActor {

    private LogHelper logger = LogHelper.getInstance(RequestRouterActor.class.getName());
    private ActorRef courseEnrollmentActorRouter;
    private ActorRef learnerStateActorRouter;
    private ActorRef learnerStateUpdateActorRouter;
    private ExecutionContext ec;
    Map<String, ActorRef> routerMap = new HashMap<>();
    private static final int WAIT_TIME_VALUE =5;
    /**
     * constructor to initialize router actor with child actor pool
     */
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
        routerMap.put(LearnerStateOperation.ADD_COURSE.getValue(), courseEnrollmentActorRouter);
        routerMap.put(LearnerStateOperation.GET_COURSE.getValue(), learnerStateActorRouter);
        routerMap.put(LearnerStateOperation.UPDATE_TOC.getValue(), learnerStateUpdateActorRouter);
        routerMap.put(LearnerStateOperation.GET_CONTENT.getValue(), learnerStateActorRouter);
        routerMap.put(LearnerStateOperation.ADD_CONTENT.getValue(), learnerStateUpdateActorRouter);
        routerMap.put(LearnerStateOperation.GET_COURSE_BY_ID.getValue(), learnerStateActorRouter);
    }


    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof Request) {
            logger.debug("Actor selector onReceive called");
            Request actorMessage = (Request) message;
            ActorRef ref = routerMap.get(actorMessage.getOperation());
            if (null != ref) {
                route(ref, actorMessage);
            } else {
                logger.info("UNSUPPORTED OPERATION TYPE");
                ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidOperationName.getErrorCode(), ResponseCode.invalidOperationName.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
                sender().tell(exception, ActorRef.noSender());
            }
        } else {
            logger.info("UNSUPPORTED MESSAGE");
            ProjectCommonException exception = new ProjectCommonException(ResponseCode.invalidRequestData.getErrorCode(), ResponseCode.invalidRequestData.getErrorMessage(), ResponseCode.SERVER_ERROR.getResponseCode());
            sender().tell(exception, ActorRef.noSender());
        }

    }

    /**
     * method will route the message to corresponding router pass into the argument .
     *
     * @param router
     * @param message
     * @return boolean
     */
    private boolean route(ActorRef router, Request message) {

        Timeout timeout = new Timeout(Duration.create(WAIT_TIME_VALUE, TimeUnit.SECONDS));
        Future<Object> future = Patterns.ask(router, message, timeout);
        ActorRef parent = sender();
        future.onComplete(new OnComplete<Object>() {
            @Override
            public void onComplete(Throwable failure, Object result) {
                if (failure != null) {
                    //We got a failure, handle it here
                    ProjectCommonException exception = new ProjectCommonException(ResponseCode.internalError.getErrorCode(), ResponseCode.internalError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
                    parent.tell(exception, ActorRef.noSender());
                    logger.error(failure);
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
