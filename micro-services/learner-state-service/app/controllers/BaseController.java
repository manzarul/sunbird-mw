package controllers;
import play.mvc.Controller;
import play.mvc.Http.Request;

import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.response.ResponseParams;
import org.sunbird.common.models.util.ProjectUtil;
import org.sunbird.common.request.ExecutionContext;
import org.sunbird.common.responsecode.ResponseCode;
/**
 * This controller we can use for writing some
 * common method.
 * @author Manzarul
 */
public class BaseController extends Controller {
	/**
	 * 
	 * @param request
	 * @param code
	 * @param headerCode
	 * @return
	 */
	public static Response createFailureResponse(Request request,ResponseCode code , ResponseCode headerCode) {
		Response response = new Response();
		response.setVer(getApiVersion(request));
		response.setId(ExecutionContext.getRequestId());
		response.setTs(ProjectUtil.getFormattedDate());
		response.setResponseCode(headerCode);
		ResponseParams params = new ResponseParams();
		params.setErr(code.getErrorCode());
		params.setErrmsg(code.getErrorMessage());
		params.setStatus(ResponseCode.getHeaderResponseCode(code.getResponseCode()).name());
		response.setParams(params);
		return response;
	}
   
	
	/**
	 * This method will provide api version.
	 * @param request Request
	 * @return String
	 */
  public static String getApiVersion(Request request) {
	     String reqPath = request+"";
	    return reqPath.split("[/]")[1];
  }
}
