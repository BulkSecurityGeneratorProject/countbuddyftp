package com.mastertek.service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mastertek.config.ApplicationProperties;
import com.mastertek.domain.FileCatalog;
import com.mastertek.repository.FileCatalogRepository;
import com.mastertek.web.rest.util.CountBuddyUtil;
import com.mastertek.web.rest.util.FileDeleteWorkerThread;

@Service
@Transactional
public class FileCatalogService {

    private final Logger log = LoggerFactory.getLogger(FileCatalogService.class);

    private final FileCatalogRepository fileCatalogRepository; 
    
    private final ApplicationProperties applicationProperties; 
    
    private final DatabaseService databaseService; 

    private final NotifyService notifyService; 
    
    public FileCatalogService(FileCatalogRepository fileCatalogRepository,ApplicationProperties applicationProperties,
    		DatabaseService databaseService,NotifyService notifyService
    		) {
		super();
		this.fileCatalogRepository = fileCatalogRepository;
		this.applicationProperties = applicationProperties;
		this.databaseService = databaseService;
		this.notifyService = notifyService;
	}
    
    @Scheduled(cron = "0 0/15 * * * ?") //This is scheduled to get fired everyday, at 01:00 (am).
    public void deleteFiles() throws InterruptedException {
    	
    	log.info("Delete_Files started");
    	
    	List list = databaseService.findFilesForDelete();
    	log.info("Delete_Files "+list.size() +" silinecek.");
    	ExecutorService executor = Executors.newFixedThreadPool(20);
		long countTemp = 0;
		for (int i = 0; i < list.size(); i++) {
			BigInteger object = (BigInteger)list.get(i);
			Thread.currentThread().sleep(5);
			FileDeleteWorkerThread worker = new FileDeleteWorkerThread(databaseService,fileCatalogRepository,object);
			executor.execute(worker);
			countTemp++;
			System.out.println("Delele files thread -->" + i + ",count-->" + countTemp);

		}
		
		executor.shutdown();
		executor.awaitTermination(30, TimeUnit.SECONDS);

		List list2 = databaseService.findFilesForDelete();
		log.info("Delete_Files finished."+list2.size()+" dosya kaldı");
    	
    }
    
   // @Scheduled(cron = "0 4 * * * ?") //This is scheduled to get fired everyday, at 01:00 (am).
    public void findUnprocessedFilesFromDisk() throws Exception {
    	
    	log.info("findUnprocessedFiles started");
    	
    	Collection<File> fileList = CountBuddyUtil.listFileTree(new File(applicationProperties.getFtpDirectory()));
    	log.info("findUnprocessedFiles "+fileList.size() +" işlenecek.");
    	
    	List deviceIdList = databaseService.getDeviceListofStoreWhichDeletedActivated();
    	
    	Long count=0l;
    	for (Iterator iterator = fileList.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			if(!file.getAbsolutePath().contains("jpg"))
				continue;
			String deviceId = CountBuddyUtil.getDeviceId(file.getAbsolutePath());
			if(deviceIdList.contains(deviceId)) {
			 	FileCatalog fileCatalog= notifyService.createFileCatalog(file.getAbsolutePath());
				notifyService.notifyBackendApp(fileCatalog);
				count++;
			}
		}
   	
		log.info("findUnprocessedFiles tamamlandı. "+count+ " dosya işlendi" );
    	
    }
    
}