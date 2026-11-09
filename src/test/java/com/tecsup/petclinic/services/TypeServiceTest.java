package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
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

// CASO 5 - Pruebas unitarias de la tabla types
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@ActiveProfiles("h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TypeServiceTest {

    @Autowired
    private PetTypeService petTypeService;

    @BeforeAll
    public void inicio() {
        CasoLog.caso("CASO 5 - TYPES", "types");
    }

    @Test
    @Order(1)
    @DisplayName("CASO 5: buscar tipo de mascota por id")
    public void testFindTypeById() throws PetTypeNotFoundException {

        final Integer ID = 1;

        PetTypeDTO petType = this.petTypeService.findById(ID);

        assertNotNull(petType);
        assertEquals(ID, petType.getId());
        assertEquals("cat", petType.getName());
        assertEquals("small", petType.getSizeCategory());
        assertEquals(15, petType.getAverageLifespan());
        assertTrue(petType.getActive());

        CasoLog.fila(1, "buscar por id", "id=" + ID,
                petType.getName() + " | " + petType.getSizeCategory()
                        + " | vida " + petType.getAverageLifespan());
    }

    @Test
    @Order(2)
    @DisplayName("CASO 5: buscar tipo de mascota por nombre")
    public void testFindTypeByName() {

        final String NAME = "dog";

        List<PetTypeDTO> petTypes = this.petTypeService.findByName(NAME);

        assertEquals(1, petTypes.size());
        assertEquals(NAME, petTypes.get(0).getName());
        assertEquals(12, petTypes.get(0).getAverageLifespan());

        CasoLog.fila(2, "buscar por nombre", "name=" + NAME,
                petTypes.size() + " registro | vida " + petTypes.get(0).getAverageLifespan());
    }

    @Test
    @Order(3)
    @DisplayName("CASO 5: listar solo tipos activos")
    public void testFindActiveTypes() {

        List<PetTypeDTO> petTypes = this.petTypeService.findActive();

        assertTrue(petTypes.size() >= 6);
        assertTrue(petTypes.stream().allMatch(PetTypeDTO::getActive));
        assertFalse(petTypes.stream().anyMatch(petType -> "snake".equals(petType.getName())));

        CasoLog.fila(3, "listar activos", "active=true",
                petTypes.size() + " tipos | snake excluido");
    }

    @Test
    @Order(4)
    @DisplayName("CASO 5: crear tipo de mascota")
    public void testCreateType() {

        PetTypeDTO petTypeDTO = PetTypeDTO.builder()
                .name("parrot")
                .description("Domestic bird")
                .active(true)
                .sizeCategory("small")
                .averageLifespan(40)
                .careLevel("high")
                .build();

        PetTypeDTO newPetTypeDTO = this.petTypeService.create(petTypeDTO);

        assertNotNull(newPetTypeDTO.getId());
        assertEquals("parrot", newPetTypeDTO.getName());
        assertEquals(40, newPetTypeDTO.getAverageLifespan());
        assertEquals("high", newPetTypeDTO.getCareLevel());

        CasoLog.fila(4, "crear", newPetTypeDTO.getName() + " | vida " + newPetTypeDTO.getAverageLifespan(),
                "id=" + newPetTypeDTO.getId() + " generado");
    }

    @Test
    @Order(5)
    @DisplayName("CASO 5: actualizar tipo de mascota")
    public void testUpdateType() {

        PetTypeDTO petTypeDTO = PetTypeDTO.builder()
                .name("gerbil")
                .description("Small rodent")
                .active(true)
                .sizeCategory("small")
                .averageLifespan(4)
                .careLevel("low")
                .build();

        PetTypeDTO creado = this.petTypeService.create(petTypeDTO);
        Integer id = creado.getId();

        creado.setName("ferret");
        creado.setCareLevel("medium");
        creado.setAverageLifespan(8);

        PetTypeDTO actualizado = this.petTypeService.update(creado);

        assertEquals(id, actualizado.getId());
        assertEquals("ferret", actualizado.getName());
        assertEquals("medium", actualizado.getCareLevel());
        assertEquals(8, actualizado.getAverageLifespan());

        CasoLog.fila(5, "actualizar", "id=" + id,
                actualizado.getName() + " | vida " + actualizado.getAverageLifespan()
                        + " | " + actualizado.getCareLevel());
    }

    @Test
    @Order(6)
    @DisplayName("CASO 5: eliminar tipo de mascota")
    public void testDeleteType() throws PetTypeNotFoundException {

        PetTypeDTO petTypeDTO = PetTypeDTO.builder()
                .name("chinchilla")
                .description("Small rodent")
                .active(true)
                .sizeCategory("small")
                .averageLifespan(10)
                .careLevel("medium")
                .build();

        PetTypeDTO creado = this.petTypeService.create(petTypeDTO);

        this.petTypeService.delete(creado.getId());

        assertThrows(PetTypeNotFoundException.class, () -> this.petTypeService.findById(creado.getId()));

        CasoLog.fila(6, "eliminar", "id=" + creado.getId(),
                "ya no existe (PetTypeNotFoundException)");
    }

    @Test
    @Order(7)
    @DisplayName("CASO 5: tipo inexistente lanza excepcion")
    public void testFindTypeById_NotFound() {

        final Integer ID = 9999;

        assertThrows(PetTypeNotFoundException.class, () -> this.petTypeService.findById(ID));

        CasoLog.fila(7, "buscar inexistente", "id=" + ID, "lanza PetTypeNotFoundException");
    }
}
