package com.kamjritztex.aoms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kamjritztex.aoms.models.Asset;
import com.kamjritztex.aoms.models.Employee;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByEmployee(Employee employee);
}
