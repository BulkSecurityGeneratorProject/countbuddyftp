package com.mastertek.service;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.Instant;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mastertek.domain.FileCatalog;
import com.mastertek.repository.FileCatalogRepository;

@Service
@Transactional
public class NotifyService {

    private final Logger log = LoggerFactory.getLogger(NotifyService.class);

    private final FileCatalogRepository fileCatalogRepository; 
    
    
    
    public NotifyService(FileCatalogRepository fileCatalogRepository) {
		super();
		this.fileCatalogRepository = fileCatalogRepository;
	}



	public void process(String path) {
    	FileCatalog fileCatalog = new FileCatalog();
    	fileCatalog.setDeviceId(getDeviceId(path));
    	fileCatalog.setInsert(Instant.now());
    	fileCatalog.setPath(path);
    	fileCatalog.setProcessed(false);
    	fileCatalogRepository.save(fileCatalog);
    }
    
   
    
    @Async
	public void notifyBackendApp(String path) throws ClientProtocolException, IOException {

		String url = "http://localhost:9095/api/records/processImage?path=";
		String encoded = URLEncoder.encode(path, "UTF-8");
		
		
		HttpGet httpPost = new HttpGet(url+encoded);
		String result = sendRequest(httpPost);
		log.info("notifyFileUpload tamamlandı");
	}
	
	private String sendRequest(HttpUriRequest request) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
	    CloseableHttpResponse response = client.execute(request);
	    if(response.getStatusLine().getStatusCode()!=200 && response.getStatusLine().getStatusCode()!=201) {
	    	String content = EntityUtils.toString(response.getEntity());
	    	log.error("error content:"+content);
	    	throw new RuntimeException(content);
	    }
	    String content = EntityUtils.toString(response.getEntity());
	    client.close();
	    return content;
	}



	public FileCatalogRepository getFileCatalogRepository() {
		return fileCatalogRepository;
	}
	
	public static String getDeviceId(String path) {
		try {
			File file =  new File(path);
			String name = file.getName();
			String[] arg = name.split("_");
			return arg[1];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "";
		}
	}
}
