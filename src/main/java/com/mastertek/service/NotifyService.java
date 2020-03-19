package com.mastertek.service;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.UUID;

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

import com.mastertek.config.ApplicationProperties;
import com.mastertek.domain.FileCatalog;
import com.mastertek.repository.FileCatalogRepository;
import com.mastertek.web.rest.util.CountBuddyUtil;

@Service
@Transactional
public class NotifyService {

    private final Logger log = LoggerFactory.getLogger(NotifyService.class);

    private final FileCatalogRepository fileCatalogRepository; 
    
    private final ApplicationProperties applicationProperties; 
    
    public NotifyService(FileCatalogRepository fileCatalogRepository,ApplicationProperties applicationProperties) {
		super();
		this.fileCatalogRepository = fileCatalogRepository;
		this.applicationProperties = applicationProperties;
	}

    public FileCatalog createFileCatalog(String path) {
    	String uuid = UUID.randomUUID().toString();
		log.info(uuid+" islemi yapılıyor.");
    	
		FileCatalog fileCatalog = new FileCatalog();
    	fileCatalog.setUuid(uuid);
    	fileCatalog.setDeviceId(CountBuddyUtil.getDeviceId(path));
    	fileCatalog.setInsert(Instant.now());
    	fileCatalog.setPath(path);
    	fileCatalog.setProcessed(false);
    	fileCatalog.setUrl(CountBuddyUtil.getAccesUrlByWeb(applicationProperties.getFtpDirectory(), applicationProperties.getLocalWebServerUrl(), path));
    	fileCatalogRepository.save(fileCatalog);
    	
    	log.info(uuid+" kaydedildi.");
    	return fileCatalog;
    }
    

	
    
   
    
    @Async
	public void notifyBackendApp(FileCatalog fileCatalog) throws ClientProtocolException, IOException {
    	if(applicationProperties.getEnvironment().equals("unittest"))
    		return;
		
    	String url = applicationProperties.getNotifyUrl();
    	String parameters = "?";
    	parameters = parameters+"path="+URLEncoder.encode(fileCatalog.getUrl(), "UTF-8")+"&";
    	parameters = parameters+"uuid="+fileCatalog.getUuid()+"&";
    	parameters = parameters+"fileCatalogId="+fileCatalog.getId();
		
		log.info(fileCatalog.getUuid()+" notify ediliyor.");
		
		//String encoded = URLEncoder.encode(parameters, "UTF-8");
		HttpGet httpPost = new HttpGet(url+parameters);
		String result = sendRequest(httpPost);
		
		log.info(fileCatalog.getUuid()+" notify tamamlandı.");
	}
	
    @Async
	public void notifyBackendApp(String uuid,String path) throws ClientProtocolException, IOException {
    	if(applicationProperties.getEnvironment().equals("unittest"))
    		return;
		
    	String url = applicationProperties.getNotifyUrl();
    	String parameters = "?";
    	String fileUrl = CountBuddyUtil.getAccesUrlByWeb(applicationProperties.getFtpDirectory(), applicationProperties.getLocalWebServerUrl(), path);
    	parameters = parameters+"path="+URLEncoder.encode(fileUrl, "UTF-8")+"&";
    	parameters = parameters+"uuid="+uuid+"&";
    	parameters = parameters+"fileCatalogId="+0;
		
		log.info(uuid+" notify ediliyor.(Hata dolayısı ile fileCatalog iletilmedi)");
		
		//String encoded = URLEncoder.encode(parameters, "UTF-8");
		HttpGet httpPost = new HttpGet(url+parameters);
		String result = sendRequest(httpPost);
		
		log.info(uuid+" notify tamamlandı.(Hata dolayısı ile fileCatalog iletilmedi)");
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
	
	
}
