package com.kamjritztex.aoms.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class ExitInterview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // @JsonBackReference
    // @OneToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "offboarding_process_id")
    // private OffboardingProcess offboardingProcess;

    //@JsonIgnoreProperties("exitInterview")
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offboarding_process_id")
    private OffboardingProcess offboardingProcess;

    
    private LocalDateTime scheduledAt;
    private LocalDateTime conductedAt;
    
    @Lob
    private String feedback;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conducted_by_id")
    private Employee conductedBy;
}