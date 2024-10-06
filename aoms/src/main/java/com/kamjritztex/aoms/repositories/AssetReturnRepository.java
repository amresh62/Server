package com.kamjritztex.aoms.repositories;

import com.kamjritztex.aoms.models.AssetReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetReturnRepository extends JpaRepository<AssetReturn, Long> {
}