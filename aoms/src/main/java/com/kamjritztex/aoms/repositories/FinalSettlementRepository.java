package com.kamjritztex.aoms.repositories;

import com.kamjritztex.aoms.models.FinalSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinalSettlementRepository extends JpaRepository<FinalSettlement, Long> {
}