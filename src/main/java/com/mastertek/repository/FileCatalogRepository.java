package com.mastertek.repository;

import com.mastertek.domain.FileCatalog;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FileCatalog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileCatalogRepository extends JpaRepository<FileCatalog, Long> {

}
