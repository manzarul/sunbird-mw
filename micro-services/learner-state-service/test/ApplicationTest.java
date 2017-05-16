import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.BaseController;

import org.junit.*;
import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.response.Response;
import org.sunbird.common.models.response.ResponseParams;
import org.sunbird.common.request.HeaderParam;
import org.sunbird.common.responsecode.ResponseCode;

import play.mvc.*;
import play.mvc.Http.RequestBuilder;
import play.test.*;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import static play.test.Helpers.*;
import static org.junit.Assert.*;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest {

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertEquals(2, a);
    }
    
    @Test
    public void checkGetApiVersion() {
    	 String apiPath ="/v1/learner/getenrolledcoures";
    	 String version = BaseController.getApiVersion(apiPath);
    	 assertEquals("v1", version);
    }
    
    @Test
    public void checkCreateRequestParam() {
    	ResponseCode code = ResponseCode.getResponse(ResponseCode.success.getErrorCode());
    	code.setResponseCode(ResponseCode.OK.getResponseCode());
    	ResponseParams params = BaseController.createResponseParamObj(code);
    	assertEquals(ResponseCode.success.name(), params.getStatus());
    }
    
    @Test
	public void checkExceptionResponse() {
		String apiPath ="/v1/learner/getenrolledcoures";
		ProjectCommonException exception = new ProjectCommonException(ResponseCode.courseIdRequiredError.getErrorCode(),
				ResponseCode.courseIdRequiredError.getErrorMessage(), ResponseCode.CLIENT_ERROR.getResponseCode());
		Response response = BaseController.createResponseOnException(apiPath, exception);
		assertEquals( ResponseCode.CLIENT_ERROR,  response.getResponseCode());
	}
    
    @Test
    public void testSuccessResponse() {
    	String apiPath ="/v1/learner/getenrolledcoures";
    	Response response = new Response();
    	response = BaseController.createSuccessResponse(apiPath, response);
    	assertEquals(ResponseCode.OK, response.getResponseCode());
    }
    
    ObjectMapper mapper = new ObjectMapper();

	/*@Test
	public void testgetEnrolledCoursev1() {
			RequestBuilder req = new RequestBuilder().uri("/v1/learner/getenrolledcoures").method(GET);
			req.header(HeaderParam.X_Consumer_ID.getName(), "header value");
			req.header(HeaderParam.X_Session_ID.getName(), "session value");
			Result result = route(req);
			assertEquals(ResponseCode.CLIENT_ERROR, result.status());
			assertEquals("application/json", result.contentType());
			
	}*/
    
    }
