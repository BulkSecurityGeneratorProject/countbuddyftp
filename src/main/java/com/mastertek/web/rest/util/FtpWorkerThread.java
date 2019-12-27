package com.mastertek.web.rest.util;

import java.io.File;
import java.util.UUID;

public class FtpWorkerThread implements Runnable{
		File file;
	    int count= 0;
	    int port=0;
	    String username;
	    String password;
	    String host;
	    
	    public FtpWorkerThread(File file,String host,int port,String username,String password,int count){  
	        this.file = file;
	        this.count = count;
	        this.port = port;
	        this.username=username;
	        this.password = password;
	        this.host = host;
	    }  
	   
		public void run() {  
	    	 long start = System.currentTimeMillis(); 
				try {
					CountBuddyUtil.sendFtpFile(host, port, file, username, password);

					System.out.println(Thread.currentThread().getName()+" (End) count="+count +" " + (System.currentTimeMillis()-start));//prints thread name  
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     
	}
}
