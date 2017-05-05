package org.sunbird.model;

import java.util.Map;

import com.datastax.driver.mapping.annotations.Table;

@Table(name = "course",keyspace="cassandraKeySpace")
public class Course {
	
	private String courseId;
	private String courseName;
	private String userId;
	private String enrolledDate;
	private String description;
	private String tocUrl;
	private Map<String,String> deltaMap;
	private String courseProgressStatus;				//(notstarted, started,completed)
	private boolean active;
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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the enrolledDate
	 */
	public String getEnrolledDate() {
		return enrolledDate;
	}
	/**
	 * @param enrolledDate the enrolledDate to set
	 */
	public void setEnrolledDate(String enrolledDate) {
		this.enrolledDate = enrolledDate;
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
	 * @return the deltaMap
	 */
	public Map<String, String> getDeltaMap() {
		return deltaMap;
	}
	/**
	 * @param deltaMap the deltaMap to set
	 */
	public void setDeltaMap(Map<String, String> deltaMap) {
		this.deltaMap = deltaMap;
	}
	/**
	 * @return the courseProgressStatus
	 */
	public String getCourseProgressStatus() {
		return courseProgressStatus;
	}
	/**
	 * @param courseProgressStatus the courseProgressStatus to set
	 */
	public void setCourseProgressStatus(String courseProgressStatus) {
		this.courseProgressStatus = courseProgressStatus;
	}
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Course [courseId=" + courseId + ", courseName=" + courseName + ", userId=" + userId + ", enrolledDate="
				+ enrolledDate + ", description=" + description + ", tocUrl=" + tocUrl 
				+ ", courseProgressStatus=" + courseProgressStatus + ", active=" + active + "]";
	}
	
	

}
