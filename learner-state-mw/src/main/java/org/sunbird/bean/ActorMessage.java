package org.sunbird.bean;
import java.io.Serializable;
import java.util.Map;

import org.sunbird.bean.LearnerStateOperation;
/**
 * This class will contains learner state
 * operation type and data.
 * @author Manzarul
 */
public class ActorMessage implements Serializable{

	private LearnerStateOperation operation;
	private Map<String,Object> data;
	
	/**
	 * @return LearnerStateOperation
	 */
	public LearnerStateOperation getOperation(){
		return this.operation;
	}
	/**
	 * 
	 * @param operation LearnerStateOperation
	 */
	public void setOperation(LearnerStateOperation operation){
		this.operation = operation;
	}
	/**
	 * 
	 * @return
	 */
	public Map<String,Object> getData() {
		return this.data;
	}
	/**
	 * 
	 * @param data
	 */
	public void setData(Map<String,Object> data) {
		this.data = data;
	}

}
