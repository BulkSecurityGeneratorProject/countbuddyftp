package com.mastertek.service;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mastertek.config.ApplicationProperties;
import com.mastertek.repository.FileCatalogRepository;
import com.mastertek.web.rest.util.FileDeleteWorkerThread;

@Service
@Transactional
public class FileCatalogService {

    private final Logger log = LoggerFactory.getLogger(FileCatalogService.class);

    private final FileCatalogRepository fileCatalogRepository; 
    
    private final ApplicationProperties applicationProperties; 
    
    private final DatabaseService databaseService; 
    
    public FileCatalogService(FileCatalogRepository fileCatalogRepository,ApplicationProperties applicationProperties,DatabaseService databaseService
    		) {
		super();
		this.fileCatalogRepository = fileCatalogRepository;
		this.applicationProperties = applicationProperties;
		this.databaseService = databaseService;
	
	}
    
    @Scheduled(cron = "0 0 4 * * ?") //This is scheduled to get fired everyday, at 01:00 (am).
    //@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteFiles() throws InterruptedException {
    	
    	log.info("Delete_Files started");
    	
    	List list = databaseService.findFilesForDelete();
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

		
		
		log.info("Delete_Files finished");
    	
    }
    
    
}