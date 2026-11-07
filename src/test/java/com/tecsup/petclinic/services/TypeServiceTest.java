package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.util.CasoLog;
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
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@ActiveProfiles("h2")
public class TypeServiceTest {

    @Autowired
    private PetTypeService petTypeService;

    @Test
    @DisplayName("CASO 5: buscar tipo de mascota por id")
    public void testFindTypeById() throws PetTypeNotFoundException {

        final Integer ID = 1;
        final String NAME_EXPECTED = "cat";

        CasoLog.inicio("CASO 5 - TYPES", "Busqueda de un tipo de mascota por id");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("id solicitado", ID);

        PetTypeDTO petType = this.petTypeService.findById(ID);

        CasoLog.seccion("REGISTRO OBTENIDO");
        CasoLog.dato("id", petType.getId());
        CasoLog.dato("name", petType.getName());
        CasoLog.dato("description", petType.getDescription());
        CasoLog.dato("active", petType.getActive());
        CasoLog.dato("size_category", petType.getSizeCategory());
        CasoLog.dato("average_lifespan", petType.getAverageLifespan());
        CasoLog.dato("care_level", petType.getCareLevel());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("id", ID, petType.getId());
        CasoLog.verificacion("name", NAME_EXPECTED, petType.getName());
        CasoLog.verificacion("size_category", "small", petType.getSizeCategory());
        CasoLog.verificacion("average_lifespan", 15, petType.getAverageLifespan());
        CasoLog.verificacion("active", true, petType.getActive());

        assertNotNull(petType);
        assertEquals(ID, petType.getId());
        assertEquals(NAME_EXPECTED, petType.getName());
        assertEquals("small", petType.getSizeCategory());
        assertEquals(15, petType.getAverageLifespan());
        assertTrue(petType.getActive());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 5: buscar tipo de mascota por nombre")
    public void testFindTypeByName() {

        final String NAME = "dog";
        final Integer LIFESPAN_EXPECTED = 12;

        CasoLog.inicio("CASO 5 - TYPES", "Busqueda de tipos de mascota por nombre");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("name buscado", NAME);

        List<PetTypeDTO> petTypes = this.petTypeService.findByName(NAME);

        CasoLog.seccion("REGISTROS OBTENIDOS");
        CasoLog.dato("cantidad encontrada", petTypes.size());
        for (int i = 0; i < petTypes.size(); i++) {
            CasoLog.dato("registro " + (i + 1),
                    petTypes.get(i).getId() + " | " + petTypes.get(i).getName()
                            + " | " + petTypes.get(i).getSizeCategory()
                            + " | vida " + petTypes.get(i).getAverageLifespan() + " años"
                            + " | cuidado " + petTypes.get(i).getCareLevel());
        }

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("cantidad", 1, petTypes.size());
        CasoLog.verificacion("name", NAME,
                petTypes.isEmpty() ? null : petTypes.get(0).getName());
        CasoLog.verificacion("average_lifespan", LIFESPAN_EXPECTED,
                petTypes.isEmpty() ? null : petTypes.get(0).getAverageLifespan());

        assertEquals(1, petTypes.size());
        assertEquals(NAME, petTypes.get(0).getName());
        assertEquals(LIFESPAN_EXPECTED, petTypes.get(0).getAverageLifespan());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 5: listar solo tipos activos")
    public void testFindActiveTypes() {

        CasoLog.inicio("CASO 5 - TYPES", "Listado de tipos de mascota con active = true");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("filtro", "active = true");

        List<PetTypeDTO> petTypes = this.petTypeService.findActive();

        CasoLog.seccion("TIPOS ACTIVOS OBTENIDOS");
        CasoLog.dato("cantidad", petTypes.size());
        petTypes.forEach(petType -> CasoLog.dato("tipo",
                petType.getId() + " | " + petType.getName() + " | active=" + petType.getActive()));

        CasoLog.seccion("VERIFICACION");
        CasoLog.ok("todos los tipos tienen active = true");
        CasoLog.ok("la lista no contiene a snake (inactivo)");

        assertTrue(petTypes.size() >= 6);
        assertTrue(petTypes.stream().allMatch(PetTypeDTO::getActive));
        assertFalse(petTypes.stream().anyMatch(petType -> "snake".equals(petType.getName())));

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 5: crear tipo de mascota")
    public void testCreateType() {

        final String NAME = "parrot";
        final String DESCRIPTION = "Domestic bird";
        final String SIZE_CATEGORY = "small";
        final Integer AVERAGE_LIFESPAN = 40;
        final String CARE_LEVEL = "high";

        CasoLog.inicio("CASO 5 - TYPES", "Creacion de un tipo de mascota (INSERT)");
        CasoLog.seccion("DATOS ENVIADOS AL SERVICIO");
        CasoLog.dato("name", NAME);
        CasoLog.dato("description", DESCRIPTION);
        CasoLog.dato("active", true);
        CasoLog.dato("size_category", SIZE_CATEGORY);
        CasoLog.dato("average_lifespan", AVERAGE_LIFESPAN);
        CasoLog.dato("care_level", CARE_LEVEL);

        PetTypeDTO petTypeDTO = PetTypeDTO.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .active(true)
                .sizeCategory(SIZE_CATEGORY)
                .averageLifespan(AVERAGE_LIFESPAN)
                .careLevel(CARE_LEVEL)
                .build();

        PetTypeDTO newPetTypeDTO = this.petTypeService.create(petTypeDTO);

        CasoLog.seccion("REGISTRO GUARDADO EN LA BASE DE DATOS");
        CasoLog.dato("id generado", newPetTypeDTO.getId());
        CasoLog.dato("name", newPetTypeDTO.getName());
        CasoLog.dato("description", newPetTypeDTO.getDescription());
        CasoLog.dato("active", newPetTypeDTO.getActive());
        CasoLog.dato("size_category", newPetTypeDTO.getSizeCategory());
        CasoLog.dato("average_lifespan", newPetTypeDTO.getAverageLifespan());
        CasoLog.dato("care_level", newPetTypeDTO.getCareLevel());

        CasoLog.seccion("VERIFICACION");
        CasoLog.ok("la base de datos genero el id " + newPetTypeDTO.getId());
        CasoLog.verificacion("name", NAME, newPetTypeDTO.getName());
        CasoLog.verificacion("size_category", SIZE_CATEGORY, newPetTypeDTO.getSizeCategory());
        CasoLog.verificacion("average_lifespan", AVERAGE_LIFESPAN, newPetTypeDTO.getAverageLifespan());
        CasoLog.verificacion("care_level", CARE_LEVEL, newPetTypeDTO.getCareLevel());

        assertNotNull(newPetTypeDTO.getId());
        assertEquals(NAME, newPetTypeDTO.getName());
        assertEquals(DESCRIPTION, newPetTypeDTO.getDescription());
        assertEquals(SIZE_CATEGORY, newPetTypeDTO.getSizeCategory());
        assertEquals(AVERAGE_LIFESPAN, newPetTypeDTO.getAverageLifespan());
        assertEquals(CARE_LEVEL, newPetTypeDTO.getCareLevel());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 5: actualizar tipo de mascota")
    public void testUpdateType() {

        final String UP_NAME = "ferret";
        final String UP_CARE_LEVEL = "medium";
        final Integer UP_AVERAGE_LIFESPAN = 8;

        CasoLog.inicio("CASO 5 - TYPES", "Actualizacion de un tipo de mascota (UPDATE)");
        CasoLog.seccion("DATOS PARA CREAR EL REGISTRO");
        CasoLog.dato("name", "gerbil");
        CasoLog.dato("description", "Small rodent");
        CasoLog.dato("size_category", "small");
        CasoLog.dato("average_lifespan", 4);
        CasoLog.dato("care_level", "low");

        PetTypeDTO petTypeDTO = PetTypeDTO.builder()
                .name("gerbil")
                .description("Small rodent")
                .active(true)
                .sizeCategory("small")
                .averageLifespan(4)
                .careLevel("low")
                .build();

        PetTypeDTO petTypeDTOCreated = this.petTypeService.create(petTypeDTO);

        CasoLog.seccion("REGISTRO CREADO");
        CasoLog.dato("id", petTypeDTOCreated.getId());
        CasoLog.dato("name anterior", petTypeDTOCreated.getName());
        CasoLog.dato("average_lifespan anterior", petTypeDTOCreated.getAverageLifespan());
        CasoLog.dato("care_level anterior", petTypeDTOCreated.getCareLevel());

        Integer createdId = petTypeDTOCreated.getId();
        petTypeDTOCreated.setName(UP_NAME);
        petTypeDTOCreated.setCareLevel(UP_CARE_LEVEL);
        petTypeDTOCreated.setAverageLifespan(UP_AVERAGE_LIFESPAN);

        CasoLog.seccion("DATOS ENVIADOS EN LA ACTUALIZACION");
        CasoLog.dato("id", createdId);
        CasoLog.dato("name nuevo", UP_NAME);
        CasoLog.dato("average_lifespan nuevo", UP_AVERAGE_LIFESPAN);
        CasoLog.dato("care_level nuevo", UP_CARE_LEVEL);

        PetTypeDTO petTypeDTOUpdated = this.petTypeService.update(petTypeDTOCreated);

        CasoLog.seccion("REGISTRO ACTUALIZADO");
        CasoLog.dato("id", petTypeDTOUpdated.getId());
        CasoLog.dato("name", petTypeDTOUpdated.getName());
        CasoLog.dato("average_lifespan", petTypeDTOUpdated.getAverageLifespan());
        CasoLog.dato("care_level", petTypeDTOUpdated.getCareLevel());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("id", createdId, petTypeDTOUpdated.getId());
        CasoLog.verificacion("name", UP_NAME, petTypeDTOUpdated.getName());
        CasoLog.verificacion("average_lifespan", UP_AVERAGE_LIFESPAN, petTypeDTOUpdated.getAverageLifespan());
        CasoLog.verificacion("care_level", UP_CARE_LEVEL, petTypeDTOUpdated.getCareLevel());

        assertEquals(createdId, petTypeDTOUpdated.getId());
        assertEquals(UP_NAME, petTypeDTOUpdated.getName());
        assertEquals(UP_CARE_LEVEL, petTypeDTOUpdated.getCareLevel());
        assertEquals(UP_AVERAGE_LIFESPAN, petTypeDTOUpdated.getAverageLifespan());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 5: eliminar tipo de mascota")
    public void testDeleteType() throws PetTypeNotFoundException {

        CasoLog.inicio("CASO 5 - TYPES", "Eliminacion de un tipo de mascota (DELETE)");
        CasoLog.seccion("DATOS PARA CREAR EL REGISTRO A ELIMINAR");
        CasoLog.dato("name", "chinchilla");
        CasoLog.dato("description", "Small rodent");
        CasoLog.dato("size_category", "small");
        CasoLog.dato("average_lifespan", 10);
        CasoLog.dato("care_level", "medium");

        PetTypeDTO petTypeDTO = PetTypeDTO.builder()
                .name("chinchilla")
                .description("Small rodent")
                .active(true)
                .sizeCategory("small")
                .averageLifespan(10)
                .careLevel("medium")
                .build();

        PetTypeDTO newPetTypeDTO = this.petTypeService.create(petTypeDTO);

        CasoLog.seccion("REGISTRO CREADO");
        CasoLog.dato("id", newPetTypeDTO.getId());
        CasoLog.dato("name", newPetTypeDTO.getName());

        this.petTypeService.delete(newPetTypeDTO.getId());

        CasoLog.seccion("VERIFICACION DESPUES DEL DELETE");
        CasoLog.dato("id eliminado", newPetTypeDTO.getId());

        PetTypeNotFoundException exception = assertThrows(PetTypeNotFoundException.class,
                () -> this.petTypeService.findById(newPetTypeDTO.getId()));

        CasoLog.dato("excepcion lanzada", exception.getClass().getSimpleName());
        CasoLog.dato("mensaje", exception.getMessage());
        CasoLog.verificacion("tipo de excepcion", "PetTypeNotFoundException",
                exception.getClass().getSimpleName());
        CasoLog.ok("el tipo de mascota eliminado ya no se puede recuperar");

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 5: tipo inexistente lanza excepcion")
    public void testFindTypeById_NotFound() {

        final Integer ID = 9999;

        CasoLog.inicio("CASO 5 - TYPES", "Busqueda de un tipo de mascota que no existe");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("id solicitado", ID);

        PetTypeNotFoundException exception = assertThrows(PetTypeNotFoundException.class,
                () -> this.petTypeService.findById(ID));

        CasoLog.seccion("RESULTADO DE LA BUSQUEDA");
        CasoLog.dato("excepcion lanzada", exception.getClass().getSimpleName());
        CasoLog.dato("mensaje", exception.getMessage());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("tipo de excepcion", "PetTypeNotFoundException",
                exception.getClass().getSimpleName());

        CasoLog.fin();
    }
}
