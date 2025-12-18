package com.JFBRA.repository;

import com.JFBRA.model.AdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<AdditionalService, Long> {

    List<AdditionalService> findByIsActive(Boolean isActive);

    AdditionalService findByServiceCode(String serviceCode);

    List<AdditionalService> findByServiceCodeIn(List<String> serviceCodes);

    boolean existsByServiceCode(String serviceCode);
}