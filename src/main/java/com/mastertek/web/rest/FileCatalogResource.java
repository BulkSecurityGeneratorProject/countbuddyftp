package com.mastertek.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.mastertek.domain.FileCatalog;
import com.mastertek.repository.FileCatalogRepository;
import com.mastertek.web.rest.errors.BadRequestAlertException;
import com.mastertek.web.rest.util.HeaderUtil;
import com.mastertek.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing FileCatalog.
 */
@RestController
@RequestMapping("/api")
public class FileCatalogResource {

    private final Logger log = LoggerFactory.getLogger(FileCatalogResource.class);

    private static final String ENTITY_NAME = "fileCatalog";

    private final FileCatalogRepository fileCatalogRepository;

    public FileCatalogResource(FileCatalogRepository fileCatalogRepository) {
        this.fileCatalogRepository = fileCatalogRepository;
    }

    /**
     * POST  /file-catalogs : Create a new fileCatalog.
     *
     * @param fileCatalog the fileCatalog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fileCatalog, or with status 400 (Bad Request) if the fileCatalog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/file-catalogs")
    @Timed
    public ResponseEntity<FileCatalog> createFileCatalog(@Valid @RequestBody FileCatalog fileCatalog) throws URISyntaxException {
        log.debug("REST request to save FileCatalog : {}", fileCatalog);
        if (fileCatalog.getId() != null) {
            throw new BadRequestAlertException("A new fileCatalog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileCatalog result = fileCatalogRepository.save(fileCatalog);
        return ResponseEntity.created(new URI("/api/file-catalogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /file-catalogs : Updates an existing fileCatalog.
     *
     * @param fileCatalog the fileCatalog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fileCatalog,
     * or with status 400 (Bad Request) if the fileCatalog is not valid,
     * or with status 500 (Internal Server Error) if the fileCatalog couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/file-catalogs")
    @Timed
    public ResponseEntity<FileCatalog> updateFileCatalog(@Valid @RequestBody FileCatalog fileCatalog) throws URISyntaxException {
        log.debug("REST request to update FileCatalog : {}", fileCatalog);
        if (fileCatalog.getId() == null) {
            return createFileCatalog(fileCatalog);
        }
        FileCatalog result = fileCatalogRepository.save(fileCatalog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fileCatalog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /file-catalogs : get all the fileCatalogs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fileCatalogs in body
     */
    @GetMapping("/file-catalogs")
    @Timed
    public ResponseEntity<List<FileCatalog>> getAllFileCatalogs(Pageable pageable) {
        log.debug("REST request to get a page of FileCatalogs");
        Page<FileCatalog> page = fileCatalogRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/file-catalogs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /file-catalogs/:id : get the "id" fileCatalog.
     *
     * @param id the id of the fileCatalog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fileCatalog, or with status 404 (Not Found)
     */
    @GetMapping("/file-catalogs/{id}")
    @Timed
    public ResponseEntity<FileCatalog> getFileCatalog(@PathVariable Long id) {
        log.debug("REST request to get FileCatalog : {}", id);
        FileCatalog fileCatalog = fileCatalogRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fileCatalog));
    }

    /**
     * DELETE  /file-catalogs/:id : delete the "id" fileCatalog.
     *
     * @param id the id of the fileCatalog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/file-catalogs/{id}")
    @Timed
    public ResponseEntity<Void> deleteFileCatalog(@PathVariable Long id) {
        log.debug("REST request to delete FileCatalog : {}", id);
        fileCatalogRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}