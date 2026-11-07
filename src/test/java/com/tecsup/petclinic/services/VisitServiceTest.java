package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.util.CasoLog;
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
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@ActiveProfiles("h2")
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

        CasoLog.inicio("CASO 4 - VISITS", "Busqueda de una visita por id");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("id solicitado", ID);

        VisitDTO visit = this.visitService.findById(ID);

        CasoLog.seccion("REGISTRO OBTENIDO");
        CasoLog.dato("id", visit.getId());
        CasoLog.dato("pet_id", visit.getPetId());
        CasoLog.dato("vet_id", visit.getVetId());
        CasoLog.dato("visit_date", visit.getVisitDate());
        CasoLog.dato("description", visit.getDescription());
        CasoLog.dato("cost", visit.getCost().toPlainString());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("id", ID, visit.getId());
        CasoLog.verificacion("pet_id", PET_ID_EXPECTED, visit.getPetId());
        CasoLog.verificacion("vet_id", VET_ID_EXPECTED, visit.getVetId());
        CasoLog.verificacion("description", DESCRIPTION_EXPECTED, visit.getDescription());
        CasoLog.verificacion("cost", COST_EXPECTED.toPlainString(), visit.getCost().toPlainString());

        assertNotNull(visit);
        assertEquals(ID, visit.getId());
        assertEquals(PET_ID_EXPECTED, visit.getPetId());
        assertEquals(VET_ID_EXPECTED, visit.getVetId());
        assertEquals(DESCRIPTION_EXPECTED, visit.getDescription());
        assertEquals(0, COST_EXPECTED.compareTo(visit.getCost()));

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 4: buscar visitas de una mascota")
    public void testFindByPetId() {

        final Integer PET_ID = 7;
        final int SIZE_EXPECTED = 2;

        CasoLog.inicio("CASO 4 - VISITS", "Busqueda de visitas por mascota (pet_id)");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("pet_id buscado", PET_ID);

        List<VisitDTO> visits = this.visitService.findByPetId(PET_ID);

        CasoLog.seccion("REGISTROS OBTENIDOS");
        CasoLog.dato("cantidad encontrada", visits.size());
        for (int i = 0; i < visits.size(); i++) {
            CasoLog.dato("registro " + (i + 1),
                    visits.get(i).getId() + " | pet_id=" + visits.get(i).getPetId()
                            + " | vet_id=" + visits.get(i).getVetId()
                            + " | " + visits.get(i).getVisitDate()
                            + " | " + visits.get(i).getDescription()
                            + " | S/ " + visits.get(i).getCost().toPlainString());
        }

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("cantidad", SIZE_EXPECTED, visits.size());
        CasoLog.ok("todas las visitas pertenecen a la mascota " + PET_ID);

        assertEquals(SIZE_EXPECTED, visits.size());
        assertTrue(visits.stream().allMatch(visit -> PET_ID.equals(visit.getPetId())));

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 4: buscar visitas de un veterinario")
    public void testFindByVetId() {

        final Integer VET_ID = 2;
        final int SIZE_EXPECTED = 2;

        CasoLog.inicio("CASO 4 - VISITS", "Busqueda de visitas por veterinario (vet_id)");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("vet_id buscado", VET_ID);

        List<VisitDTO> visits = this.visitService.findByVetId(VET_ID);

        CasoLog.seccion("REGISTROS OBTENIDOS");
        CasoLog.dato("cantidad encontrada", visits.size());
        for (int i = 0; i < visits.size(); i++) {
            CasoLog.dato("registro " + (i + 1),
                    visits.get(i).getId() + " | pet_id=" + visits.get(i).getPetId()
                            + " | vet_id=" + visits.get(i).getVetId()
                            + " | " + visits.get(i).getVisitDate()
                            + " | " + visits.get(i).getDescription());
        }

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("cantidad", SIZE_EXPECTED, visits.size());
        CasoLog.ok("todas las visitas fueron atendidas por el veterinario " + VET_ID);

        assertEquals(SIZE_EXPECTED, visits.size());
        assertTrue(visits.stream().allMatch(visit -> VET_ID.equals(visit.getVetId())));

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 4: crear visita")
    public void testCreateVisit() {

        final Integer PET_ID = 1;
        final Integer VET_ID = 1;
        final LocalDate VISIT_DATE = LocalDate.of(2026, 11, 6);
        final String DESCRIPTION = "control general";
        final BigDecimal COST = new BigDecimal("80.00");

        CasoLog.inicio("CASO 4 - VISITS", "Registro de una visita (INSERT)");
        CasoLog.seccion("DATOS ENVIADOS AL SERVICIO");
        CasoLog.dato("pet_id", PET_ID);
        CasoLog.dato("vet_id", VET_ID);
        CasoLog.dato("visit_date", VISIT_DATE);
        CasoLog.dato("description", DESCRIPTION);
        CasoLog.dato("cost", COST.toPlainString());

        VisitDTO visitDTO = VisitDTO.builder()
                .petId(PET_ID)
                .vetId(VET_ID)
                .visitDate(VISIT_DATE)
                .description(DESCRIPTION)
                .cost(COST)
                .build();

        VisitDTO newVisitDTO = this.visitService.create(visitDTO);

        CasoLog.seccion("REGISTRO GUARDADO EN LA BASE DE DATOS");
        CasoLog.dato("id generado", newVisitDTO.getId());
        CasoLog.dato("pet_id", newVisitDTO.getPetId());
        CasoLog.dato("vet_id", newVisitDTO.getVetId());
        CasoLog.dato("visit_date", newVisitDTO.getVisitDate());
        CasoLog.dato("description", newVisitDTO.getDescription());
        CasoLog.dato("cost", newVisitDTO.getCost().toPlainString());

        CasoLog.seccion("VERIFICACION");
        CasoLog.ok("la base de datos genero el id " + newVisitDTO.getId());
        CasoLog.verificacion("pet_id", PET_ID, newVisitDTO.getPetId());
        CasoLog.verificacion("vet_id", VET_ID, newVisitDTO.getVetId());
        CasoLog.verificacion("visit_date", VISIT_DATE, newVisitDTO.getVisitDate());
        CasoLog.verificacion("description", DESCRIPTION, newVisitDTO.getDescription());
        CasoLog.verificacion("cost", COST.toPlainString(), newVisitDTO.getCost().toPlainString());

        assertNotNull(newVisitDTO.getId());
        assertEquals(PET_ID, newVisitDTO.getPetId());
        assertEquals(VET_ID, newVisitDTO.getVetId());
        assertEquals(VISIT_DATE, newVisitDTO.getVisitDate());
        assertEquals(DESCRIPTION, newVisitDTO.getDescription());
        assertEquals(0, COST.compareTo(newVisitDTO.getCost()));

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 4: actualizar visita")
    public void testUpdateVisit() {

        final String UP_DESCRIPTION = "control general actualizado";
        final BigDecimal UP_COST = new BigDecimal("100.50");

        CasoLog.inicio("CASO 4 - VISITS", "Actualizacion de una visita (UPDATE)");
        CasoLog.seccion("DATOS PARA CREAR EL REGISTRO");
        CasoLog.dato("pet_id", 13);
        CasoLog.dato("vet_id", 5);
        CasoLog.dato("visit_date", "2026-10-15");
        CasoLog.dato("description", "consulta");
        CasoLog.dato("cost", "60.00");

        VisitDTO visitDTO = VisitDTO.builder()
                .petId(13)
                .vetId(5)
                .visitDate(LocalDate.of(2026, 10, 15))
                .description("consulta")
                .cost(new BigDecimal("60.00"))
                .build();

        VisitDTO visitDTOCreated = this.visitService.create(visitDTO);

        CasoLog.seccion("REGISTRO CREADO");
        CasoLog.dato("id", visitDTOCreated.getId());
        CasoLog.dato("description anterior", visitDTOCreated.getDescription());
        CasoLog.dato("cost anterior", visitDTOCreated.getCost().toPlainString());

        Integer createdId = visitDTOCreated.getId();
        visitDTOCreated.setDescription(UP_DESCRIPTION);
        visitDTOCreated.setCost(UP_COST);

        CasoLog.seccion("DATOS ENVIADOS EN LA ACTUALIZACION");
        CasoLog.dato("id", createdId);
        CasoLog.dato("description nueva", UP_DESCRIPTION);
        CasoLog.dato("cost nuevo", UP_COST.toPlainString());

        VisitDTO visitDTOUpdated = this.visitService.update(visitDTOCreated);

        CasoLog.seccion("REGISTRO ACTUALIZADO");
        CasoLog.dato("id", visitDTOUpdated.getId());
        CasoLog.dato("description", visitDTOUpdated.getDescription());
        CasoLog.dato("cost", visitDTOUpdated.getCost().toPlainString());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("id", createdId, visitDTOUpdated.getId());
        CasoLog.verificacion("description", UP_DESCRIPTION, visitDTOUpdated.getDescription());
        CasoLog.verificacion("cost", UP_COST.toPlainString(), visitDTOUpdated.getCost().toPlainString());

        assertEquals(createdId, visitDTOUpdated.getId());
        assertEquals(UP_DESCRIPTION, visitDTOUpdated.getDescription());
        assertEquals(0, UP_COST.compareTo(visitDTOUpdated.getCost()));

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 4: eliminar visita")
    public void testDeleteVisit() throws VisitNotFoundException {

        CasoLog.inicio("CASO 4 - VISITS", "Eliminacion de una visita (DELETE)");
        CasoLog.seccion("DATOS PARA CREAR EL REGISTRO A ELIMINAR");
        CasoLog.dato("pet_id", 2);
        CasoLog.dato("vet_id", 1);
        CasoLog.dato("visit_date", "2026-09-20");
        CasoLog.dato("description", "vacunacion");
        CasoLog.dato("cost", "55.00");

        VisitDTO visitDTO = VisitDTO.builder()
                .petId(2)
                .vetId(1)
                .visitDate(LocalDate.of(2026, 9, 20))
                .description("vacunacion")
                .cost(new BigDecimal("55.00"))
                .build();

        VisitDTO newVisitDTO = this.visitService.create(visitDTO);

        CasoLog.seccion("REGISTRO CREADO");
        CasoLog.dato("id", newVisitDTO.getId());
        CasoLog.dato("description", newVisitDTO.getDescription());
        CasoLog.dato("cost", newVisitDTO.getCost().toPlainString());

        this.visitService.delete(newVisitDTO.getId());

        CasoLog.seccion("VERIFICACION DESPUES DEL DELETE");
        CasoLog.dato("id eliminado", newVisitDTO.getId());

        VisitNotFoundException exception = assertThrows(VisitNotFoundException.class,
                () -> this.visitService.findById(newVisitDTO.getId()));

        CasoLog.dato("excepcion lanzada", exception.getClass().getSimpleName());
        CasoLog.dato("mensaje", exception.getMessage());
        CasoLog.verificacion("tipo de excepcion", "VisitNotFoundException",
                exception.getClass().getSimpleName());
        CasoLog.ok("la visita eliminada ya no se puede recuperar");

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 4: visita inexistente lanza excepcion")
    public void testFindVisitById_NotFound() {

        final Integer ID = 9999;

        CasoLog.inicio("CASO 4 - VISITS", "Busqueda de una visita que no existe");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("id solicitado", ID);

        VisitNotFoundException exception = assertThrows(VisitNotFoundException.class,
                () -> this.visitService.findById(ID));

        CasoLog.seccion("RESULTADO DE LA BUSQUEDA");
        CasoLog.dato("excepcion lanzada", exception.getClass().getSimpleName());
        CasoLog.dato("mensaje", exception.getMessage());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("tipo de excepcion", "VisitNotFoundException",
                exception.getClass().getSimpleName());

        CasoLog.fin();
    }
}
