package com.mastertek.service;


import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mastertek.config.ApplicationProperties;

/**
 *
 * @author dom
 */
public class NotifyFtplet extends DefaultFtplet {

	private final Logger log = LoggerFactory.getLogger(NotifyFtplet.class);
	
	String readDirectory;
	
	
	private final ApplicationProperties applicationProperties ;

	public NotifyFtplet(NotifyService notifyService, String readDirectory, ApplicationProperties applicationProperties) {
		super();
		this.readDirectory = readDirectory;
		this.applicationProperties = applicationProperties;
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
	       
			//faceRecognitionService.analyze(uuid, path);
		
			log.info(request.getArgument() +" ftp  finish");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FtpletResult.DEFAULT;
    }

}