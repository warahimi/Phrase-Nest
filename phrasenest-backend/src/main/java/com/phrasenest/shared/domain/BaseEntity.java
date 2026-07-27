
package com.phrasenest.shared.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.Instant;

/**
 * Base class for database entities that need created and updated timestamps.
 *
 * @MappedSuperclass means:
 * - This class is not its own database table.
 * - Its fields become columns in the tables of child entities.
 */
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * The time when the record was first inserted.
     *
     * updatable = false prevents Hibernate from changing this value later.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * The most recent time when the record was updated.
     */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Runs automatically before the entity is inserted.
     */
    @PrePersist
    protected void beforeInsert() {
        Instant now = Instant.now();

        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Runs automatically before the entity is updated.
     */
    @PreUpdate
    protected void beforeUpdate() {
        this.updatedAt = Instant.now();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}