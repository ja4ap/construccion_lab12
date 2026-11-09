package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// CASO 1 - Pruebas unitarias de la tabla vets
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@ActiveProfiles("h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VetServiceTest {

    @Autowired
    private VetService vetService;

    @BeforeAll
    public void inicio() {
        CasoLog.caso("CASO 1 - VETS", "vets");
    }

    @Test
    @Order(1)
    @DisplayName("CASO 1: buscar veterinario por id")
    public void testFindVetById() throws VetNotFoundException {

        final Integer ID = 1;

        VetDTO vet = this.vetService.findById(ID);

        assertNotNull(vet);
        assertEquals(ID, vet.getId());
        assertEquals("James", vet.getFirstName());
        assertEquals("Carter", vet.getLastName());
        assertTrue(vet.getActive());

        CasoLog.fila(1, "buscar por id", "id=" + ID,
                vet.getFirstName() + " " + vet.getLastName() + " | active=" + vet.getActive());
    }

    @Test
    @Order(2)
    @DisplayName("CASO 1: buscar veterinarios por apellido")
    public void testFindVetByLastName() {

        final String LAST_NAME = "Leary";

        List<VetDTO> vets = this.vetService.findByLastName(LAST_NAME);

        assertEquals(1, vets.size());
        assertEquals("Helen", vets.get(0).getFirstName());
        assertEquals(LAST_NAME, vets.get(0).getLastName());

        CasoLog.fila(2, "buscar por apellido", "last_name=" + LAST_NAME,
                vets.size() + " registro: " + vets.get(0).getFirstName() + " " + vets.get(0).getLastName());
    }

    @Test
    @Order(3)
    @DisplayName("CASO 1: listar solo veterinarios activos")
    public void testFindActiveVets() {

        List<VetDTO> vets = this.vetService.findActive();

        assertTrue(vets.size() >= 5);
        assertTrue(vets.stream().allMatch(VetDTO::getActive));
        assertTrue(vets.stream().anyMatch(vet -> "Carter".equals(vet.getLastName())));
        assertFalse(vets.stream().anyMatch(vet -> "Jenkins".equals(vet.getLastName())));

        CasoLog.fila(3, "listar activos", "active=true",
                vets.size() + " registros | Jenkins excluido");
    }

    @Test
    @Order(4)
    @DisplayName("CASO 1: crear veterinario")
    public void testCreateVet() {

        VetDTO vetDTO = VetDTO.builder()
                .firstName("Jose")
                .lastName("Paredes")
                .email("jose.paredes@petclinic.com")
                .phone("987654321")
                .active(true)
                .build();

        VetDTO newVetDTO = this.vetService.create(vetDTO);

        assertNotNull(newVetDTO.getId());
        assertEquals("Jose", newVetDTO.getFirstName());
        assertEquals("Paredes", newVetDTO.getLastName());
        assertTrue(newVetDTO.getActive());

        CasoLog.fila(4, "crear", newVetDTO.getFirstName() + " " + newVetDTO.getLastName(),
                "id=" + newVetDTO.getId() + " generado");
    }

    @Test
    @Order(5)
    @DisplayName("CASO 1: actualizar veterinario")
    public void testUpdateVet() {

        VetDTO vetDTO = VetDTO.builder()
                .firstName("Carlos")
                .lastName("Ramirez")
                .email("carlos.ramirez@petclinic.com")
                .phone("959595959")
                .active(true)
                .build();

        VetDTO creado = this.vetService.create(vetDTO);
        Integer id = creado.getId();

        creado.setFirstName("Jose Antonio");
        creado.setLastName("Paredes Huaracha");
        creado.setPhone("999888777");

        VetDTO actualizado = this.vetService.update(creado);

        assertEquals(id, actualizado.getId());
        assertEquals("Jose Antonio", actualizado.getFirstName());
        assertEquals("Paredes Huaracha", actualizado.getLastName());
        assertEquals("999888777", actualizado.getPhone());

        CasoLog.fila(5, "actualizar", "id=" + id,
                actualizado.getFirstName() + " " + actualizado.getLastName()
                        + " | phone " + actualizado.getPhone());
    }

    @Test
    @Order(6)
    @DisplayName("CASO 1: eliminar veterinario")
    public void testDeleteVet() throws VetNotFoundException {

        VetDTO vetDTO = VetDTO.builder()
                .firstName("Lucia")
                .lastName("Torres")
                .email("lucia.torres@petclinic.com")
                .phone("912345678")
                .active(true)
                .build();

        VetDTO creado = this.vetService.create(vetDTO);

        this.vetService.delete(creado.getId());

        assertThrows(VetNotFoundException.class, () -> this.vetService.findById(creado.getId()));

        CasoLog.fila(6, "eliminar", "id=" + creado.getId(),
                "ya no existe (VetNotFoundException)");
    }

    @Test
    @Order(7)
    @DisplayName("CASO 1: veterinario inexistente lanza excepcion")
    public void testFindVetById_NotFound() {

        final Integer ID = 9999;

        assertThrows(VetNotFoundException.class, () -> this.vetService.findById(ID));

        CasoLog.fila(7, "buscar inexistente", "id=" + ID, "lanza VetNotFoundException");
    }
}
