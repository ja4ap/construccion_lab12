package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// CASO 6 - Pruebas unitarias de la tabla vet_specialties
@SpringBootTest
@ActiveProfiles("h2")
@Slf4j
public class VetSpecialtyServiceTest {

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    @Test
    @DisplayName("CASO 6: asignar especialidad a un veterinario")
    public void testAssignSpecialtyToVet() throws VetNotFoundException, SpecialtyNotFoundException,
            VetSpecialtyNotFoundException {

        final Integer VET_ID = 1;
        final Integer SPECIALTY_ID = 3;
        final LocalDate CERTIFICATION_DATE = LocalDate.of(2020, 5, 18);
        final Integer YEARS_EXPERIENCE = 6;

        VetSpecialtyDTO vetSpecialtyDTO = VetSpecialtyDTO.builder()
                .vetId(VET_ID)
                .specialtyId(SPECIALTY_ID)
                .certificationDate(CERTIFICATION_DATE)
                .yearsExperience(YEARS_EXPERIENCE)
                .isPrimary(false)
                .notes("Asignacion de prueba")
                .build();

        VetSpecialtyDTO newVetSpecialtyDTO = this.vetSpecialtyService.assign(vetSpecialtyDTO);

        log.info("VET-SPECIALTY ASSIGNED : {}", newVetSpecialtyDTO);

        assertNotNull(newVetSpecialtyDTO);
        assertEquals(VET_ID, newVetSpecialtyDTO.getVetId());
        assertEquals(SPECIALTY_ID, newVetSpecialtyDTO.getSpecialtyId());
        assertEquals(CERTIFICATION_DATE, newVetSpecialtyDTO.getCertificationDate());
        assertEquals(YEARS_EXPERIENCE, newVetSpecialtyDTO.getYearsExperience());

        VetSpecialtyDTO found = this.vetSpecialtyService.findById(VET_ID, SPECIALTY_ID);
        assertEquals(YEARS_EXPERIENCE, found.getYearsExperience());
    }

    @Test
    @DisplayName("CASO 6: actualizar datos de la asignacion")
    public void testUpdateVetSpecialty() throws VetNotFoundException, SpecialtyNotFoundException,
            VetSpecialtyNotFoundException {

        final Integer VET_ID = 1;
        final Integer SPECIALTY_ID = 2;
        final Integer UP_YEARS_EXPERIENCE = 12;

        VetSpecialtyDTO vetSpecialtyDTO = VetSpecialtyDTO.builder()
                .vetId(VET_ID)
                .specialtyId(SPECIALTY_ID)
                .certificationDate(LocalDate.of(2019, 3, 10))
                .yearsExperience(4)
                .isPrimary(false)
                .notes("Registro inicial")
                .build();

        this.vetSpecialtyService.assign(vetSpecialtyDTO);

        vetSpecialtyDTO.setYearsExperience(UP_YEARS_EXPERIENCE);
        vetSpecialtyDTO.setIsPrimary(true);
        vetSpecialtyDTO.setNotes("Registro actualizado");

        VetSpecialtyDTO vetSpecialtyDTOUpdated = this.vetSpecialtyService.update(vetSpecialtyDTO);
        log.info("VET-SPECIALTY UPDATED : {}", vetSpecialtyDTOUpdated);

        assertEquals(UP_YEARS_EXPERIENCE, vetSpecialtyDTOUpdated.getYearsExperience());
        assertTrue(vetSpecialtyDTOUpdated.getIsPrimary());
        assertEquals("Registro actualizado", vetSpecialtyDTOUpdated.getNotes());
    }

    @Test
    @DisplayName("CASO 6: buscar especialidades de un veterinario")
    public void testFindSpecialtiesByVet() throws VetNotFoundException {

        final Integer VET_ID = 3;

        List<SpecialtyDTO> specialties = this.vetSpecialtyService.findSpecialtiesByVetId(VET_ID);

        log.info("SPECIALTIES OF VET {} : {}", VET_ID, specialties.size());
        specialties.forEach(specialty -> log.info("{}", specialty));

        assertEquals(2, specialties.size());
        assertTrue(specialties.stream().anyMatch(specialty -> "surgery".equals(specialty.getName())));
        assertTrue(specialties.stream().anyMatch(specialty -> "dentistry".equals(specialty.getName())));
    }

    @Test
    @DisplayName("CASO 6: buscar veterinarios de una especialidad")
    public void testFindVetsBySpecialty() throws SpecialtyNotFoundException {

        final Integer SPECIALTY_ID = 1;

        List<VetDTO> vets = this.vetSpecialtyService.findVetsBySpecialtyId(SPECIALTY_ID);

        log.info("VETS OF SPECIALTY {} : {}", SPECIALTY_ID, vets.size());
        vets.forEach(vet -> log.info("{}", vet));

        assertEquals(2, vets.size());
        assertTrue(vets.stream().anyMatch(vet -> "Leary".equals(vet.getLastName())));
        assertTrue(vets.stream().anyMatch(vet -> "Stevens".equals(vet.getLastName())));
    }

    @Test
    @DisplayName("CASO 6: eliminar la relacion veterinario-especialidad")
    public void testRemoveSpecialtyFromVet() throws VetNotFoundException, SpecialtyNotFoundException,
            VetSpecialtyNotFoundException {

        final Integer VET_ID = 6;
        final Integer SPECIALTY_ID = 3;

        VetSpecialtyDTO vetSpecialtyDTO = VetSpecialtyDTO.builder()
                .vetId(VET_ID)
                .specialtyId(SPECIALTY_ID)
                .certificationDate(LocalDate.of(2021, 8, 1))
                .yearsExperience(2)
                .isPrimary(false)
                .notes("Registro a eliminar")
                .build();

        this.vetSpecialtyService.assign(vetSpecialtyDTO);

        this.vetSpecialtyService.remove(VET_ID, SPECIALTY_ID);

        assertThrows(VetSpecialtyNotFoundException.class,
                () -> this.vetSpecialtyService.findById(VET_ID, SPECIALTY_ID));
    }

    @Test
    @DisplayName("CASO 6: relacion inexistente lanza excepcion")
    public void testRemoveVetSpecialty_NotFound() {

        assertThrows(VetSpecialtyNotFoundException.class,
                () -> this.vetSpecialtyService.remove(9999, 9999));
    }

    @Test
    @DisplayName("CASO 6: buscar relacion por veterinario y especialidad")
    public void testFindVetSpecialtyById() throws VetSpecialtyNotFoundException {

        final Integer VET_ID = 2;
        final Integer SPECIALTY_ID = 1;

        VetSpecialtyDTO vetSpecialty = this.vetSpecialtyService.findById(VET_ID, SPECIALTY_ID);

        log.info("VET-SPECIALTY FOUND : {}", vetSpecialty);

        assertNotNull(vetSpecialty);
        assertEquals(VET_ID, vetSpecialty.getVetId());
        assertEquals(SPECIALTY_ID, vetSpecialty.getSpecialtyId());
        assertEquals(8, vetSpecialty.getYearsExperience());
        assertTrue(vetSpecialty.getIsPrimary());
    }
}
