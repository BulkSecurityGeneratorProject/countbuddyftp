package com.mastertek.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mastertek.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
