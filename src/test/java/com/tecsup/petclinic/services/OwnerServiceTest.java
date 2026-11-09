package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
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

// CASO 2 - Pruebas unitarias de la tabla owners
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@ActiveProfiles("h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    @BeforeAll
    public void inicio() {
        CasoLog.caso("CASO 2 - OWNERS", "owners");
    }

    @Test
    @Order(1)
    @DisplayName("CASO 2: buscar dueño por id")
    public void testFindOwnerById() throws OwnerNotFoundException {

        final Integer ID = 1;

        OwnerDTO owner = this.ownerService.findById(ID);

        assertNotNull(owner);
        assertEquals(ID, owner.getId());
        assertEquals("George", owner.getFirstName());
        assertEquals("Franklin", owner.getLastName());

        CasoLog.fila(1, "buscar por id", "id=" + ID,
                owner.getFirstName() + " " + owner.getLastName() + " | " + owner.getCity());
    }

    @Test
    @Order(2)
    @DisplayName("CASO 2: buscar dueños por apellido")
    public void testFindOwnerByLastName() {

        final String LAST_NAME = "Davis";

        List<OwnerDTO> owners = this.ownerService.findByLastName(LAST_NAME);

        assertEquals(2, owners.size());

        CasoLog.fila(2, "buscar por apellido", "last_name=" + LAST_NAME,
                owners.size() + " registros");
    }

    @Test
    @Order(3)
    @DisplayName("CASO 2: buscar dueños por ciudad")
    public void testFindOwnerByCity() {

        final String CITY = "Monona";

        List<OwnerDTO> owners = this.ownerService.findByCity(CITY);

        assertEquals(2, owners.size());

        CasoLog.fila(3, "buscar por ciudad", "city=" + CITY, owners.size() + " registros");
    }

    @Test
    @Order(4)
    @DisplayName("CASO 2: crear dueño")
    public void testCreateOwner() {

        OwnerDTO ownerDTO = OwnerDTO.builder()
                .firstName("Jose")
                .lastName("Paredes")
                .address("Av. Cascanueces 2221")
                .city("Lima")
                .telephone("987654321")
                .build();

        OwnerDTO newOwnerDTO = this.ownerService.create(ownerDTO);

        assertNotNull(newOwnerDTO.getId());
        assertEquals("Jose", newOwnerDTO.getFirstName());
        assertEquals("Paredes", newOwnerDTO.getLastName());
        assertEquals("Lima", newOwnerDTO.getCity());

        CasoLog.fila(4, "crear", newOwnerDTO.getFirstName() + " " + newOwnerDTO.getLastName()
                + " | " + newOwnerDTO.getCity(), "id=" + newOwnerDTO.getId() + " generado");
    }

    @Test
    @Order(5)
    @DisplayName("CASO 2: actualizar dueño")
    public void testUpdateOwner() {

        OwnerDTO ownerDTO = OwnerDTO.builder()
                .firstName("Carlos")
                .lastName("Ramirez")
                .address("Calle Mercaderes 123")
                .city("Arequipa")
                .telephone("959595959")
                .build();

        OwnerDTO creado = this.ownerService.create(ownerDTO);
        Integer id = creado.getId();

        creado.setFirstName("Carlos Alberto");
        creado.setCity("Cusco");
        creado.setTelephone("999888777");

        OwnerDTO actualizado = this.ownerService.update(creado);

        assertEquals(id, actualizado.getId());
        assertEquals("Carlos Alberto", actualizado.getFirstName());
        assertEquals("Cusco", actualizado.getCity());
        assertEquals("999888777", actualizado.getTelephone());

        CasoLog.fila(5, "actualizar", "id=" + id,
                actualizado.getFirstName() + " | " + actualizado.getCity());
    }

    @Test
    @Order(6)
    @DisplayName("CASO 2: eliminar dueño")
    public void testDeleteOwner() throws OwnerNotFoundException {

        OwnerDTO ownerDTO = OwnerDTO.builder()
                .firstName("Lucia")
                .lastName("Torres")
                .address("Jr. Union 456")
                .city("Trujillo")
                .telephone("912345678")
                .build();

        OwnerDTO creado = this.ownerService.create(ownerDTO);

        this.ownerService.delete(creado.getId());

        assertThrows(OwnerNotFoundException.class, () -> this.ownerService.findById(creado.getId()));

        CasoLog.fila(6, "eliminar", "id=" + creado.getId(),
                "ya no existe (OwnerNotFoundException)");
    }

    @Test
    @Order(7)
    @DisplayName("CASO 2: dueño inexistente lanza excepcion")
    public void testFindOwnerNotFound() {

        final Integer ID = 9999;

        assertThrows(OwnerNotFoundException.class, () -> this.ownerService.findById(ID));

        CasoLog.fila(7, "buscar inexistente", "id=" + ID, "lanza OwnerNotFoundException");
    }
}
