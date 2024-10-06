package com.kamjritztex.aoms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kamjritztex.aoms.models.ExitInterview;
import com.kamjritztex.aoms.models.OffboardingProcess;

@Repository
public interface ExitInterviewRepository extends JpaRepository<ExitInterview, Long> {
   Optional<ExitInterview> findByOffboardingProcess(OffboardingProcess process);
}