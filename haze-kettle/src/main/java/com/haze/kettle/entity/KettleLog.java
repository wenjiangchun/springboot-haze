package com.haze.kettle.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haze.core.jpa.entity.SimpleBaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name="k_log")
@JsonIgnoreProperties(value = {"kettleRepository"})
public class KettleLog extends SimpleBaseEntity<Long> {

	private String objectId;

	private ObjectType objectType;

	private String taskId;

	private String name;

	private String lastDay;

	private Integer errorCount;

	private String errorText;

	private Date startTime;

	private Date endTime;

	private Boolean success;

	private String params;

	private KettleRepository kettleRepository;

	public KettleLog() {
	}

	public KettleLog(String objectId, ObjectType objectType, String name) {
		this.objectId = objectId;
		this.objectType = objectType;
		this.name = name;
	}

	public KettleLog(String objectId, ObjectType objectType, String name, KettleRepository kettleRepository) {
		this.objectId = objectId;
		this.objectType = objectType;
		this.name = name;
		this.kettleRepository = kettleRepository;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastDay() {
		return lastDay;
	}

	public void setLastDay(String lastDay) {
		this.lastDay = lastDay;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@JoinColumn(name = "k_repository_id")
	@ManyToOne
	public KettleRepository getKettleRepository() {
		return kettleRepository;
	}

	public void setKettleRepository(KettleRepository kettleRepository) {
		this.kettleRepository = kettleRepository;
	}

	@Override
	public String toString() {
		return "KettleLog{" +
				"objectId='" + objectId + '\'' +
				", objectType=" + objectType +
				", taskId='" + taskId + '\'' +
				", name='" + name + '\'' +
				", lastDay='" + lastDay + '\'' +
				", errorCount=" + errorCount +
				", errorText='" + errorText + '\'' +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", success=" + success +
				", params=" + params +
				", kettleRepository=" + kettleRepository.getName() +
				'}';
	}

	public static KettleLog createErrorLog(Long repositoryId, String objectId, String errorText) {
		KettleLog kettleLog = new KettleLog();
		kettleLog.setObjectId(objectId);
		kettleLog.setSuccess(false);
		kettleLog.setErrorText(errorText);
		kettleLog.setKettleRepository(new KettleRepository(repositoryId));
		return kettleLog;
	}

	public static String getParams(Map<String, String> parametes) {
		try {
			return new ObjectMapper().writeValueAsString(parametes);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	public enum ObjectType {
		TRANSFORMATION,
		JOB
	}


}
