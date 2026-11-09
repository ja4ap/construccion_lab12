package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

// CASO 3 - Pruebas unitarias de la tabla specialties
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@ActiveProfiles("h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SpecialtyServiceTest {

    @Autowired
    private SpecialtyService specialtyService;

    @BeforeAll
    public void inicio() {
        CasoLog.caso("CASO 3 - SPECIALTIES", "specialties");
    }

    @Test
    @Order(1)
    @DisplayName("CASO 3: buscar especialidad por id")
    public void testFindSpecialtyById() throws SpecialtyNotFoundException {

        final Integer ID = 1;

        SpecialtyDTO specialty = this.specialtyService.findById(ID);

        assertNotNull(specialty);
        assertEquals(ID, specialty.getId());
        assertEquals("radiology", specialty.getName());
        assertEquals("Farewell", specialty.getOffice());
        assertEquals(8, specialty.getOpenHour());
        assertEquals(18, specialty.getCloseHour());

        CasoLog.fila(1, "buscar por id", "id=" + ID,
                specialty.getName() + " | " + specialty.getOffice()
                        + " " + specialty.getOpenHour() + "-" + specialty.getCloseHour());
    }

    @Test
    @Order(2)
    @DisplayName("CASO 3: buscar especialidad por nombre")
    public void testFindSpecialtyByName() {

        final String NAME = "surgery";

        List<SpecialtyDTO> specialties = this.specialtyService.findByName(NAME);

        assertEquals(1, specialties.size());
        assertEquals("Maryland", specialties.get(0).getOffice());

        CasoLog.fila(2, "buscar por nombre", "name=" + NAME,
                specialties.size() + " registro | " + specialties.get(0).getOffice());
    }

    @Test
    @Order(3)
    @DisplayName("CASO 3: buscar especialidad por consultorio")
    public void testFindSpecialtyByOffice() {

        final String OFFICE = "Terranova";

        List<SpecialtyDTO> specialties = this.specialtyService.findByOffice(OFFICE);

        assertEquals(1, specialties.size());
        assertEquals("dentistry", specialties.get(0).getName());

        CasoLog.fila(3, "buscar por consultorio", "office=" + OFFICE,
                specialties.size() + " registro | " + specialties.get(0).getName());
    }

    @Test
    @Order(4)
    @DisplayName("CASO 3: crear especialidad")
    public void testCreateSpecialty() {

        SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
                .name("cardiology")
                .office("Lima")
                .openHour(7)
                .closeHour(15)
                .build();

        SpecialtyDTO newSpecialtyDTO = this.specialtyService.create(specialtyDTO);

        assertNotNull(newSpecialtyDTO.getId());
        assertEquals("cardiology", newSpecialtyDTO.getName());
        assertEquals("Lima", newSpecialtyDTO.getOffice());
        assertEquals(7, newSpecialtyDTO.getOpenHour());
        assertEquals(15, newSpecialtyDTO.getCloseHour());

        CasoLog.fila(4, "crear", newSpecialtyDTO.getName() + " | " + newSpecialtyDTO.getOffice(),
                "id=" + newSpecialtyDTO.getId() + " generado");
    }

    @Test
    @Order(5)
    @DisplayName("CASO 3: actualizar especialidad")
    public void testUpdateSpecialty() {

        SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
                .name("dermatology")
                .office("Trujillo")
                .openHour(8)
                .closeHour(14)
                .build();

        SpecialtyDTO creado = this.specialtyService.create(specialtyDTO);
        Integer id = creado.getId();

        creado.setOffice("Arequipa");
        creado.setOpenHour(10);
        creado.setCloseHour(20);

        SpecialtyDTO actualizado = this.specialtyService.update(creado);

        assertEquals(id, actualizado.getId());
        assertEquals("Arequipa", actualizado.getOffice());
        assertEquals(10, actualizado.getOpenHour());
        assertEquals(20, actualizado.getCloseHour());

        CasoLog.fila(5, "actualizar", "id=" + id,
                actualizado.getOffice() + " | " + actualizado.getOpenHour()
                        + "-" + actualizado.getCloseHour());
    }

    @Test
    @Order(6)
    @DisplayName("CASO 3: eliminar especialidad")
    public void testDeleteSpecialty() throws SpecialtyNotFoundException {

        SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
                .name("ophthalmology")
                .office("Piura")
                .openHour(9)
                .closeHour(17)
                .build();

        SpecialtyDTO creado = this.specialtyService.create(specialtyDTO);

        this.specialtyService.delete(creado.getId());

        assertThrows(SpecialtyNotFoundException.class,
                () -> this.specialtyService.findById(creado.getId()));

        CasoLog.fila(6, "eliminar", "id=" + creado.getId(),
                "ya no existe (SpecialtyNotFoundException)");
    }

    @Test
    @Order(7)
    @DisplayName("CASO 3: especialidad inexistente lanza excepcion")
    public void testFindSpecialtyById_NotFound() {

        final Integer ID = 9999;

        assertThrows(SpecialtyNotFoundException.class, () -> this.specialtyService.findById(ID));

        CasoLog.fila(7, "buscar inexistente", "id=" + ID, "lanza SpecialtyNotFoundException");
    }
}
