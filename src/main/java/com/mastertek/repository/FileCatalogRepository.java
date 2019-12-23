package com.mastertek.repository;

import com.mastertek.domain.FileCatalog;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the FileCatalog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileCatalogRepository extends JpaRepository<FileCatalog, Long> {

	
}
