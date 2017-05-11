/**
 * 
 */
package org.sunbird.common.models.util;

/**
 * 
 * This class will contains all the common utility 
 * methods.
 * @author Manzarul
 *
 */
public class ProjectUtil {
	
	/**
	 * This method will check incoming value is null or empty
	 * it will do empty check by doing trim method. in case of 
	 * null or empty it will return true else false.
	 * @param value
	 * @return
	 */
	public static boolean isStringNullOREmpty(String value) {
		if (value == null || "".equals(value.trim())) {
			return true;
		}
		return false;
	}

}
