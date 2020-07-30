package com.mastertek.service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.ftpserver.ConnectionConfig;
import org.apache.ftpserver.DataConnectionConfiguration;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.impl.DefaultConnectionConfig;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mastertek.config.ApplicationProperties;

@Service
public class CustomFtpServer {
	
	 private final Logger log = LoggerFactory.getLogger(CustomFtpServer.class);
	
	NotifyService notifyService;
	
	ApplicationProperties applicationProperties;
	
	FtpServer server;
	
	public CustomFtpServer(NotifyService notifyService,ApplicationProperties applicationProperties) {
		super();
		// TODO Auto-generated constructor stub
		this.notifyService = notifyService;
		this.applicationProperties = applicationProperties;
		
	}

@PostConstruct
public void init() throws FtpException, IOException {

    FtpServerFactory serverFactory = new FtpServerFactory();
    serverFactory.getFtplets().put("uploadNOtify",  new NotifyFtplet(notifyService,applicationProperties.getFtpDirectory(),applicationProperties));
	
   
	
	File ftpDirectory = new File(applicationProperties.getFtpDirectory());
	if(!ftpDirectory.exists()) {
		ftpDirectory.mkdir();
		log.info(applicationProperties.getFtpDirectory() +" oluşturuldu");
	}
	log.info("parameter_ftp_directory:"+applicationProperties.getFtpDirectory());
	log.info("parameter_local_web_url:"+applicationProperties.getLocalWebServerUrl());
	log.info("parameter_passive_address:"+applicationProperties.getPassiveAddress());
	log.info("parameter_passive_external_address:"+applicationProperties.getPassiveExternalAddress());
	log.info("parameter_server_name:"+applicationProperties.getServerName());
	log.info("parameter_notify_url:"+applicationProperties.getNotifyUrl());
	
	File passwordFile = new File(applicationProperties.getPassowordFile());
	if(!passwordFile.exists()) {
		passwordFile.createNewFile();
		log.info(applicationProperties.getPassowordFile() +" oluşturuldu");
	}
	log.info(applicationProperties.getPassowordFile() +" şifre dosyası kullanılıyor");
	
	
	PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
	userManagerFactory.setFile(passwordFile);
	userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
	UserManager um = userManagerFactory.createUserManager();
	
	if(um.getUserByName(applicationProperties.getFtpDefaultUser())==null) {
	
		BaseUser user = new BaseUser();
		user.setName(applicationProperties.getFtpDefaultUser());
		user.setPassword(applicationProperties.getFtpDefaultPassord());
		user.setHomeDirectory(applicationProperties.getFtpDirectory());
		user.setEnabled(true);
	
		
		um.save(user);
	}
	
	 serverFactory.setUserManager(userManagerFactory.createUserManager());
	 
	 
	
	 
	 DataConnectionConfigurationFactory dataConnectionConfigurationFactory = new DataConnectionConfigurationFactory();
	 //dataConnectionConfigurationFactory.setPassiveAddress(applicationProperties.getPassiveAddress());
	 dataConnectionConfigurationFactory.setPassiveExternalAddress(applicationProperties.getPassiveExternalAddress());
	 dataConnectionConfigurationFactory.setPassivePorts(applicationProperties.getPassivePorts());
	 DataConnectionConfiguration dataConnectionConfig = dataConnectionConfigurationFactory.createDataConnectionConfiguration();

	 ListenerFactory listenerFactory = new ListenerFactory();
	 listenerFactory.setPort(applicationProperties.getFtpPort().intValue());
	 listenerFactory.setDataConnectionConfiguration(dataConnectionConfig);
	 
	 
	  org.apache.ftpserver.listener.Listener listener = listenerFactory.createListener();
	 
	  serverFactory.addListener("default", listener);
	
	  System.out.println("passive external address:"+applicationProperties.getPassiveAddress());
	  
	  server = serverFactory.createServer();
	  ConnectionConfig connectionConfig = new DefaultConnectionConfig(true,500,600,10,3,1000);
		 
	  serverFactory.setConnectionConfig(connectionConfig);
	 
	
	  server.start();
	  System.out.println("ftp server calisti.port:"+applicationProperties.getFtpPort());
}



@PreDestroy
public void stop() {
	server.stop();
}
}
