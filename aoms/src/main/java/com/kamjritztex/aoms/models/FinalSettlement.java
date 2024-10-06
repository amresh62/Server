package com.kamjritztex.aoms.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "final_settlement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalSettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "final_settlement_sequence")
    @SequenceGenerator(name = "final_settlement_sequence", sequenceName = "final_settlement_sequence", allocationSize = 100)
    private Long id;
    
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offboarding_process_id")
    private OffboardingProcess offboardingProcess;
    
    private BigDecimal amount;
    private LocalDate settlementDate;
    
    @Enumerated(EnumType.STRING)
    private SettlementStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_id")
    private Employee approvedBy;
}