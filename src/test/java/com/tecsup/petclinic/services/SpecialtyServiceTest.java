package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

// CASO 3 - Pruebas unitarias de la tabla specialties
@SpringBootTest
@ActiveProfiles("h2")
@Slf4j
public class SpecialtyServiceTest {

    @Autowired
    private SpecialtyService specialtyService;

    @Test
    @DisplayName("CASO 3: buscar especialidad por id")
    public void testFindSpecialtyById() throws SpecialtyNotFoundException {

        final Integer ID = 1;
        final String NAME_EXPECTED = "radiology";
        final String OFFICE_EXPECTED = "Farewell";

        SpecialtyDTO specialty = this.specialtyService.findById(ID);

        log.info("SPECIALTY FOUND : {}", specialty);

        assertNotNull(specialty);
        assertEquals(ID, specialty.getId());
        assertEquals(NAME_EXPECTED, specialty.getName());
        assertEquals(OFFICE_EXPECTED, specialty.getOffice());
        assertEquals(8, specialty.getOpenHour());
        assertEquals(18, specialty.getCloseHour());
    }

    @Test
    @DisplayName("CASO 3: buscar especialidad por nombre")
    public void testFindSpecialtyByName() {

        final String NAME = "surgery";
        final String OFFICE_EXPECTED = "Maryland";

        List<SpecialtyDTO> specialties = this.specialtyService.findByName(NAME);

        log.info("SPECIALTIES FOUND : {}", specialties.size());

        assertEquals(1, specialties.size());
        assertEquals(OFFICE_EXPECTED, specialties.get(0).getOffice());
    }

    @Test
    @DisplayName("CASO 3: buscar especialidad por consultorio")
    public void testFindSpecialtyByOffice() {

        final String OFFICE = "Terranova";
        final String NAME_EXPECTED = "dentistry";

        List<SpecialtyDTO> specialties = this.specialtyService.findByOffice(OFFICE);

        log.info("SPECIALTIES FOUND : {}", specialties.size());

        assertEquals(1, specialties.size());
        assertEquals(NAME_EXPECTED, specialties.get(0).getName());
    }

    @Test
    @DisplayName("CASO 3: crear especialidad")
    public void testCreateSpecialty() {

        final String NAME = "cardiology";
        final String OFFICE = "Lima";
        final Integer H_OPEN = 7;
        final Integer H_CLOSE = 15;

        SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
                .name(NAME)
                .office(OFFICE)
                .openHour(H_OPEN)
                .closeHour(H_CLOSE)
                .build();

        SpecialtyDTO newSpecialtyDTO = this.specialtyService.create(specialtyDTO);

        log.info("SPECIALTY CREATED : {}", newSpecialtyDTO);

        assertNotNull(newSpecialtyDTO.getId());
        assertEquals(NAME, newSpecialtyDTO.getName());
        assertEquals(OFFICE, newSpecialtyDTO.getOffice());
        assertEquals(H_OPEN, newSpecialtyDTO.getOpenHour());
        assertEquals(H_CLOSE, newSpecialtyDTO.getCloseHour());
    }

    @Test
    @DisplayName("CASO 3: actualizar especialidad")
    public void testUpdateSpecialty() throws SpecialtyNotFoundException {

        final String UP_OFFICE = "Arequipa";
        final Integer UP_H_OPEN = 10;
        final Integer UP_H_CLOSE = 20;

        SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
                .name("dermatology")
                .office("Trujillo")
                .openHour(8)
                .closeHour(14)
                .build();

        SpecialtyDTO specialtyDTOCreated = this.specialtyService.create(specialtyDTO);
        log.info("SPECIALTY CREATED : {}", specialtyDTOCreated);

        Integer createdId = specialtyDTOCreated.getId();
        specialtyDTOCreated.setOffice(UP_OFFICE);
        specialtyDTOCreated.setOpenHour(UP_H_OPEN);
        specialtyDTOCreated.setCloseHour(UP_H_CLOSE);

        SpecialtyDTO specialtyDTOUpdated = this.specialtyService.update(specialtyDTOCreated);
        log.info("SPECIALTY UPDATED : {}", specialtyDTOUpdated);

        assertEquals(createdId, specialtyDTOUpdated.getId());
        assertEquals(UP_OFFICE, specialtyDTOUpdated.getOffice());
        assertEquals(UP_H_OPEN, specialtyDTOUpdated.getOpenHour());
        assertEquals(UP_H_CLOSE, specialtyDTOUpdated.getCloseHour());
    }

    @Test
    @DisplayName("CASO 3: eliminar especialidad")
    public void testDeleteSpecialty() throws SpecialtyNotFoundException {

        SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
                .name("ophthalmology")
                .office("Piura")
                .openHour(9)
                .closeHour(17)
                .build();

        SpecialtyDTO newSpecialtyDTO = this.specialtyService.create(specialtyDTO);
        log.info("SPECIALTY TO DELETE : {}", newSpecialtyDTO);

        this.specialtyService.delete(newSpecialtyDTO.getId());

        assertThrows(SpecialtyNotFoundException.class,
                () -> this.specialtyService.findById(newSpecialtyDTO.getId()));
    }

    @Test
    @DisplayName("CASO 3: especialidad inexistente lanza excepcion")
    public void testFindSpecialtyById_NotFound() {

        final Integer ID = 9999;

        assertThrows(SpecialtyNotFoundException.class, () -> this.specialtyService.findById(ID));
    }
}
