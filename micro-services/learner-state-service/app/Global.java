import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Logger.ALogger;
import play.core.j.JavaResultExtractor;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.Result;
/**
 * This class will work as a filter.
 * @author Manzarul
 *
 */
public class Global extends GlobalSettings {

	private static final ALogger accessLogger = Logger.of("accesslog");
	private static ObjectMapper mapper = new ObjectMapper();
    
	/**
	 * This method will be called on application start up.
	 * it will be called only time in it's lifecycle.
	 * @param app Application
	 */
	public void onStart(Application app) {
		
	}
    
	/**
	 * This method will be called on each request.
	 * @param request Request
	 * @param actionMethod Method
	 * @return Action
	 */
	@SuppressWarnings("rawtypes")
	public Action onRequest(Request request, Method actionMethod) {
		long startTime = System.currentTimeMillis();
		return new Action.Simple() {
			public Promise<Result> call(Context ctx) throws Throwable {
				Promise<Result> call = delegate.call(ctx);
				call.onRedeem((r) -> {
					try {
						JsonNode requestData = request.body().asJson();
						
					} catch (Exception e) {
						accessLogger.error(e.getMessage());
					}
				});
				return call;
			}
		};
	}
}
