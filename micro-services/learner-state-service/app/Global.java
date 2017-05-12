import java.lang.reflect.Method;
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
import play.mvc.Results.Redirect;

import org.sunbird.common.models.util.LogHelper;
import org.sunbird.common.models.response.Response;
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
    private static final String GET_METHOD = "GET";
	/**
	 * This method will be called on application start up.
	 * it will be called only time in it's lifecycle.
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
		logger.info("method call start.." +  request + " "+ actionMethod);
		long startTime = System.currentTimeMillis();
		if (request.method().equals(GET_METHOD)) {
		return new Action.Simple() {
			public Promise<Result> call(Context ctx) throws Throwable {
				Promise<Result> call = onDataValidationError(request);
				call.onRedeem((r) -> {
					try {
						JsonNode requestData = request.body().asJson();
						
					} catch (Exception e) {
						logger.error(e);
					}
				});
				return call;
			}
		};
		} else {
			return new Action.Simple() {
				public Promise<Result> call(Context ctx) throws Throwable {
					Promise<Result> call = delegate.call(ctx);
					call.onRedeem((r) -> {
						try {
							JsonNode requestData = request.body().asJson();
							
						} catch (Exception e) {
							logger.error(e);
						}
					});
					return call;
				}
			};
		}
	}
	
	
	 /**
	  *This method will do request data validation for GET method only.
     * As a GET request user must send some key in header.
     */
      public Promise<Result> onDataValidationError(Request request) {
    	  String message = verifyGetRequestData(request);
    	  Response resp = new Response();
    	  resp.setId(message);
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
	 * @return String
	 */
	private String verifyGetRequestData(Request request) {
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
	}
	
}
