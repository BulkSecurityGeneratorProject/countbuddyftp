package com.mastertek.service;


import java.io.IOException;
import java.util.UUID;

import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mastertek.config.ApplicationProperties;
import com.mastertek.domain.FileCatalog;

/**
 *
 * @author dom
 */
public class NotifyFtplet extends DefaultFtplet {

	private final Logger log = LoggerFactory.getLogger(NotifyFtplet.class);
	
	String readDirectory;
	
	private final NotifyService notifyService;
	
	private final ApplicationProperties applicationProperties ;

	public NotifyFtplet(NotifyService notifyService, String readDirectory, ApplicationProperties applicationProperties) {
		super();
		this.readDirectory = readDirectory;
		this.applicationProperties = applicationProperties;
		this.notifyService = notifyService;
	}



	public FtpletResult onUploadEnd(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
		if(!request.getArgument().contains("jpg")) return FtpletResult.DEFAULT;;
		
		String path = "";
		String path2 =session.getFileSystemView().getWorkingDirectory().getPhysicalFile().toString(); 
		
		if(session.getFileSystemView().getWorkingDirectory().getAbsolutePath() == "/")
			path = readDirectory+ "\\"+ request.getArgument();
		else 
			path = readDirectory+session.getFileSystemView().getWorkingDirectory().getAbsolutePath().replaceAll("//", "\\") + "\\"+ request.getArgument();
		
		try {
			String uuid = UUID.randomUUID().toString();
			log.info(request.getArgument() +" ftp  start");
	       
			FileCatalog fileCatalog= notifyService.createFileCatalog(path);
			notifyService.notifyBackendApp(fileCatalog);
		
			log.info(request.getArgument() +" ftp  finish");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FtpletResult.DEFAULT;
    }

}