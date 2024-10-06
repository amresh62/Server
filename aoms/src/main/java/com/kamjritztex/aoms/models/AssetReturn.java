package com.kamjritztex.aoms.models;

import java.time.LocalDateTime;

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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asset_return")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asset_return_sequence")
    @SequenceGenerator(name = "asset_return_sequence", sequenceName = "asset_return_sequence", allocationSize = 100)
    private Long id;
    
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offboarding_process_id")
    private OffboardingProcess offboardingProcess;
    
    private String assetName;
    private String assetId;
    
    @Enumerated(EnumType.STRING)
    private AssetReturnStatus status;
    
    private LocalDateTime returnedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by_id")
    private Employee receivedBy;
}