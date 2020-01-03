package com.mastertek.service;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {

	@PersistenceContext
    private EntityManager entityManager;

	public BigInteger getRecordCount() {
		Query query ;
    	
    	query = entityManager.createNativeQuery("select count(*) from record");
        List ad = query.getResultList();
        
        
        return (BigInteger)ad.get(0);
	}
	
	   @Transactional
	public void prepareDatabaseForTest() {
		Query query ;
    	
		query = entityManager.createNativeQuery("call local_seperate_ftp_test()");
        query.executeUpdate();
        
        
       
        
	}
	   
	@Transactional
	public List findFilesForDelete() {
		Query query;

		String sql = "select id from file_catalog where  deleted = 0 and device_id in(  \r\n" + 
				"select device_id from device d\r\n" + 
				"join location l on l.id = d.location_id\r\n" + 
				"join floor f on f.id = l.floor_id\r\n" + 
				"join store s on s.id = f.store_id\r\n" + 
				"where s.delete_images = 1\r\n" + 
				") limit 100000";
		query = entityManager.createNativeQuery(sql);
		
		List result = query.getResultList();
		
		return result;

	} 

	
}
