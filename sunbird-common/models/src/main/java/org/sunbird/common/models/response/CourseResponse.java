package org.sunbird.common.models.response;

import java.io.Serializable;

/**
 * This class will contains complete course 
 * response object.
 * @author Manzarul
 *
 */
public class CourseResponse implements Serializable{
	private static final long serialVersionUID = 1227364694267678889L;
	private String responseCode;
	private String id;
	private String ts;
	private Params params;
	private CourseList result;
	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}
	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the ts
	 */
	public String getTs() {
		return ts;
	}
	/**
	 * @param ts the ts to set
	 */
	public void setTs(String ts) {
		this.ts = ts;
	}
	/**
	 * @return the params
	 */
	public Params getParams() {
		return params;
	}
	/**
	 * @param params the params to set
	 */
	public void setParams(Params params) {
		this.params = params;
	}
	/**
	 * @return the result
	 */
	public CourseList getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(CourseList result) {
		this.result = result;
	}
	
	

}
