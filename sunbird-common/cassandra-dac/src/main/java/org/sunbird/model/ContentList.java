package org.sunbird.model;

import java.io.Serializable;
import java.util.List;

public class ContentList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -643621624407707952L;
	
	private List<Content> contentList;

	/**
	 * @return the contentList
	 */
	public List<Content> getContentList() {
		return contentList;
	}

	/**
	 * @param contentList the contentList to set
	 */
	public void setContentList(List<Content> contentList) {
		this.contentList = contentList;
	}
	
	

}
