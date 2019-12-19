package com.mastertek.service;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mastertek.FtpcountbuddyApp;
import com.mastertek.config.ApplicationProperties;
import com.mastertek.domain.FileCatalog;
import com.mastertek.repository.FileCatalogRepository;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = FtpcountbuddyApp.class)
@Transactional
public class FtpTest {

	@Autowired
    private ApplicationProperties applicationProperties;
	
	@Autowired
    private FileCatalogRepository fileCatalogRepository;
	
	
    @Before
    public void setup() {
    
    }	
    @Test
    public void performanceTest_2() throws Exception {
        System.out.println("sdfsdf");
    }	
    
    @Test
    public void performanceTest() throws Exception {
    	
    	
    }
   
    
    @Test
    public void ftpTest() throws Exception {
    	FileUtils.cleanDirectory(new File(applicationProperties.getFtpDirectory()));
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("Face_733935_19121_1557049797506.jpg").getFile());
    	
    	sendFtpFile("localhost", applicationProperties.getFtpPort().intValue(), file);
    	assertThat(fileCatalogRepository.count()).isEqualTo(1);
    	
    	FileCatalog fileCatalog = fileCatalogRepository.findAll().get(0);
    	assertThat(fileCatalog.getDeviceId()).isEqualTo("733935");
    	assertThat(fileCatalog.getInsert()).isNotNull();
    	assertThat(fileCatalog.getPath()).contains("Face_733935_19121_1557049797506.jpg");
    	assertThat(fileCatalog.getProcessFinishDate()).isNull();
    	assertThat(fileCatalog.getUuid()).isNotNull();
    }	
    
    public Boolean sendFtpFile(String localhost,int port,File file) throws Exception {
    	FTPClient ftp = null;
    	ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		int reply;
		ftp.connect("localhost",port);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			throw new Exception("Exception in connecting to FTP Server");
		}
		
		ftp.login(applicationProperties.getFtpDefaultUser(),applicationProperties.getFtpDefaultPassord());
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.enterLocalPassiveMode();
		
		
    	
		InputStream input = new FileInputStream(file);
		Boolean result =ftp.storeFile(file.getName(), input);
		
		if (ftp.isConnected()) {
			try {
				ftp.logout();
				ftp.disconnect();
			} catch (IOException f) {
				// do nothing as file is already saved to server
			}
		}
		return result;
		
    }
    
    
}
