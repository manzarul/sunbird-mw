package controllers;
import play.mvc.Controller;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.request.Request;
/**
 * This controller we can use for writing some
 * common method.
 * @author Manzarul
 */
public class BaseController extends Controller {
	
	
	public static Response createFailureResponse(Request request) {
		return null;
	}
   
}
