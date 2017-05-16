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
import org.sunbird.common.request.HeaderParam;
import org.sunbird.common.request.Request;
import org.sunbird.common.request.RequestValidator;
import org.sunbird.common.responsecode.ResponseCode;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;
/**
 * This controller will handler all the request related 
 * to learner state.
 * @author Manzarul
 *
 */
public class LearnerController extends BaseController {
	private LogHelper logger = LogHelper.getInstance(LearnerController.class.getName());
	private static ActorSelection selection=null;
	private static final int Akka_wait_time =3; 
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
	public Promise<Result> getEnrolledCourses() {
		try {
			String userId = request().getHeader(HeaderParam.X_Session_ID.getName());
			Map<String, Object> map = new HashMap<>();
			map.put(JsonKey.USER_ID, userId);
			Request request = new Request();
			request.setRequest(map);
			request.setOperation(LearnerStateOperation.GET_COURSE.getValue());
			request.setRequest(map);
			Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
			request.setRequest_id(ExecutionContext.getRequestId());
			Promise<Result> res = actorResponseHandler(selection,request,timeout,JsonKey.COURSE_LIST);
			return res;
		} catch (Exception e) {
			return Promise.<Result> pure(createCommonExceptionResponse(e));
		}
	}
	
	/**
	 * This method will be called when  user will
	 * enroll for a new course. 
	 * @return Result
	 */
	public Promise<Result> enrollCourse() {
		try {
			JsonNode requestData = request().body().asJson();
			logger.info(" get course request data=" + requestData);
			Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
			RequestValidator.validateCreateCourse(reqObj);
			reqObj.setRequest_id(ExecutionContext.getRequestId());
			reqObj.setOperation(LearnerStateOperation.ADD_COURSE.getValue());
			HashMap<String, Object> innerMap = new HashMap<>();
			innerMap.put(JsonKey.COURSE, reqObj.getRequest());
			innerMap.put(JsonKey.USER_ID, reqObj.getParams().getUid());
			reqObj.setRequest(innerMap);
			Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
			Promise<Result> res = actorResponseHandler(selection,reqObj,timeout,null);
			return res;
		} catch (Exception e) {
			return Promise.<Result> pure(createCommonExceptionResponse(e));
		}
	}
	
	/**
	 *This method will provide list of user content state.
	 *Content refer user activity {started,half completed ,completed} 
	 *against TOC (table of content).
	 * @return Result
	 */
	public Promise<Result> getContentState() {
		try {
			JsonNode requestData = request().body().asJson();
			logger.info(" get course request data=" + requestData);
			Request reqObj = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
			RequestValidator.validateGetData(reqObj);
			reqObj.setRequest_id(ExecutionContext.getRequestId());
			reqObj.setOperation(LearnerStateOperation.GET_CONTENT.getValue());
			HashMap<String, Object> innerMap = new HashMap<>();
			innerMap.put(JsonKey.USER_ID, reqObj.getParams().getUid());
			innerMap.put(JsonKey.CONTENT_IDS, reqObj.getRequest().get(JsonKey.CONTENT_IDS));
			reqObj.setRequest(innerMap);
			Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
			Promise<Result> res = actorResponseHandler(selection, reqObj, timeout, JsonKey.CONTENT_LIST);
			return res;
		} catch (Exception e) {
			return Promise.<Result> pure(createCommonExceptionResponse(e));
		}

	}
   
	/**
	 *This method will update learner current state with last 
	 *store state.
	 * @return Result
	 */
	public Promise<Result> updateContentState() {
		try {
		JsonNode requestData = request().body().asJson();
        logger.info(" get course request data=" + requestData);
        Request reqObj  = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
        RequestValidator.validateUpdateContent(reqObj);
        reqObj.setOperation(LearnerStateOperation.ADD_CONTENT.getValue());
        reqObj.setRequest_id(ExecutionContext.getRequestId());
		HashMap<String, Object> innerMap = new HashMap<>();
		innerMap.put(JsonKey.CONTENTS, reqObj.getRequest().get(JsonKey.CONTENTS));
		innerMap.put(JsonKey.USER_ID, reqObj.getParams().getUid());
		reqObj.setRequest(innerMap);
		Timeout timeout = new Timeout(Akka_wait_time, TimeUnit.SECONDS);
		Promise<Result> res = actorResponseHandler(selection, reqObj, timeout, null);
		return res;
		} catch (Exception e) {
			return Promise.<Result> pure(createCommonExceptionResponse(e));
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
		    return Results.ok(Json.toJson(BaseController.createSuccessResponse(request().path(), (Response) courseResponse)));
			//return ok(Json.toJson(BaseController.createSuccessResponse(request().path(), (Response) courseResponse)));
		} else {
			 ProjectCommonException exception = (ProjectCommonException) response;
			 return Results.ok(Json.toJson(BaseController.createResponseOnException(request().path(), exception)));
			//return ok(Json.toJson(BaseController.createResponseOnException(request().path(), exception)));
		}
	}
	
	/**
	 * Common exception response handler method.
	 * @param e Exception
	 * @return Result
	 */
	private Result createCommonExceptionResponse (Exception e) {
		logger.error(e);
		ProjectCommonException exception = null;
		if(e instanceof ProjectCommonException) {
			exception = (ProjectCommonException) e;
		}else {
		 exception = new ProjectCommonException(ResponseCode.internalError.getErrorCode(),
				ResponseCode.internalError.getErrorMessage(), ResponseCode.SERVER_ERROR.getResponseCode());
		}
		return Results.ok(Json.toJson(BaseController.createResponseOnException(request().path(), exception)));
	}
	
	/**
	 * This method will make a call to Akka actor and return promise.
	 * @param selection ActorSelection
	 * @param request Request
	 * @param timeout Timeout
	 * @param responseKey String
	 * @return Promise<Result>
	 */
	private Promise<Result> actorResponseHandler(ActorSelection selection, Request request, Timeout timeout,String responseKey) {
		Promise<Result> res = Promise.wrap(Patterns.ask(selection, request, timeout))
				.map(new Function<Object, Result>() {
					public Result apply(Object result) {
						if (result instanceof Response) {
							Response response = (Response) result;
							return createCommonResponse(response, responseKey);
						} else if (result instanceof ProjectCommonException) {
							return createCommonExceptionResponse((ProjectCommonException) result);
						} else {
							return createCommonExceptionResponse(new Exception());
						}
					}
				});
		return res;
	}
}
