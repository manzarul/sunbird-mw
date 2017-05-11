package org.sunbird.common.responsecode;


/**
 * 
 * @author Manzarul
 *
 */
public enum ResponseCode {
    unAuthorised(ResponseMessage.Key.UNAUTHORISE_USER, ResponseMessage.Message.UNAUTHORISE_USER),
    invalidUserCredentials(ResponseMessage.Key.INVALID_USER_CREDENTIALS, ResponseMessage.Message.INVALID_USER_CREDENTIALS),
    invalidOperationName(ResponseMessage.Key.INVALID_OPERATION_NAME, ResponseMessage.Message.INVALID_OPERATION_NAME),
    invalidRequestData(ResponseMessage.Key.INVALID_REQUESTED_DATA, ResponseMessage.Message.INVALID_REQUESTED_DATA),
    invalidCustomerId(ResponseMessage.Key.COMSUMER_ID_MISSING_ERROR, ResponseMessage.Message.COMSUMER_ID_MISSING_ERROR),
    customerIdRequired(ResponseMessage.Key.COMSUMER_ID_INVALID_ERROR, ResponseMessage.Message.COMSUMER_ID_INVALID_ERROR),
    deviceIdRequired(ResponseMessage.Key.DEVICE_ID_MISSING_ERROR, ResponseMessage.Message.DEVICE_ID_MISSING_ERROR),
    invalidContentId(ResponseMessage.Key.CONTENT_ID_INVALID_ERROR, ResponseMessage.Message.CONTENT_ID_INVALID_ERROR),
    contentIdRequired(ResponseMessage.Key.CONTENT_ID_MISSING_ERROR, ResponseMessage.Message.CONTENT_ID_MISSING_ERROR),
    apiKeyRequired(ResponseMessage.Key.API_KEY_MISSING_ERROR, ResponseMessage.Message.API_KEY_MISSING_ERROR),
    invalidApiKey(ResponseMessage.Key.API_KEY_INVALID_ERROR, ResponseMessage.Message.API_KEY_INVALID_ERROR),
	internalError(ResponseMessage.Key.INTERNAL_ERROR, ResponseMessage.Message.INTERNAL_ERROR),
	courseNameRequired(ResponseMessage.Key.COURSE_NAME_MISSING, ResponseMessage.Message.COURSE_NAME_MISSING); 
	/**
     * error code contains String value
     */
    private String errorCode;
    /**
     * errorMessage contains proper error message.
     */
    private String errorMessage;

    /**
     * @param errorCode String
     * @param errorMessage String
     */
    private ResponseCode(String errorCode, String errorMessage) {
	this.errorCode = errorCode;
	this.errorMessage = errorMessage;
    }

    /**
     * 
     * @param errorCode
     * @return
     */
    public String getMessage(int errorCode) {
	return "";
    }

    /**
     * @return
     */
    public String getErrorCode() {
	return errorCode;
    }

    /**
     * @param errorCode
     */
    public void setErrorCode(String errorCode) {
	this.errorCode = errorCode;
    }

    /**
     * @return
     */
    public String getErrorMessage() {
	return errorMessage;
    }

    /**
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    /**
     * This method will provide status message based on code
     * 
     * @param code
     * @return String
     */
    public static String getResponseMessage(String code) {
	String value = "";
	ResponseCode responseCodes[] = ResponseCode.values();
	for (ResponseCode actionState : responseCodes) {
	    if (actionState.getErrorCode().equals(code)) {
		value = actionState.getErrorMessage();
	    }
	}
	return value;
    }
}
