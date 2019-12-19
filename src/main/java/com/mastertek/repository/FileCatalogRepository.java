package com.mastertek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mastertek.domain.FileCatalog;


/**
 * Spring Data JPA repository for the FileCatalog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileCatalogRepository extends JpaRepository<FileCatalog, Long> {

}
