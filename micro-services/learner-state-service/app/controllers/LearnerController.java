package controllers;

import play.libs.F.Promise;
import play.mvc.Result;
import play.mvc.Results;
import org.sunbird.learner.actors.*;
import org.sunbird.model.Course;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import org.sunbird.bean.*;
import org.sunbird.model.*;
import java.util.*;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * This controller will handler all the request related 
 * to learner state.
 * @author Manzarul
 *
 */
public class LearnerController extends BaseController {
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
		ActorMessage msg = new ActorMessage();
		msg.setOperation(LearnerStateOperation.GET_COURSE);
		Map map = new HashMap<String,Object>();
		map.put("Course 1","user ID 1");
		msg.setData(map);
		Timeout timeout = new Timeout(3, TimeUnit.SECONDS);
        Future<Object> future = Patterns.ask(selection, msg, timeout);
        List<Course> courseList = null;
        try {
        	courseList =(List) Await.result(future, timeout.duration());
            System.out.println(" final retun response=="+courseList.size());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
		return ok("courseList");
		
	}
	
	/**
	 * This method will be called when  user will
	 * enroll for a new course. 
	 * @return Result
	 */
	public Result enrollCourse() {
				Course course= new Course();
		    	course.setCourseId("Course 121234");
		    	course.setCourseName("Course Name121234");
		    	course.setUserId("user ID 1");
		    	course.setEnrolledDate("2017-05-05");
		    	course.setDescription("Teacher training Course Material");
		    	course.setCourseProgressStatus("Not Started");
		    	course.setActive(true);
		    	
				ActorMessage msg = new ActorMessage();
				msg.setOperation(LearnerStateOperation.ADD_COURSE);
				Map map = new HashMap<String,Object>();
				map.put("Course 1",course);
				msg.setData(map);
				Timeout timeout = new Timeout(3, TimeUnit.SECONDS);
		        Future<Object> future = Patterns.ask(selection, msg, timeout);
		        String val = null;
		        try {
		        	val =(String) Await.result(future, timeout.duration());
		            System.out.println(" final retun response=="+val);
		        } catch (Exception e1) {
		            // TODO Auto-generated catch block
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
		
		return ok("success");
	}
   
	/**
	 *This method will update learner current state with last 
	 *store state.
	 * @return Promise<Result>
	 */
	public Promise<Result> updateContentState() {
		return Promise.<Result>pure(Results.ok());
	}
}
