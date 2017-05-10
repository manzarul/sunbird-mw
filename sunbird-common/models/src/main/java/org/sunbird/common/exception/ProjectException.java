/**
 * 
 */
package org.sunbird.common.exception;

/**
 * @author Manzarul.Haque
 *This exception will be used across all backend code.
 *This will send status code and error message
 */
public class ProjectException extends RuntimeException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private String code;
    private String message;
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
	public ProjectException(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
}
