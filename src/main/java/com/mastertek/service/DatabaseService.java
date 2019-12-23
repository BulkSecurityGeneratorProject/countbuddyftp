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
}
