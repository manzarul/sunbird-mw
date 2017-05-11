/**
 * 
 */
package org.sunbird.common.responsecode;

/**
 * This enum will contains all those response code
 * which will be used for response header
 * @author Manzarul
 *
 */
public enum HeaderResponseCode {

	OK(200), CLIENT_ERROR(400), SERVER_ERROR(500), RESOURCE_NOT_FOUND(404);

	private int code;

	private HeaderResponseCode(int code) {
		this.code = code;
	}

	public int code() {
		return this.code;
	}
   
	/**
	 * This method will take header response code as int value and 
	 * it provide matched enum value, if code is not matched or exception occurs
	 * then it will provide SERVER_ERROR
	 * @param code int
	 * @return HeaderResponseCode
	 */
	public static HeaderResponseCode getHeaderResponseCode(int code) {
		if (code > 0) {
			try {
				HeaderResponseCode[] arr = HeaderResponseCode.values();
				if (null != arr) {
					for (HeaderResponseCode rc : arr) {
						if (rc.code() == code)
							return rc;
					}
				}
			} catch (Exception e) {
				return HeaderResponseCode.SERVER_ERROR;
			}
		}
		return HeaderResponseCode.SERVER_ERROR;
	}
}
