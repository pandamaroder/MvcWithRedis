package com.example.demo.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@NoArgsConstructor
public class ClockAwareEntityListener implements Serializable {

    private static final long serialVersionUID = 1L;
    // Couldn't use constructor injection here
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private Clock clock;

    @PrePersist
    public void initCreatedAt(@Nonnull final BaseEntity entity) {
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDateTime.now(ZoneId.systemDefault()));
        }
    }

    @PreUpdate
    public void initUpdatedAt(@Nonnull final BaseEntity entity) {
        if (entity.getId() != null && entity.getCreatedAt() != null) {
            entity.setUpdatedAt(LocalDateTime.now(ZoneId.systemDefault()));
        }
    }

}
