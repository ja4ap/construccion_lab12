package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// CASO 4 - Pruebas unitarias de la tabla visits
@SpringBootTest
@ActiveProfiles("h2")
@Slf4j
public class VisitServiceTest {

    @Autowired
    private VisitService visitService;

    @Test
    @DisplayName("CASO 4: buscar visita por id")
    public void testFindVisitById() throws VisitNotFoundException {

        final Integer ID = 1;
        final Integer PET_ID_EXPECTED = 7;
        final Integer VET_ID_EXPECTED = 2;
        final String DESCRIPTION_EXPECTED = "rabies shot";
        final BigDecimal COST_EXPECTED = new BigDecimal("45.00");

        VisitDTO visit = this.visitService.findById(ID);

        log.info("VISIT FOUND : {}", visit);

        assertNotNull(visit);
        assertEquals(ID, visit.getId());
        assertEquals(PET_ID_EXPECTED, visit.getPetId());
        assertEquals(VET_ID_EXPECTED, visit.getVetId());
        assertEquals(DESCRIPTION_EXPECTED, visit.getDescription());
        assertEquals(0, COST_EXPECTED.compareTo(visit.getCost()));
    }

    @Test
    @DisplayName("CASO 4: buscar visitas de una mascota")
    public void testFindByPetId() {

        final Integer PET_ID = 7;
        final int SIZE_EXPECTED = 2;

        List<VisitDTO> visits = this.visitService.findByPetId(PET_ID);

        log.info("VISITS FOUND : {}", visits.size());

        assertEquals(SIZE_EXPECTED, visits.size());
        assertTrue(visits.stream().allMatch(visit -> PET_ID.equals(visit.getPetId())));
    }

    @Test
    @DisplayName("CASO 4: buscar visitas de un veterinario")
    public void testFindByVetId() {

        final Integer VET_ID = 2;
        final int SIZE_EXPECTED = 2;

        List<VisitDTO> visits = this.visitService.findByVetId(VET_ID);

        log.info("VISITS FOUND : {}", visits.size());

        assertEquals(SIZE_EXPECTED, visits.size());
        assertTrue(visits.stream().allMatch(visit -> VET_ID.equals(visit.getVetId())));
    }

    @Test
    @DisplayName("CASO 4: crear visita")
    public void testCreateVisit() {

        final Integer PET_ID = 1;
        final Integer VET_ID = 1;
        final LocalDate VISIT_DATE = LocalDate.of(2026, 11, 6);
        final String DESCRIPTION = "control general";
        final BigDecimal COST = new BigDecimal("80.00");

        VisitDTO visitDTO = VisitDTO.builder()
                .petId(PET_ID)
                .vetId(VET_ID)
                .visitDate(VISIT_DATE)
                .description(DESCRIPTION)
                .cost(COST)
                .build();

        VisitDTO newVisitDTO = this.visitService.create(visitDTO);

        log.info("VISIT CREATED : {}", newVisitDTO);

        assertNotNull(newVisitDTO.getId());
        assertEquals(PET_ID, newVisitDTO.getPetId());
        assertEquals(VET_ID, newVisitDTO.getVetId());
        assertEquals(VISIT_DATE, newVisitDTO.getVisitDate());
        assertEquals(DESCRIPTION, newVisitDTO.getDescription());
        assertEquals(0, COST.compareTo(newVisitDTO.getCost()));
    }

    @Test
    @DisplayName("CASO 4: actualizar visita")
    public void testUpdateVisit() throws VisitNotFoundException {

        final String UP_DESCRIPTION = "control general actualizado";
        final BigDecimal UP_COST = new BigDecimal("100.50");

        VisitDTO visitDTO = VisitDTO.builder()
                .petId(13)
                .vetId(5)
                .visitDate(LocalDate.of(2026, 10, 15))
                .description("consulta")
                .cost(new BigDecimal("60.00"))
                .build();

        VisitDTO visitDTOCreated = this.visitService.create(visitDTO);
        log.info("VISIT CREATED : {}", visitDTOCreated);

        Integer createdId = visitDTOCreated.getId();
        visitDTOCreated.setDescription(UP_DESCRIPTION);
        visitDTOCreated.setCost(UP_COST);

        VisitDTO visitDTOUpdated = this.visitService.update(visitDTOCreated);
        log.info("VISIT UPDATED : {}", visitDTOUpdated);

        assertEquals(createdId, visitDTOUpdated.getId());
        assertEquals(UP_DESCRIPTION, visitDTOUpdated.getDescription());
        assertEquals(0, UP_COST.compareTo(visitDTOUpdated.getCost()));
    }

    @Test
    @DisplayName("CASO 4: eliminar visita")
    public void testDeleteVisit() throws VisitNotFoundException {

        VisitDTO visitDTO = VisitDTO.builder()
                .petId(2)
                .vetId(1)
                .visitDate(LocalDate.of(2026, 9, 20))
                .description("vacunacion")
                .cost(new BigDecimal("55.00"))
                .build();

        VisitDTO newVisitDTO = this.visitService.create(visitDTO);
        log.info("VISIT TO DELETE : {}", newVisitDTO);

        this.visitService.delete(newVisitDTO.getId());

        assertThrows(VisitNotFoundException.class,
                () -> this.visitService.findById(newVisitDTO.getId()));
    }

    @Test
    @DisplayName("CASO 4: visita inexistente lanza excepcion")
    public void testFindVisitById_NotFound() {

        final Integer ID = 9999;

        assertThrows(VisitNotFoundException.class, () -> this.visitService.findById(ID));
    }
}
