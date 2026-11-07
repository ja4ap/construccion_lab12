package com.tecsup.petclinic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VetSpecialtyDTO {

    private Integer vetId;
    private Integer specialtyId;
    private LocalDate certificationDate;
    private Integer yearsExperience;
    private Boolean isPrimary;
    private String notes;
}
