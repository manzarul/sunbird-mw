package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.models.util.ProjectUtil;
import org.sunbird.common.request.ExecutionContext;
import org.sunbird.common.request.Request;
import org.sunbird.common.request.RequestValidator;
import org.sunbird.common.responsecode.ResponseCode;
import org.sunbird.common.request.HeaderParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import play.libs.Json;
import play.mvc.Result;
import scala.concurrent.Await;
import scala.concurrent.Future;
/**
 * This controller will handler all the request related 
 * to learner state.
 * @author Manzarul
 *
 */
public class LearnerController extends BaseController {
	private LogHelper logger = LogHelper.getInstance(LearnerController.class.getName());
	private static ActorSelection selection=null;
	static{
		ActorSystem system = ActorSystem.create("HelloApplication", ConfigFactory.load()
                .getConfig("HelloConfig"));
		selection = system.actorSelection("akka.tcp://RemoteMiddlewareSystem@127.0.0.1:8088/user/RequestRouterActor");
	}
	
	/**
	 * This method will provide list of enrolled courses
	 * for a user. User courses are stored in Cassandra db.
	 * @return Result
	 */
	public Result getEnrolledCourses() {
		String userId = request().getHeader(HeaderParam.X_Session_ID.getName());
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		Request request = new Request();
		request.setRequest(map);
		request.setOperation(LearnerStateOperation.GET_COURSE.getValue());
		request.setRequest(map);
		Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
		request.setRequest_id(ExecutionContext.getRequestId());
		Future<Object> future = Patterns.ask(selection, request, timeout);
		try {
			Object response = Await.result(future, timeout.duration());
			return createCommonResponse(response,JsonKey.COURSE_LIST);
		} catch (Exception e) {
			return createCommonExceptionResponse(e);
		}
	}
	
	/**
	 * This method will be called when  user will
	 * enroll for a new course. 
	 * @return Result
	 */
	public Result enrollCourse() {
		JsonNode requestData = request().body().asJson();
		logger.info(" get course request data=" + requestData);
		try {
		Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
		RequestValidator.validateCreateCourse(reqObj);
		reqObj.setRequest_id(ExecutionContext.getRequestId());
		reqObj.setOperation(LearnerStateOperation.ADD_COURSE.getValue());
		HashMap<String, Object> innerMap = new HashMap<>();
		innerMap.put(JsonKey.COURSE, reqObj.getRequest());
		innerMap.put(JsonKey.USER_ID, reqObj.getParams().getUid());
		reqObj.setRequest(innerMap);
		Timeout timeout = new Timeout(3, TimeUnit.SECONDS);
		Future<Object> future = Patterns.ask(selection, reqObj, timeout);
		
			Object response  =  Await.result(future, timeout.duration());
			return createCommonResponse(response,null);
		} catch (Exception e) {
			return createCommonExceptionResponse(e);
		}
	}
	
	/**
	 *This method will provide list of user content state.
	 *Content refer user activity {started,half completed ,completed} 
	 *against TOC (table of content).
	 * @return Result
	 */
	public Result getContentState() {
		 JsonNode requestData = request().body().asJson();
        logger.info(" get course request data=" + requestData);
        Request reqObj  = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
        reqObj.setRequest_id(ExecutionContext.getRequestId());
        reqObj.setOperation(LearnerStateOperation.GET_CONTENT.getValue());
        HashMap<String, Object> innerMap = new HashMap<>();
		innerMap.put(JsonKey.USER_ID, reqObj.getParams().getUid());
		innerMap.put(JsonKey.CONTENT_IDS, reqObj.getRequest().get(JsonKey.CONTENT_IDS));
		reqObj.setRequest(innerMap);  
		Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
        Future<Object> future = Patterns.ask(selection, reqObj, timeout);
        try {
        	Object response = Await.result(future, timeout.duration());
        	return createCommonResponse(response,JsonKey.CONTENT_LIST);
		} catch (Exception e) {
			return createCommonExceptionResponse(e);
		}

	}
   
	/**
	 *This method will update learner current state with last 
	 *store state.
	 * @return Result
	 */
	public Result updateContentState() {
		JsonNode requestData = request().body().asJson();
        logger.info(" get course request data=" + requestData);
        Request reqObj  = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
        reqObj.setOperation(LearnerStateOperation.ADD_CONTENT.getValue());
		Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
        Future<Object> future = Patterns.ask(selection, reqObj, timeout);
        try {
        	Response response  =  (Response) Await.result(future, timeout.duration());
        	return createCommonResponse(response,null);
		} catch (Exception e) {
			return createCommonExceptionResponse(e);
		}
	}
	
	/**
	 * This method will create common response for all controller method
	 * @param response Object
	 * @return Result
	 */
	private Result createCommonResponse(Object response,String key) {
		if (response instanceof Response) {
			Response courseResponse = (Response) response;
			if(!ProjectUtil.isStringNullOREmpty(key)){
				Object value = courseResponse.getResult().get(JsonKey.RESPONSE);
				courseResponse.getResult().remove(JsonKey.RESPONSE);
				courseResponse.getResult().put(key, value);
			}
			return ok(Json.toJson(BaseController.createSuccessResponse(request().path(), (Response) courseResponse)));
		} else {
			ProjectCommonException exception = (ProjectCommonException) response;
			return ok(Json.toJson(BaseController.createResponseOnException(request().path(), exception)));
		}
	}
	
	/**
	 * Common exception response handler method.
	 * @param e Exception
	 * @return Result
	 */
	private Result createCommonExceptionResponse (Exception e) {
		logger.error(e);
		ProjectCommonException exception = new ProjectCommonException(ResponseCode.internalError.getErrorCode(),
				ResponseCode.internalError.getErrorMessage(), ResponseCode.SERVER_ERROR.getResponseCode());
		return ok(Json.toJson(BaseController.createResponseOnException(request().path(), exception)));
	}
}
