package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.sunbird.bean.LearnerStateOperation;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.request.Request;
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
		selection = system.actorSelection("akka.tcp://RemoteMiddlewareSystem@127.0.0.1:8088/user/LearnerActorSelector");
	}
	
	/**
	 * This method will provide list of enrolled courses
	 * for a user. User courses are stored in Cassandra db.
	 * @return Result
	 */
	public Result getEnrolledCourses() {
		String userId= request().getHeader(HeaderParam.X_Session_ID.getName());
		Map<String,Object> map = new HashMap<>();
		map.put("userId", userId);
		Request request = new Request();
		request.setRequest(map);
		request.setOperation(LearnerStateOperation.GET_COURSE.toString());
		request.setRequest(map);
		Timeout timeout = new Timeout(5, TimeUnit.SECONDS);

        Future<Object> future = Patterns.ask(selection, request, timeout);
        try {
        	Object response = Await.result(future, timeout.duration());
        	if(response instanceof Response){
        		return ok(Json.toJson(response));
        	}else{
        		return ok(Json.toJson(response));
        	}
        } catch (Exception e) {
        	logger.error(e);
        	return ok("Failure");
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
		        Request reqObj  = (Request) mapper.RequestMapper.mapRequest(requestData, Request.class);
				Timeout timeout = new Timeout(3, TimeUnit.SECONDS);
		        Future<Object> future = Patterns.ask(selection, reqObj, timeout);
		        String val = null;
		        try {
		        	val =(String) Await.result(future, timeout.duration());
		            System.out.println(" final retun response=="+val);
		        } catch (Exception e1) {
		            e1.printStackTrace();
		        }
				return ok(val);
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
		Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
        Future<Object> future = Patterns.ask(selection, reqObj, timeout);
        try {
        	 Await.result(future, timeout.duration());
            System.out.println(" final retun response==");
        } catch (Exception e) {
        	logger.error(e);
        	return ok("Failure");
        }
		return ok("SUCCESS");

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
		Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
        Future<Object> future = Patterns.ask(selection, reqObj, timeout);
        String val = null;
        try {
        	val =(String) Await.result(future, timeout.duration());
            System.out.println(" final retun response=="+val);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
		return ok(val);
	}
}
