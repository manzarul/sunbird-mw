package org.sunbird.common.models.response;

import java.io.Serializable;
import java.util.List;

/**
 * This class will hold course data.
 * @author Manzarul
 *
 */
public class Course implements Serializable{
	private static final long serialVersionUID = -1888295560077745940L;
	private String courseId;
	private String courseName;
	private String description;
	private String enrollDate;
	private String authour;
	private String contnetId;
	private String contentType;
	private String tocUrl;
	private List<Delta> delta;
	/**
	 * @return the courseId
	 */
	public String getCourseId() {
		return courseId;
	}
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the enrollDate
	 */
	public String getEnrollDate() {
		return enrollDate;
	}
	/**
	 * @param enrollDate the enrollDate to set
	 */
	public void setEnrollDate(String enrollDate) {
		this.enrollDate = enrollDate;
	}
	/**
	 * @return the authour
	 */
	public String getAuthour() {
		return authour;
	}
	/**
	 * @param authour the authour to set
	 */
	public void setAuthour(String authour) {
		this.authour = authour;
	}
	/**
	 * @return the contnetId
	 */
	public String getContnetId() {
		return contnetId;
	}
	/**
	 * @param contnetId the contnetId to set
	 */
	public void setContnetId(String contnetId) {
		this.contnetId = contnetId;
	}
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/**
	 * @return the tocUrl
	 */
	public String getTocUrl() {
		return tocUrl;
	}
	/**
	 * @param tocUrl the tocUrl to set
	 */
	public void setTocUrl(String tocUrl) {
		this.tocUrl = tocUrl;
	}
	/**
	 * @return the delta
	 */
	public List<Delta> getDelta() {
		return delta;
	}
	/**
	 * @param delta the delta to set
	 */
	public void setDelta(List<Delta> delta) {
		this.delta = delta;
	}
	
	

}
