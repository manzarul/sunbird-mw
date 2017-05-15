import java.lang.reflect.Method;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.LearnerController;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Logger.ALogger;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Results;

import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.request.ExecutionContext;
import org.sunbird.common.request.HeaderParam;
import org.sunbird.common.models.util.ProjectUtil;
import org.sunbird.common.responsecode.ResponseCode;
/**
 * This class will work as a filter.
 * @author Manzarul
 *
 */
public class Global extends GlobalSettings {
	private LogHelper logger = LogHelper.getInstance(Global.class.getName());
	private static ObjectMapper mapper = new ObjectMapper();
	/**
	 * 
	 * @author Manzarul
	 *
	 */
	public enum RequestMethod {
		GET,POST,PUT,DELETE;
	}
	/**
	 * This method will be called on application start up.
	 * it will be called only time in it's life cycle.
	 * @param app Application
	 */
	public void onStart(Application app) {
		logger.info("Server started..");
	}
    
	/**
	 * This method will be called on each request.
	 * @param request Request
	 * @param actionMethod Method
	 * @return Action
	 */
	@SuppressWarnings("rawtypes")
	public Action onRequest(Request request, Method actionMethod) {
		UUID uuid = UUID.randomUUID();
		ExecutionContext.setRequestId(uuid.toString());
		long startTime = System.currentTimeMillis();
		logger.info("method call start.." +  request + " "+ actionMethod + " " + startTime);
		if (request.method().equals(RequestMethod.GET.name())) {
			return verifyGetRequestData(request);
		} else {
			return  verifyPostRequestData(request);
		}
	}
	
	
	 /**
	  *This method will do request data validation for GET method only.
     * As a GET request user must send some key in header.
     */
      public Promise<Result> onDataValidationError(Request request,String errorMessage) {
    	  Response resp = new Response();
    	  resp.setId(errorMessage);
    	  resp.setVer ("v1");
	  return   Promise.<Result>pure(Results.ok(Json.toJson(resp)));
      }
	
	
	 /**
     * This method will be used to send the request header missing error message.
     */
    @Override
    public Promise<Result> onError(Http.RequestHeader request, Throwable t) {
	  return   Promise.<Result>pure(Results.ok());
    }
	
	
	/**
	 * This method will do the get request header mandatory value check
	 * it will check all the mandatory value under header , if any value is
	 * missing then it will send missing key name in response. 
	 * @param request Request
	 * @param method String 
	 * @return String
	 */
	private String verifyRequestData(Request request, String method) {
		  if(RequestMethod.GET.name().equals(method)) {
		if (ProjectUtil.isStringNullOREmpty(request.getHeader(HeaderParam.X_Consumer_ID.getName()))) {
			return ResponseCode.customerIdRequired.getErrorMessage();
		} else if (ProjectUtil.isStringNullOREmpty(request.getHeader(HeaderParam.X_Session_ID.getName()))) {
			return ResponseCode.invalidUserCredentials.getErrorMessage();
		} else if (ProjectUtil.isStringNullOREmpty(request.getHeader(HeaderParam.X_Device_ID.getName()))) {
			return ResponseCode.deviceIdRequired.getErrorMessage();
		} else if (ProjectUtil.isStringNullOREmpty(request.getHeader(HeaderParam.ts.getName()))) {
			return ResponseCode.deviceIdRequired.getErrorMessage();
		}
		return "";
		} else {
			return "";
		}
	}
	
	
	/**
	 * This method will do the get header data verification.
	 * all the mandatory filed should be available. if any mandatory 
	 * filed is missing then it will send proper error response to client.
	 * @param request Request
	 * @return Action
	 */
	private Action verifyGetRequestData(Request request) {
		return new Action.Simple() {
			public Promise<Result> call(Context ctx) throws Throwable {
				String message = verifyRequestData(request,RequestMethod.GET.name());
				Promise<Result> call = null;
				if (!ProjectUtil.isStringNullOREmpty(message)) {
					call = onDataValidationError(request, message);
				} else {
					call = delegate.call(ctx);
				}
				call.onRedeem((r) -> {
					try {

					} catch (Exception e) {
						logger.error(e);
					}
				});
				return call;
			}
		};
	}
	
	/**
	 * This method will do the post request key data validation.
	 * all the mandatory key should be available. if any mandatory 
	 * key is missing then it will send proper error response to client.
	 * @param request Request
	 * @return Action
	 */
	private Action verifyPostRequestData(Request request) {
		return new Action.Simple() {
			public Promise<Result> call(Context ctx) throws Throwable {
				String message = verifyRequestData(request,RequestMethod.POST.name());
				Promise<Result> call = null;
				if (!ProjectUtil.isStringNullOREmpty(message)) {
					call = onDataValidationError(request, message);
				} else {
					call = delegate.call(ctx);
				}
				call.onRedeem((r) -> {
					try {

					} catch (Exception e) {
						logger.error(e);
					}
				});
				return call;
			}
		};
	}

	/**
	 * This method will provide api version.
	 * @param request Request
	 * @return String
	 */
  public static String getApiVersion(Request request) {
	    return request+"".split("[/]")[1];
  }
	
}
