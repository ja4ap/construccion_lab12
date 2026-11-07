package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

// CASO 2 - Pruebas unitarias de la tabla owners
@SpringBootTest
@ActiveProfiles("h2")
@Slf4j
public class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    @Test
    @DisplayName("CASO 2: buscar dueño por id")
    public void testFindOwnerById() throws OwnerNotFoundException {

        final Integer ID = 1;
        final String FIRST_NAME_EXPECTED = "George";

        OwnerDTO owner = this.ownerService.findById(ID);

        log.info("OWNER FOUND : {}", owner);

        assertNotNull(owner);
        assertEquals(ID, owner.getId());
        assertEquals(FIRST_NAME_EXPECTED, owner.getFirstName());
    }

    @Test
    @DisplayName("CASO 2: buscar dueños por apellido")
    public void testFindOwnerByLastName() {

        final String LAST_NAME = "Davis";
        final int SIZE_EXPECTED = 2;

        List<OwnerDTO> owners = this.ownerService.findByLastName(LAST_NAME);

        log.info("OWNERS FOUND : {}", owners.size());

        assertEquals(SIZE_EXPECTED, owners.size());
    }

    @Test
    @DisplayName("CASO 2: buscar dueños por ciudad")
    public void testFindOwnerByCity() {

        final String CITY = "Monona";
        final int SIZE_EXPECTED = 2;

        List<OwnerDTO> owners = this.ownerService.findByCity(CITY);

        log.info("OWNERS FOUND : {}", owners.size());

        assertEquals(SIZE_EXPECTED, owners.size());
    }

    @Test
    @DisplayName("CASO 2: crear dueño")
    public void testCreateOwner() {

        final String FIRST_NAME = "Jose";
        final String LAST_NAME = "Paredes";
        final String ADDRESS = "Av. Cascanueces 2221";
        final String CITY = "Lima";
        final String TELEPHONE = "987654321";

        OwnerDTO ownerDTO = OwnerDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .address(ADDRESS)
                .city(CITY)
                .telephone(TELEPHONE)
                .build();

        OwnerDTO newOwnerDTO = this.ownerService.create(ownerDTO);

        log.info("OWNER CREATED : {}", newOwnerDTO);

        assertNotNull(newOwnerDTO.getId());
        assertEquals(FIRST_NAME, newOwnerDTO.getFirstName());
        assertEquals(LAST_NAME, newOwnerDTO.getLastName());
        assertEquals(CITY, newOwnerDTO.getCity());
    }

    @Test
    @DisplayName("CASO 2: actualizar dueño")
    public void testUpdateOwner() throws OwnerNotFoundException {

        final String UP_FIRST_NAME = "Carlos Alberto";
        final String UP_CITY = "Cusco";
        final String UP_TELEPHONE = "999888777";

        OwnerDTO ownerDTO = OwnerDTO.builder()
                .firstName("Carlos")
                .lastName("Ramirez")
                .address("Calle Mercaderes 123")
                .city("Arequipa")
                .telephone("959595959")
                .build();

        OwnerDTO ownerDTOCreated = this.ownerService.create(ownerDTO);
        log.info("OWNER CREATED : {}", ownerDTOCreated);

        Integer createdId = ownerDTOCreated.getId();
        ownerDTOCreated.setFirstName(UP_FIRST_NAME);
        ownerDTOCreated.setCity(UP_CITY);
        ownerDTOCreated.setTelephone(UP_TELEPHONE);

        OwnerDTO upgradeOwnerDTO = this.ownerService.update(ownerDTOCreated);
        log.info("OWNER UPDATED : {}", upgradeOwnerDTO);

        assertEquals(createdId, upgradeOwnerDTO.getId());
        assertEquals(UP_FIRST_NAME, upgradeOwnerDTO.getFirstName());
        assertEquals(UP_CITY, upgradeOwnerDTO.getCity());
        assertEquals(UP_TELEPHONE, upgradeOwnerDTO.getTelephone());
    }

    @Test
    @DisplayName("CASO 2: eliminar dueño")
    public void testDeleteOwner() throws OwnerNotFoundException {

        OwnerDTO ownerDTO = OwnerDTO.builder()
                .firstName("Lucia")
                .lastName("Torres")
                .address("Jr. Union 456")
                .city("Trujillo")
                .telephone("912345678")
                .build();

        OwnerDTO newOwnerDTO = this.ownerService.create(ownerDTO);
        log.info("OWNER TO DELETE : {}", newOwnerDTO);

        this.ownerService.delete(newOwnerDTO.getId());

        assertThrows(OwnerNotFoundException.class,
                () -> this.ownerService.findById(newOwnerDTO.getId()));
    }

    @Test
    @DisplayName("CASO 2: dueño inexistente lanza excepcion")
    public void testFindOwnerNotFound() {

        final Integer ID = 9999;

        assertThrows(OwnerNotFoundException.class, () -> this.ownerService.findById(ID));
    }
}
