package com.mastertek.service;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
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
import com.mastertek.web.rest.util.CountBuddyUtil;
import com.mastertek.web.rest.util.FtpWorkerThread;



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
    public void performanceTest() throws Exception {
    	
    	
    }
   
    
    @Test
    public void ftpTest() throws Exception {
    	fileCatalogRepository.deleteAll();
    	FileUtils.cleanDirectory(new File(applicationProperties.getFtpDirectory()));
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("Face_733935_19121_1557049797506.jpg").getFile());
    	
    	CountBuddyUtil.sendFtpFile("localhost", applicationProperties.getFtpPort().intValue(), file,applicationProperties.getFtpDefaultUser(),applicationProperties.getFtpDefaultPassord());
    	assertThat(fileCatalogRepository.count()).isEqualTo(1);
    	
    	FileCatalog fileCatalog = fileCatalogRepository.findAll().get(0);
    	assertThat(fileCatalog.getDeviceId()).isEqualTo("733935");
    	assertThat(fileCatalog.getInsert()).isNotNull();
    	assertThat(fileCatalog.getPath()).contains("Face_733935_19121_1557049797506.jpg");
    	assertThat(fileCatalog.getProcessFinishDate()).isNull();
    	assertThat(fileCatalog.getUuid()).isNotNull();
    }	
    
    
    @Test
	public void performanceTest_2() throws Exception {
    	fileCatalogRepository.deleteAll();
    	FileUtils.cleanDirectory(new File(applicationProperties.getFtpDirectory()));
    	ExecutorService executor = Executors.newFixedThreadPool(10);

		String path4= applicationProperties.getPerformanceTestDataPath();

		File[] files = CountBuddyUtil.getFileList(path4);
		long count = 0;
		long count2;
		for (int i = 0; i < 10000; i++) {
			if (i >= files.length)
				continue;

			Runnable worker = new FtpWorkerThread(files[i],applicationProperties.getFtpPort().intValue(),applicationProperties.getFtpDefaultUser(),applicationProperties.getFtpDefaultPassord(),i);
			executor.execute(worker);
			count++;
			System.out.println("i-->" + i + ",count-->" + count);

		}
		
		executor.shutdown();
		executor.awaitTermination(30, TimeUnit.SECONDS);
		
		assertThat(fileCatalogRepository.count()).isEqualTo(1813);
	}
   
}



