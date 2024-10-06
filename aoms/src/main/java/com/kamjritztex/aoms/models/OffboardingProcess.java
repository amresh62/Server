package com.kamjritztex.aoms.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "offboarding_process")
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class OffboardingProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    private LocalDate initiationDate;
    private LocalDate lastWorkingDate;
    
    @Enumerated(EnumType.STRING)
    private OffboardingStatus status;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "offboardingProcess", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
    
    @JsonManagedReference
    @OneToOne(mappedBy = "offboardingProcess", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExitInterview exitInterview;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "offboardingProcess", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssetReturn> assetReturns = new ArrayList<>();
    
    @JsonManagedReference
    @OneToOne(mappedBy = "offboardingProcess", cascade = CascadeType.ALL, orphanRemoval = true)
    private FinalSettlement finalSettlement;
}
