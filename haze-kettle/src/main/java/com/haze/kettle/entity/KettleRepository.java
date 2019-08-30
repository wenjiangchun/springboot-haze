package com.haze.kettle.entity;

import com.haze.core.jpa.entity.AbstractBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="k_repository")
public class KettleRepository extends AbstractBaseEntity<Long> {

	private String name;

	private String jdbcUrl;

	private String dbType;

	private String userName;

	private String password;

	private String host;

	private String schemaName;

	private String port;

	private String remark;

	private String driverClass;

	private Boolean enabled;

	public KettleRepository() {
	}

	public KettleRepository(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "KettleRepository{" +
				"name='" + name + '\'' +
				", jdbcUrl='" + jdbcUrl + '\'' +
				", dbType='" + dbType + '\'' +
				", userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", host='" + host + '\'' +
				", port='" + port + '\'' +
				", remark='" + remark + '\'' +
				", driverClass='" + driverClass + '\'' +
				'}';
	}
}
