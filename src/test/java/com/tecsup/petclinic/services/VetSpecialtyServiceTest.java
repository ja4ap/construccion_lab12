package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// CASO 6 - Pruebas unitarias de la tabla vet_specialties
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@ActiveProfiles("h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VetSpecialtyServiceTest {

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    @BeforeAll
    public void inicio() {
        CasoLog.caso("CASO 6 - VET_SPECIALTIES", "vet_specialties");
    }

    @Test
    @Order(1)
    @DisplayName("CASO 6: asignar especialidad a un veterinario")
    public void testAssignSpecialtyToVet() throws VetNotFoundException, SpecialtyNotFoundException,
            VetSpecialtyNotFoundException {

        VetSpecialtyDTO vetSpecialtyDTO = VetSpecialtyDTO.builder()
                .vetId(1)
                .specialtyId(3)
                .certificationDate(LocalDate.of(2020, 5, 18))
                .yearsExperience(6)
                .isPrimary(false)
                .notes("Asignacion de prueba")
                .build();

        VetSpecialtyDTO nuevo = this.vetSpecialtyService.assign(vetSpecialtyDTO);
        VetSpecialtyDTO encontrado = this.vetSpecialtyService.findById(1, 3);

        assertNotNull(nuevo);
        assertEquals(1, nuevo.getVetId());
        assertEquals(3, nuevo.getSpecialtyId());
        assertEquals(LocalDate.of(2020, 5, 18), nuevo.getCertificationDate());
        assertEquals(6, nuevo.getYearsExperience());
        assertEquals(6, encontrado.getYearsExperience());

        CasoLog.fila(1, "asignar especialidad", "vet 1 | spec 3",
                "cert " + nuevo.getCertificationDate() + " | experiencia " + nuevo.getYearsExperience());
    }

    @Test
    @Order(2)
    @DisplayName("CASO 6: actualizar datos de la asignacion")
    public void testUpdateVetSpecialty() throws VetNotFoundException, SpecialtyNotFoundException,
            VetSpecialtyNotFoundException {

        VetSpecialtyDTO vetSpecialtyDTO = VetSpecialtyDTO.builder()
                .vetId(1)
                .specialtyId(2)
                .certificationDate(LocalDate.of(2019, 3, 10))
                .yearsExperience(4)
                .isPrimary(false)
                .notes("Registro inicial")
                .build();

        this.vetSpecialtyService.assign(vetSpecialtyDTO);

        vetSpecialtyDTO.setYearsExperience(12);
        vetSpecialtyDTO.setIsPrimary(true);
        vetSpecialtyDTO.setNotes("Registro actualizado");

        VetSpecialtyDTO actualizado = this.vetSpecialtyService.update(vetSpecialtyDTO);

        assertEquals(12, actualizado.getYearsExperience());
        assertTrue(actualizado.getIsPrimary());
        assertEquals("Registro actualizado", actualizado.getNotes());

        CasoLog.fila(2, "actualizar asignacion", "vet 1 | spec 2",
                "experiencia " + actualizado.getYearsExperience() + " | principal=" + actualizado.getIsPrimary());
    }

    @Test
    @Order(3)
    @DisplayName("CASO 6: buscar especialidades de un veterinario")
    public void testFindSpecialtiesByVet() throws VetNotFoundException {

        final Integer VET_ID = 3;

        List<SpecialtyDTO> specialties = this.vetSpecialtyService.findSpecialtiesByVetId(VET_ID);

        assertEquals(2, specialties.size());
        assertTrue(specialties.stream().anyMatch(specialty -> "surgery".equals(specialty.getName())));
        assertTrue(specialties.stream().anyMatch(specialty -> "dentistry".equals(specialty.getName())));

        CasoLog.fila(3, "especialidades de vet", "vet_id=" + VET_ID,
                specialties.size() + ": "
                        + specialties.stream().map(SpecialtyDTO::getName).collect(Collectors.joining(", ")));
    }

    @Test
    @Order(4)
    @DisplayName("CASO 6: buscar veterinarios de una especialidad")
    public void testFindVetsBySpecialty() throws SpecialtyNotFoundException {

        final Integer SPECIALTY_ID = 1;

        List<VetDTO> vets = this.vetSpecialtyService.findVetsBySpecialtyId(SPECIALTY_ID);

        assertEquals(2, vets.size());
        assertTrue(vets.stream().anyMatch(vet -> "Leary".equals(vet.getLastName())));
        assertTrue(vets.stream().anyMatch(vet -> "Stevens".equals(vet.getLastName())));

        CasoLog.fila(4, "veterinarios de espec.", "specialty_id=" + SPECIALTY_ID,
                vets.size() + ": "
                        + vets.stream().map(VetDTO::getLastName).collect(Collectors.joining(", ")));
    }

    @Test
    @Order(5)
    @DisplayName("CASO 6: eliminar la relacion veterinario-especialidad")
    public void testRemoveSpecialtyFromVet() throws VetNotFoundException, SpecialtyNotFoundException,
            VetSpecialtyNotFoundException {

        VetSpecialtyDTO vetSpecialtyDTO = VetSpecialtyDTO.builder()
                .vetId(6)
                .specialtyId(3)
                .certificationDate(LocalDate.of(2021, 8, 1))
                .yearsExperience(2)
                .isPrimary(false)
                .notes("Registro a eliminar")
                .build();

        this.vetSpecialtyService.assign(vetSpecialtyDTO);
        this.vetSpecialtyService.remove(6, 3);

        assertThrows(VetSpecialtyNotFoundException.class,
                () -> this.vetSpecialtyService.findById(6, 3));

        CasoLog.fila(5, "eliminar asignacion", "vet 6 | spec 3",
                "ya no existe (VetSpecialtyNotFoundException)");
    }

    @Test
    @Order(6)
    @DisplayName("CASO 6: relacion inexistente lanza excepcion")
    public void testRemoveVetSpecialty_NotFound() {

        assertThrows(VetSpecialtyNotFoundException.class,
                () -> this.vetSpecialtyService.remove(9999, 9999));

        CasoLog.fila(6, "eliminar inexistente", "vet 9999 | spec 9999",
                "lanza VetSpecialtyNotFoundException");
    }

    @Test
    @Order(7)
    @DisplayName("CASO 6: buscar relacion por veterinario y especialidad")
    public void testFindVetSpecialtyById() throws VetSpecialtyNotFoundException {

        VetSpecialtyDTO vetSpecialty = this.vetSpecialtyService.findById(2, 1);

        assertNotNull(vetSpecialty);
        assertEquals(2, vetSpecialty.getVetId());
        assertEquals(1, vetSpecialty.getSpecialtyId());
        assertEquals(8, vetSpecialty.getYearsExperience());
        assertTrue(vetSpecialty.getIsPrimary());

        CasoLog.fila(7, "buscar asignacion", "vet 2 | spec 1",
                "experiencia " + vetSpecialty.getYearsExperience() + " | principal=" + vetSpecialty.getIsPrimary());
    }
}
