package com.kamjritztex.aoms.repositories;

import com.kamjritztex.aoms.models.OffboardingProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffboardingProcessRepository extends JpaRepository<OffboardingProcess, Long> {
}