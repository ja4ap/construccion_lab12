package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.util.CasoLog;
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
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@ActiveProfiles("h2")
public class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    @Test
    @DisplayName("CASO 2: buscar dueño por id")
    public void testFindOwnerById() throws OwnerNotFoundException {

        final Integer ID = 1;
        final String FIRST_NAME_EXPECTED = "George";

        CasoLog.inicio("CASO 2 - OWNERS", "Busqueda de un dueño por id");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("id solicitado", ID);

        OwnerDTO owner = this.ownerService.findById(ID);

        CasoLog.seccion("REGISTRO OBTENIDO");
        CasoLog.dato("id", owner.getId());
        CasoLog.dato("first_name", owner.getFirstName());
        CasoLog.dato("last_name", owner.getLastName());
        CasoLog.dato("address", owner.getAddress());
        CasoLog.dato("city", owner.getCity());
        CasoLog.dato("telephone", owner.getTelephone());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("id", ID, owner.getId());
        CasoLog.verificacion("first_name", FIRST_NAME_EXPECTED, owner.getFirstName());

        assertNotNull(owner);
        assertEquals(ID, owner.getId());
        assertEquals(FIRST_NAME_EXPECTED, owner.getFirstName());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 2: buscar dueños por apellido")
    public void testFindOwnerByLastName() {

        final String LAST_NAME = "Davis";
        final int SIZE_EXPECTED = 2;

        CasoLog.inicio("CASO 2 - OWNERS", "Busqueda de dueños por apellido");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("last_name buscado", LAST_NAME);

        List<OwnerDTO> owners = this.ownerService.findByLastName(LAST_NAME);

        CasoLog.seccion("REGISTROS OBTENIDOS");
        CasoLog.dato("cantidad encontrada", owners.size());
        for (int i = 0; i < owners.size(); i++) {
            CasoLog.dato("registro " + (i + 1),
                    owners.get(i).getId() + " | " + owners.get(i).getFirstName()
                            + " " + owners.get(i).getLastName() + " | " + owners.get(i).getCity());
        }

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("cantidad", SIZE_EXPECTED, owners.size());

        assertEquals(SIZE_EXPECTED, owners.size());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 2: buscar dueños por ciudad")
    public void testFindOwnerByCity() {

        final String CITY = "Monona";
        final int SIZE_EXPECTED = 2;

        CasoLog.inicio("CASO 2 - OWNERS", "Busqueda de dueños por ciudad");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("city buscada", CITY);

        List<OwnerDTO> owners = this.ownerService.findByCity(CITY);

        CasoLog.seccion("REGISTROS OBTENIDOS");
        CasoLog.dato("cantidad encontrada", owners.size());
        for (int i = 0; i < owners.size(); i++) {
            CasoLog.dato("registro " + (i + 1),
                    owners.get(i).getId() + " | " + owners.get(i).getFirstName()
                            + " " + owners.get(i).getLastName() + " | " + owners.get(i).getCity());
        }

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("cantidad", SIZE_EXPECTED, owners.size());

        assertEquals(SIZE_EXPECTED, owners.size());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 2: crear dueño")
    public void testCreateOwner() {

        final String FIRST_NAME = "Jose";
        final String LAST_NAME = "Paredes";
        final String ADDRESS = "Av. Cascanueces 2221";
        final String CITY = "Lima";
        final String TELEPHONE = "987654321";

        CasoLog.inicio("CASO 2 - OWNERS", "Creacion de un dueño (INSERT)");
        CasoLog.seccion("DATOS ENVIADOS AL SERVICIO");
        CasoLog.dato("first_name", FIRST_NAME);
        CasoLog.dato("last_name", LAST_NAME);
        CasoLog.dato("address", ADDRESS);
        CasoLog.dato("city", CITY);
        CasoLog.dato("telephone", TELEPHONE);

        OwnerDTO ownerDTO = OwnerDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .address(ADDRESS)
                .city(CITY)
                .telephone(TELEPHONE)
                .build();

        OwnerDTO newOwnerDTO = this.ownerService.create(ownerDTO);

        CasoLog.seccion("REGISTRO GUARDADO EN LA BASE DE DATOS");
        CasoLog.dato("id generado", newOwnerDTO.getId());
        CasoLog.dato("first_name", newOwnerDTO.getFirstName());
        CasoLog.dato("last_name", newOwnerDTO.getLastName());
        CasoLog.dato("address", newOwnerDTO.getAddress());
        CasoLog.dato("city", newOwnerDTO.getCity());
        CasoLog.dato("telephone", newOwnerDTO.getTelephone());

        CasoLog.seccion("VERIFICACION");
        CasoLog.ok("la base de datos genero el id " + newOwnerDTO.getId());
        CasoLog.verificacion("first_name", FIRST_NAME, newOwnerDTO.getFirstName());
        CasoLog.verificacion("last_name", LAST_NAME, newOwnerDTO.getLastName());
        CasoLog.verificacion("city", CITY, newOwnerDTO.getCity());
        CasoLog.verificacion("telephone", TELEPHONE, newOwnerDTO.getTelephone());

        assertNotNull(newOwnerDTO.getId());
        assertEquals(FIRST_NAME, newOwnerDTO.getFirstName());
        assertEquals(LAST_NAME, newOwnerDTO.getLastName());
        assertEquals(CITY, newOwnerDTO.getCity());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 2: actualizar dueño")
    public void testUpdateOwner() {

        final String UP_FIRST_NAME = "Carlos Alberto";
        final String UP_CITY = "Cusco";
        final String UP_TELEPHONE = "999888777";

        CasoLog.inicio("CASO 2 - OWNERS", "Actualizacion de un dueño (UPDATE)");
        CasoLog.seccion("DATOS PARA CREAR EL REGISTRO");
        CasoLog.dato("first_name", "Carlos");
        CasoLog.dato("last_name", "Ramirez");
        CasoLog.dato("address", "Calle Mercaderes 123");
        CasoLog.dato("city", "Arequipa");
        CasoLog.dato("telephone", "959595959");

        OwnerDTO ownerDTO = OwnerDTO.builder()
                .firstName("Carlos")
                .lastName("Ramirez")
                .address("Calle Mercaderes 123")
                .city("Arequipa")
                .telephone("959595959")
                .build();

        OwnerDTO ownerDTOCreated = this.ownerService.create(ownerDTO);

        CasoLog.seccion("REGISTRO CREADO");
        CasoLog.dato("id", ownerDTOCreated.getId());
        CasoLog.dato("first_name anterior", ownerDTOCreated.getFirstName());
        CasoLog.dato("city anterior", ownerDTOCreated.getCity());
        CasoLog.dato("telephone anterior", ownerDTOCreated.getTelephone());

        Integer createdId = ownerDTOCreated.getId();
        ownerDTOCreated.setFirstName(UP_FIRST_NAME);
        ownerDTOCreated.setCity(UP_CITY);
        ownerDTOCreated.setTelephone(UP_TELEPHONE);

        CasoLog.seccion("DATOS ENVIADOS EN LA ACTUALIZACION");
        CasoLog.dato("id", createdId);
        CasoLog.dato("first_name nuevo", UP_FIRST_NAME);
        CasoLog.dato("city nueva", UP_CITY);
        CasoLog.dato("telephone nuevo", UP_TELEPHONE);

        OwnerDTO upgradeOwnerDTO = this.ownerService.update(ownerDTOCreated);

        CasoLog.seccion("REGISTRO ACTUALIZADO");
        CasoLog.dato("id", upgradeOwnerDTO.getId());
        CasoLog.dato("first_name", upgradeOwnerDTO.getFirstName());
        CasoLog.dato("city", upgradeOwnerDTO.getCity());
        CasoLog.dato("telephone", upgradeOwnerDTO.getTelephone());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("id", createdId, upgradeOwnerDTO.getId());
        CasoLog.verificacion("first_name", UP_FIRST_NAME, upgradeOwnerDTO.getFirstName());
        CasoLog.verificacion("city", UP_CITY, upgradeOwnerDTO.getCity());
        CasoLog.verificacion("telephone", UP_TELEPHONE, upgradeOwnerDTO.getTelephone());

        assertEquals(createdId, upgradeOwnerDTO.getId());
        assertEquals(UP_FIRST_NAME, upgradeOwnerDTO.getFirstName());
        assertEquals(UP_CITY, upgradeOwnerDTO.getCity());
        assertEquals(UP_TELEPHONE, upgradeOwnerDTO.getTelephone());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 2: eliminar dueño")
    public void testDeleteOwner() throws OwnerNotFoundException {

        CasoLog.inicio("CASO 2 - OWNERS", "Eliminacion de un dueño (DELETE)");
        CasoLog.seccion("DATOS PARA CREAR EL REGISTRO A ELIMINAR");
        CasoLog.dato("first_name", "Lucia");
        CasoLog.dato("last_name", "Torres");
        CasoLog.dato("address", "Jr. Union 456");
        CasoLog.dato("city", "Trujillo");
        CasoLog.dato("telephone", "912345678");

        OwnerDTO ownerDTO = OwnerDTO.builder()
                .firstName("Lucia")
                .lastName("Torres")
                .address("Jr. Union 456")
                .city("Trujillo")
                .telephone("912345678")
                .build();

        OwnerDTO newOwnerDTO = this.ownerService.create(ownerDTO);

        CasoLog.seccion("REGISTRO CREADO");
        CasoLog.dato("id", newOwnerDTO.getId());
        CasoLog.dato("nombre completo", newOwnerDTO.getFirstName() + " " + newOwnerDTO.getLastName());

        this.ownerService.delete(newOwnerDTO.getId());

        CasoLog.seccion("VERIFICACION DESPUES DEL DELETE");
        CasoLog.dato("id eliminado", newOwnerDTO.getId());

        OwnerNotFoundException exception = assertThrows(OwnerNotFoundException.class,
                () -> this.ownerService.findById(newOwnerDTO.getId()));

        CasoLog.dato("excepcion lanzada", exception.getClass().getSimpleName());
        CasoLog.dato("mensaje", exception.getMessage());
        CasoLog.verificacion("tipo de excepcion", "OwnerNotFoundException",
                exception.getClass().getSimpleName());
        CasoLog.ok("el dueño eliminado ya no se puede recuperar");

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 2: dueño inexistente lanza excepcion")
    public void testFindOwnerNotFound() {

        final Integer ID = 9999;

        CasoLog.inicio("CASO 2 - OWNERS", "Busqueda de un dueño que no existe");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("id solicitado", ID);

        OwnerNotFoundException exception = assertThrows(OwnerNotFoundException.class,
                () -> this.ownerService.findById(ID));

        CasoLog.seccion("RESULTADO DE LA BUSQUEDA");
        CasoLog.dato("excepcion lanzada", exception.getClass().getSimpleName());
        CasoLog.dato("mensaje", exception.getMessage());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("tipo de excepcion", "OwnerNotFoundException",
                exception.getClass().getSimpleName());

        CasoLog.fin();
    }
}
