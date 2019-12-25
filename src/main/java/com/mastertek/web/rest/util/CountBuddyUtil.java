package com.mastertek.web.rest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class CountBuddyUtil {
	
	public static Boolean sendFtpFile(String localhost,int port,File file,String username,String password) throws Exception {
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
		
		ftp.login(username,password);
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
	
	 public static File[] getFileList(String path) throws Exception {
	    	File file = new File(path);
			File[] files = file.listFiles(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			        return name.toLowerCase().endsWith(".jpg");
			    }
			});
			Arrays.sort(files, new Comparator<File>() {
			    public int compare(File f1, File f2) {
			        String date1 = f1.getName().split("_")[3].replace(".jpg", "");
			        String date2 = f2.getName().split("_")[3].replace(".jpg", "");
			        return Long.compare(new Long(date1), new Long(date2));
			    }
			});
			return files;
	    }
	 
	 public static String getAccesUrlByWeb(String ftpDirectory,String localWebServerUrl,String path) {
		 String result = path.replace(ftpDirectory, localWebServerUrl);
		 result = result.replace("\\", "/");
		 return result;
	 }
}
