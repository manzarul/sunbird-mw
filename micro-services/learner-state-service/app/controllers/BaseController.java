package controllers;
import play.mvc.Controller;
import play.mvc.Http.Request;

import org.sunbird.common.exception.ProjectCommonException;
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
	 * This method will create failure response
	 * @param request Request
	 * @param code ResponseCode
	 * @param headerCode ResponseCode
	 * @return Response
	 */
	public static Response createFailureResponse(Request request,ResponseCode code , ResponseCode headerCode) {
		Response response = new Response();
		response.setVer(getApiVersion(request));
		response.setId(ExecutionContext.getRequestId());
		response.setTs(ProjectUtil.getFormattedDate());
		response.setResponseCode(headerCode);
		response.setParams(createResponseParamObj(code));
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
  
    /**
     * This method will create response param 
     * @param code ResponseCode
     * @return
     */
	public static ResponseParams createResponseParamObj(ResponseCode code) {
		ResponseParams params = new ResponseParams();
		params.setErr(code.getErrorCode());
		params.setErrmsg(code.getErrorMessage());
		params.setStatus(ResponseCode.getHeaderResponseCode(code.getResponseCode()).name());
		return params;
	}
	
	/**
	 * This method will create data for success response.
	 * @param response
	 * @return
	 */
	public static Response createSuccessResponse(String request, Response response) {
		response.setVer(getApiVersion(request));
		response.setId(ExecutionContext.getRequestId());
		response.setTs(ProjectUtil.getFormattedDate());
		ResponseCode code = ResponseCode.getResponse(ResponseCode.success.getErrorCode());
		response.setParams(createResponseParamObj(code));
		return response;
	}
	
	/**
	 * This method will provide api version.
	 * @param request String
	 * @return String
	 */
  public static String getApiVersion(String request) {
	  System.out.println(request);
	    return request.split("[/]")[0];
  }
  
  
  /**
   * This method will handle response in case of exception
   * @param request String
   * @param exception ProjectCommonException
   * @return Response
   */
	public static Response createResponseOnException(String request, ProjectCommonException exception) {
		Response response = new Response();
		response.setVer(getApiVersion(request));
		response.setId(ExecutionContext.getRequestId());
		response.setTs(ProjectUtil.getFormattedDate());
		response.setResponseCode(ResponseCode.getHeaderResponseCode(exception.getResponseCode()));
		ResponseCode code = ResponseCode.getResponse(exception.getCode());
		response.setParams(createResponseParamObj(code));
		return response;
	}

  
}
