package com.mastertek.web.rest.util;

import java.io.File;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mastertek.domain.FileCatalog;
import com.mastertek.repository.FileCatalogRepository;
import com.mastertek.service.DatabaseService;

import io.undertow.util.FlexBase64;

public class FileDeleteWorkerThread implements Runnable{
		private final Logger log = LoggerFactory.getLogger(FileDeleteWorkerThread.class);
	
		BigInteger fileCatalogId;
		FileCatalogRepository fileCatalogRepository;
		DatabaseService databaseService;
	    
	    public FileDeleteWorkerThread(DatabaseService databaseService,FileCatalogRepository fileCatalogRepository,BigInteger fileCatalogId){  
	        this.fileCatalogRepository = fileCatalogRepository;
	    	this.fileCatalogId = fileCatalogId;
	    	this.databaseService = databaseService;
	    }  
	   
		public void run() {  
			log.info("file_will_be_deleted.ID:"+fileCatalogId);
	    	
			FileCatalog fileCatalog = fileCatalogRepository.findOne(fileCatalogId.longValue());
			String path = fileCatalog.getPath().replace("\\", "/");
			File file = new File(path);
	    	if(file.exists()) {
	    		boolean result = file.delete();
	    		if(result)
	    			log.info(file.getAbsolutePath() + " deleted");
	    		else
	    			log.info(file.getAbsolutePath() + " cant deleted");
	    	}
	    	fileCatalog.setDeleted(true);
	    	fileCatalogRepository.save(fileCatalog);
	    	log.info("file_deleted.ID:"+fileCatalogId);
		}
}
