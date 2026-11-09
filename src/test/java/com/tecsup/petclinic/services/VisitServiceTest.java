package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.util.CasoLog;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
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
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@ActiveProfiles("h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VisitServiceTest {

    @Autowired
    private VisitService visitService;

    @BeforeAll
    public void inicio() {
        CasoLog.caso("CASO 4 - VISITS", "visits");
    }

    @Test
    @Order(1)
    @DisplayName("CASO 4: buscar visita por id")
    public void testFindVisitById() throws VisitNotFoundException {

        final Integer ID = 1;

        VisitDTO visit = this.visitService.findById(ID);

        assertNotNull(visit);
        assertEquals(ID, visit.getId());
        assertEquals(7, visit.getPetId());
        assertEquals(2, visit.getVetId());
        assertEquals("rabies shot", visit.getDescription());
        assertEquals(0, new BigDecimal("45.00").compareTo(visit.getCost()));

        CasoLog.fila(1, "buscar por id", "id=" + ID,
                "pet " + visit.getPetId() + " | vet " + visit.getVetId()
                        + " | " + visit.getDescription() + " | S/" + visit.getCost().toPlainString());
    }

    @Test
    @Order(2)
    @DisplayName("CASO 4: buscar visitas de una mascota")
    public void testFindByPetId() {

        final Integer PET_ID = 7;

        List<VisitDTO> visits = this.visitService.findByPetId(PET_ID);

        assertEquals(2, visits.size());
        assertTrue(visits.stream().allMatch(visit -> PET_ID.equals(visit.getPetId())));

        CasoLog.fila(2, "visitas de mascota", "pet_id=" + PET_ID, visits.size() + " visitas");
    }

    @Test
    @Order(3)
    @DisplayName("CASO 4: buscar visitas de un veterinario")
    public void testFindByVetId() {

        final Integer VET_ID = 2;

        List<VisitDTO> visits = this.visitService.findByVetId(VET_ID);

        assertEquals(2, visits.size());
        assertTrue(visits.stream().allMatch(visit -> VET_ID.equals(visit.getVetId())));

        CasoLog.fila(3, "visitas de veterinario", "vet_id=" + VET_ID, visits.size() + " visitas");
    }

    @Test
    @Order(4)
    @DisplayName("CASO 4: crear visita")
    public void testCreateVisit() {

        VisitDTO visitDTO = VisitDTO.builder()
                .petId(1)
                .vetId(1)
                .visitDate(LocalDate.of(2026, 11, 6))
                .description("control general")
                .cost(new BigDecimal("80.00"))
                .build();

        VisitDTO newVisitDTO = this.visitService.create(visitDTO);

        assertNotNull(newVisitDTO.getId());
        assertEquals(1, newVisitDTO.getPetId());
        assertEquals(1, newVisitDTO.getVetId());
        assertEquals(LocalDate.of(2026, 11, 6), newVisitDTO.getVisitDate());
        assertEquals(0, new BigDecimal("80.00").compareTo(newVisitDTO.getCost()));

        CasoLog.fila(4, "crear", "pet 1 | vet 1 | 2026-11-06",
                "id=" + newVisitDTO.getId() + " | S/" + newVisitDTO.getCost().toPlainString());
    }

    @Test
    @Order(5)
    @DisplayName("CASO 4: actualizar visita")
    public void testUpdateVisit() {

        VisitDTO visitDTO = VisitDTO.builder()
                .petId(13)
                .vetId(5)
                .visitDate(LocalDate.of(2026, 10, 15))
                .description("consulta")
                .cost(new BigDecimal("60.00"))
                .build();

        VisitDTO creado = this.visitService.create(visitDTO);
        Integer id = creado.getId();

        creado.setDescription("control general actualizado");
        creado.setCost(new BigDecimal("100.50"));

        VisitDTO actualizado = this.visitService.update(creado);

        assertEquals(id, actualizado.getId());
        assertEquals("control general actualizado", actualizado.getDescription());
        assertEquals(0, new BigDecimal("100.50").compareTo(actualizado.getCost()));

        CasoLog.fila(5, "actualizar", "id=" + id,
                "S/" + actualizado.getCost().toPlainString() + " | " + actualizado.getDescription());
    }

    @Test
    @Order(6)
    @DisplayName("CASO 4: eliminar visita")
    public void testDeleteVisit() throws VisitNotFoundException {

        VisitDTO visitDTO = VisitDTO.builder()
                .petId(2)
                .vetId(1)
                .visitDate(LocalDate.of(2026, 9, 20))
                .description("vacunacion")
                .cost(new BigDecimal("55.00"))
                .build();

        VisitDTO creado = this.visitService.create(visitDTO);

        this.visitService.delete(creado.getId());

        assertThrows(VisitNotFoundException.class, () -> this.visitService.findById(creado.getId()));

        CasoLog.fila(6, "eliminar", "id=" + creado.getId(),
                "ya no existe (VisitNotFoundException)");
    }

    @Test
    @Order(7)
    @DisplayName("CASO 4: visita inexistente lanza excepcion")
    public void testFindVisitById_NotFound() {

        final Integer ID = 9999;

        assertThrows(VisitNotFoundException.class, () -> this.visitService.findById(ID));

        CasoLog.fila(7, "buscar inexistente", "id=" + ID, "lanza VisitNotFoundException");
    }
}
