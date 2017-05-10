package org.sunbird.model;

import java.io.Serializable;

import com.datastax.driver.mapping.annotations.Table;

@Table(name = "content",keyspace="cassandraKeySpace")
public class Content implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7630447305183779420L;
	
	private String contentId;
	private String viewCount;
	private String lastAccessTime;
	private String completedCount;
	private String progressstatus;
	private String userId;
	private String courseId;
	private String lastUpdatedTime;
	private String deviceId;
	private String viewPosition;
	
	
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
	 * @return the lastUpdatedTime
	 */
	public String getLastUpdatedTime() {
		return lastUpdatedTime;
	}
	/**
	 * @param lastUpdatedTime the lastUpdatedTime to set
	 */
	public void setLastUpdatedTime(String lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}
	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}
	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * @return the viewPosition
	 */
	public String getViewPosition() {
		return viewPosition;
	}
	/**
	 * @param viewPosition the viewPosition to set
	 */
	public void setViewPosition(String viewPosition) {
		this.viewPosition = viewPosition;
	}
	/**
	 * @return the contentId
	 */
	public String getContentId() {
		return contentId;
	}
	/**
	 * @param contentId the contentId to set
	 */
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	/**
	 * @return the viewCount
	 */
	public String getViewCount() {
		return viewCount;
	}
	/**
	 * @param viewCount the viewCount to set
	 */
	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}
	/**
	 * @return the lastAccessTime
	 */
	public String getLastAccessTime() {
		return lastAccessTime;
	}
	/**
	 * @param lastAccessTime the lastAccessTime to set
	 */
	public void setLastAccessTime(String lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}
	/**
	 * @return the completedCount
	 */
	public String getCompletedCount() {
		return completedCount;
	}
	/**
	 * @param completedCount the completedCount to set
	 */
	public void setCompletedCount(String completedCount) {
		this.completedCount = completedCount;
	}
	/**
	 * @return the progressstatus
	 */
	public String getProgressstatus() {
		return progressstatus;
	}
	/**
	 * @param progressstatus the progressstatus to set
	 */
	public void setProgressstatus(String progressstatus) {
		this.progressstatus = progressstatus;
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
	
	

}
