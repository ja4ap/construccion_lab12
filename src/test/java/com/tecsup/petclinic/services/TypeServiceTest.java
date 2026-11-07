package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
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

// CASO 5 - Pruebas unitarias de la tabla types
@SpringBootTest
@ActiveProfiles("h2")
@Slf4j
public class TypeServiceTest {

    @Autowired
    private PetTypeService petTypeService;

    @Test
    @DisplayName("CASO 5: buscar tipo de mascota por id")
    public void testFindTypeById() throws PetTypeNotFoundException {

        final Integer ID = 1;
        final String NAME_EXPECTED = "cat";

        PetTypeDTO petType = this.petTypeService.findById(ID);

        log.info("TYPE FOUND : {}", petType);

        assertNotNull(petType);
        assertEquals(ID, petType.getId());
        assertEquals(NAME_EXPECTED, petType.getName());
        assertEquals("small", petType.getSizeCategory());
        assertEquals(15, petType.getAverageLifespan());
        assertTrue(petType.getActive());
    }

    @Test
    @DisplayName("CASO 5: buscar tipo de mascota por nombre")
    public void testFindTypeByName() {

        final String NAME = "dog";
        final Integer LIFESPAN_EXPECTED = 12;

        List<PetTypeDTO> petTypes = this.petTypeService.findByName(NAME);

        log.info("TYPES FOUND : {}", petTypes.size());

        assertEquals(1, petTypes.size());
        assertEquals(NAME, petTypes.get(0).getName());
        assertEquals(LIFESPAN_EXPECTED, petTypes.get(0).getAverageLifespan());
    }

    @Test
    @DisplayName("CASO 5: listar solo tipos activos")
    public void testFindActiveTypes() {

        List<PetTypeDTO> petTypes = this.petTypeService.findActive();

        log.info("ACTIVE TYPES : {}", petTypes.size());

        assertTrue(petTypes.size() >= 6);
        assertTrue(petTypes.stream().allMatch(PetTypeDTO::getActive));
        assertFalse(petTypes.stream().anyMatch(petType -> "snake".equals(petType.getName())));
    }

    @Test
    @DisplayName("CASO 5: crear tipo de mascota")
    public void testCreateType() {

        final String NAME = "parrot";
        final String DESCRIPTION = "Domestic bird";
        final String SIZE_CATEGORY = "small";
        final Integer AVERAGE_LIFESPAN = 40;
        final String CARE_LEVEL = "high";

        PetTypeDTO petTypeDTO = PetTypeDTO.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .active(true)
                .sizeCategory(SIZE_CATEGORY)
                .averageLifespan(AVERAGE_LIFESPAN)
                .careLevel(CARE_LEVEL)
                .build();

        PetTypeDTO newPetTypeDTO = this.petTypeService.create(petTypeDTO);

        log.info("TYPE CREATED : {}", newPetTypeDTO);

        assertNotNull(newPetTypeDTO.getId());
        assertEquals(NAME, newPetTypeDTO.getName());
        assertEquals(DESCRIPTION, newPetTypeDTO.getDescription());
        assertEquals(SIZE_CATEGORY, newPetTypeDTO.getSizeCategory());
        assertEquals(AVERAGE_LIFESPAN, newPetTypeDTO.getAverageLifespan());
        assertEquals(CARE_LEVEL, newPetTypeDTO.getCareLevel());
    }

    @Test
    @DisplayName("CASO 5: actualizar tipo de mascota")
    public void testUpdateType() throws PetTypeNotFoundException {

        final String UP_NAME = "ferret";
        final String UP_CARE_LEVEL = "medium";
        final Integer UP_AVERAGE_LIFESPAN = 8;

        PetTypeDTO petTypeDTO = PetTypeDTO.builder()
                .name("gerbil")
                .description("Small rodent")
                .active(true)
                .sizeCategory("small")
                .averageLifespan(4)
                .careLevel("low")
                .build();

        PetTypeDTO petTypeDTOCreated = this.petTypeService.create(petTypeDTO);
        log.info("TYPE CREATED : {}", petTypeDTOCreated);

        Integer createdId = petTypeDTOCreated.getId();
        petTypeDTOCreated.setName(UP_NAME);
        petTypeDTOCreated.setCareLevel(UP_CARE_LEVEL);
        petTypeDTOCreated.setAverageLifespan(UP_AVERAGE_LIFESPAN);

        PetTypeDTO petTypeDTOUpdated = this.petTypeService.update(petTypeDTOCreated);
        log.info("TYPE UPDATED : {}", petTypeDTOUpdated);

        assertEquals(createdId, petTypeDTOUpdated.getId());
        assertEquals(UP_NAME, petTypeDTOUpdated.getName());
        assertEquals(UP_CARE_LEVEL, petTypeDTOUpdated.getCareLevel());
        assertEquals(UP_AVERAGE_LIFESPAN, petTypeDTOUpdated.getAverageLifespan());
    }

    @Test
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

        PetTypeDTO newPetTypeDTO = this.petTypeService.create(petTypeDTO);
        log.info("TYPE TO DELETE : {}", newPetTypeDTO);

        this.petTypeService.delete(newPetTypeDTO.getId());

        assertThrows(PetTypeNotFoundException.class,
                () -> this.petTypeService.findById(newPetTypeDTO.getId()));
    }

    @Test
    @DisplayName("CASO 5: tipo inexistente lanza excepcion")
    public void testFindTypeById_NotFound() {

        final Integer ID = 9999;

        assertThrows(PetTypeNotFoundException.class, () -> this.petTypeService.findById(ID));
    }
}
