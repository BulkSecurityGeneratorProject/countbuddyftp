package com.mastertek.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FileCatalog.
 */
@Entity
@Table(name = "file_catalog")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FileCatalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "processed")
    private Boolean processed;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "jhi_insert")
    private Instant insert;

    @Column(name = "process_finish_date")
    private Instant processFinishDate;

    @Column(name = "device_id")
    private String deviceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public FileCatalog path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean isProcessed() {
        return processed;
    }

    public FileCatalog processed(Boolean processed) {
        this.processed = processed;
        return this;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public FileCatalog deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getInsert() {
        return insert;
    }

    public FileCatalog insert(Instant insert) {
        this.insert = insert;
        return this;
    }

    public void setInsert(Instant insert) {
        this.insert = insert;
    }

    public Instant getProcessFinishDate() {
        return processFinishDate;
    }

    public FileCatalog processFinishDate(Instant processFinishDate) {
        this.processFinishDate = processFinishDate;
        return this;
    }

    public void setProcessFinishDate(Instant processFinishDate) {
        this.processFinishDate = processFinishDate;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public FileCatalog deviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileCatalog fileCatalog = (FileCatalog) o;
        if (fileCatalog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fileCatalog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FileCatalog{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            ", processed='" + isProcessed() + "'" +
            ", deleted='" + isDeleted() + "'" +
            ", insert='" + getInsert() + "'" +
            ", processFinishDate='" + getProcessFinishDate() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            "}";
    }
}
