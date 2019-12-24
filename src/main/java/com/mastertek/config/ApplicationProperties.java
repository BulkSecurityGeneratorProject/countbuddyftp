package com.mastertek.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Ftpcountbuddy.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	String ftpDirectory;
	String passiveAddress;
	String passiveExternalAddress;
	String passivePorts;
	String passowordFile;
	Long ftpPort;
	String ftpDefaultUser;
	String ftpDefaultPassord;
	
	String environment;
	
	String performanceTestDataPath; 
	
	String notifyUrl; 
	
	String localWebServerUrl;
	
	public String getFtpDirectory() {
		return ftpDirectory;
	}
	public void setFtpDirectory(String ftpDirectory) {
		this.ftpDirectory = ftpDirectory;
	}
	public String getPassiveAddress() {
		return passiveAddress;
	}
	public void setPassiveAddress(String passiveAddress) {
		this.passiveAddress = passiveAddress;
	}
	public String getPassiveExternalAddress() {
		return passiveExternalAddress;
	}
	public void setPassiveExternalAddress(String passiveExternalAddress) {
		this.passiveExternalAddress = passiveExternalAddress;
	}
	public String getPassivePorts() {
		return passivePorts;
	}
	public void setPassivePorts(String passivePorts) {
		this.passivePorts = passivePorts;
	}
	public String getPassowordFile() {
		return passowordFile;
	}
	public void setPassowordFile(String passowordFile) {
		this.passowordFile = passowordFile;
	}
	public Long getFtpPort() {
		return ftpPort;
	}
	public void setFtpPort(Long ftpPort) {
		this.ftpPort = ftpPort;
	}
	public String getFtpDefaultUser() {
		return ftpDefaultUser;
	}
	public void setFtpDefaultUser(String ftpDefaultUser) {
		this.ftpDefaultUser = ftpDefaultUser;
	}
	public String getFtpDefaultPassord() {
		return ftpDefaultPassord;
	}
	public void setFtpDefaultPassord(String ftpDefaultPassord) {
		this.ftpDefaultPassord = ftpDefaultPassord;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getPerformanceTestDataPath() {
		return performanceTestDataPath;
	}
	public void setPerformanceTestDataPath(String performanceTestDataPath) {
		this.performanceTestDataPath = performanceTestDataPath;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getLocalWebServerUrl() {
		return localWebServerUrl;
	}
	public void setLocalWebServerUrl(String localWebServerUrl) {
		this.localWebServerUrl = localWebServerUrl;
	}
	
}
