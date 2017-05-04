package controllers;

import play.libs.F.Promise;
import play.mvc.Result;
import play.mvc.Results;
import org.sunbird.learner.*;
/**
 * This controller will handler all the request related 
 * to learner state.
 * @author Manzarul
 *
 */
public class LearnerController extends BaseController {
	
	/**
	 * This method will provide list of enrolled courses
	 * for a user. User courses are stored in Cassandra db.
	 * @return Result
	 */
	public Result getEnrolledCourses() {
		LearnerStateActor actor = null;
		return ok("success");
	}
	
	/**
	 * This method will be called when  user will
	 * enroll for a new course. 
	 * @return Result
	 */
	public Result enrollCourse() {
		return ok("success ho gaya!");
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
