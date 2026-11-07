package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
@SpringBootTest
@ActiveProfiles("h2")
@Slf4j
public class VetServiceTest {

    @Autowired
    private VetService vetService;

    @Test
    @DisplayName("CASO 1: buscar veterinario por id")
    public void testFindVetById() throws VetNotFoundException {

        final Integer ID = 1;
        final String FIRST_NAME_EXPECTED = "James";
        final String LAST_NAME_EXPECTED = "Carter";

        VetDTO vet = this.vetService.findById(ID);

        log.info("VET FOUND : {}", vet);

        assertNotNull(vet);
        assertEquals(ID, vet.getId());
        assertEquals(FIRST_NAME_EXPECTED, vet.getFirstName());
        assertEquals(LAST_NAME_EXPECTED, vet.getLastName());
        assertTrue(vet.getActive());
    }

    @Test
    @DisplayName("CASO 1: buscar veterinarios por apellido")
    public void testFindVetByLastName() {

        final String LAST_NAME = "Leary";
        final String FIRST_NAME_EXPECTED = "Helen";

        List<VetDTO> vets = this.vetService.findByLastName(LAST_NAME);

        log.info("VETS FOUND : {}", vets.size());

        assertEquals(1, vets.size());
        assertEquals(FIRST_NAME_EXPECTED, vets.get(0).getFirstName());
    }

    @Test
    @DisplayName("CASO 1: listar solo veterinarios activos")
    public void testFindActiveVets() {

        List<VetDTO> vets = this.vetService.findActive();

        log.info("ACTIVE VETS : {}", vets.size());

        assertTrue(vets.size() >= 5);
        assertTrue(vets.stream().allMatch(VetDTO::getActive));
        assertTrue(vets.stream().anyMatch(vet -> "Carter".equals(vet.getLastName())));
        assertFalse(vets.stream().anyMatch(vet -> "Jenkins".equals(vet.getLastName())));
    }

    @Test
    @DisplayName("CASO 1: crear veterinario")
    public void testCreateVet() {

        final String FIRST_NAME = "Jose";
        final String LAST_NAME = "Paredes";
        final String EMAIL = "jose.paredes@petclinic.com";
        final String PHONE = "987654321";

        VetDTO vetDTO = VetDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .phone(PHONE)
                .active(true)
                .build();

        VetDTO newVetDTO = this.vetService.create(vetDTO);

        log.info("VET CREATED : {}", newVetDTO);

        assertNotNull(newVetDTO.getId());
        assertEquals(FIRST_NAME, newVetDTO.getFirstName());
        assertEquals(LAST_NAME, newVetDTO.getLastName());
        assertEquals(EMAIL, newVetDTO.getEmail());
        assertEquals(PHONE, newVetDTO.getPhone());
        assertTrue(newVetDTO.getActive());
    }

    @Test
    @DisplayName("CASO 1: actualizar veterinario")
    public void testUpdateVet() throws VetNotFoundException {

        final String UP_FIRST_NAME = "Jose Antonio";
        final String UP_LAST_NAME = "Paredes Huaracha";
        final String UP_PHONE = "999888777";

        VetDTO vetDTO = VetDTO.builder()
                .firstName("Carlos")
                .lastName("Ramirez")
                .email("carlos.ramirez@petclinic.com")
                .phone("959595959")
                .active(true)
                .build();

        VetDTO vetDTOCreated = this.vetService.create(vetDTO);
        log.info("VET CREATED : {}", vetDTOCreated);

        Integer createdId = vetDTOCreated.getId();
        vetDTOCreated.setFirstName(UP_FIRST_NAME);
        vetDTOCreated.setLastName(UP_LAST_NAME);
        vetDTOCreated.setPhone(UP_PHONE);

        VetDTO vetDTOUpdated = this.vetService.update(vetDTOCreated);
        log.info("VET UPDATED : {}", vetDTOUpdated);

        assertEquals(createdId, vetDTOUpdated.getId());
        assertEquals(UP_FIRST_NAME, vetDTOUpdated.getFirstName());
        assertEquals(UP_LAST_NAME, vetDTOUpdated.getLastName());
        assertEquals(UP_PHONE, vetDTOUpdated.getPhone());
    }

    @Test
    @DisplayName("CASO 1: eliminar veterinario")
    public void testDeleteVet() throws VetNotFoundException {

        VetDTO vetDTO = VetDTO.builder()
                .firstName("Lucia")
                .lastName("Torres")
                .email("lucia.torres@petclinic.com")
                .phone("912345678")
                .active(true)
                .build();

        VetDTO newVetDTO = this.vetService.create(vetDTO);
        log.info("VET TO DELETE : {}", newVetDTO);

        this.vetService.delete(newVetDTO.getId());

        assertThrows(VetNotFoundException.class,
                () -> this.vetService.findById(newVetDTO.getId()));
    }

    @Test
    @DisplayName("CASO 1: veterinario inexistente lanza excepcion")
    public void testFindVetById_NotFound() {

        final Integer ID = 9999;

        assertThrows(VetNotFoundException.class, () -> this.vetService.findById(ID));
    }
}
