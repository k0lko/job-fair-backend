package com.JFBRA.repository;

import com.JFBRA.model.Booth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoothRepository extends JpaRepository<Booth, Long> {

    List<Booth> findByStatus(Booth.BoothStatus status);

    Booth findByBoothNumber(String boothNumber);

    boolean existsByBoothNumber(String boothNumber);
}