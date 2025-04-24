package com.repsy.fullstackdevassignment.repository;

import com.repsy.fullstackdevassignment.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
    Optional<PackageEntity> findByNameAndVersion(String name, String version);
}