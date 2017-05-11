package org.sunbird.common.request;

import org.sunbird.common.exception.ProjectCommonException;
import org.sunbird.common.models.util.ProjectUtil;
import org.sunbird.common.responsecode.HeaderResponseCode;
import org.sunbird.common.responsecode.ResponseCode;

/**
 * This call will do validation
 * for all incoming request data.
 * @author Manzarul
 *
 */
public class RequestValidator {
    
	/**
	 * This method will do course enrollment request data
	 * validation. if all mandatory data is coming then it won't do any thing
	 * if any mandatory data is missing then it will throw exception.
	 * @param courseRequestDto CourseRequestDto
	 */
	public static void validateCreateCourse(Request courseRequestDto) {
		if (ProjectUtil.isStringNullOREmpty(courseRequestDto.getOperation())) {
			ProjectCommonException dataException = new ProjectCommonException(ResponseCode.courseNameRequired.getErrorCode(),
					ResponseCode.courseNameRequired.getErrorMessage(),HeaderResponseCode.CLIENT_ERROR.code());
			throw dataException;
		}
	}

}
