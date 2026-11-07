package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
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

// CASO 1 - Pruebas unitarias de la tabla vets
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@ActiveProfiles("h2")
public class VetServiceTest {

    @Autowired
    private VetService vetService;

    @Test
    @DisplayName("CASO 1: buscar veterinario por id")
    public void testFindVetById() throws VetNotFoundException {

        final Integer ID = 1;
        final String FIRST_NAME_EXPECTED = "James";
        final String LAST_NAME_EXPECTED = "Carter";

        CasoLog.inicio("CASO 1 - VETS", "Busqueda de un veterinario por id");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("id solicitado", ID);

        VetDTO vet = this.vetService.findById(ID);

        CasoLog.seccion("REGISTRO OBTENIDO");
        CasoLog.dato("id", vet.getId());
        CasoLog.dato("first_name", vet.getFirstName());
        CasoLog.dato("last_name", vet.getLastName());
        CasoLog.dato("email", vet.getEmail());
        CasoLog.dato("phone", vet.getPhone());
        CasoLog.dato("active", vet.getActive());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("id", ID, vet.getId());
        CasoLog.verificacion("first_name", FIRST_NAME_EXPECTED, vet.getFirstName());
        CasoLog.verificacion("last_name", LAST_NAME_EXPECTED, vet.getLastName());
        CasoLog.verificacion("active", true, vet.getActive());

        assertNotNull(vet);
        assertEquals(ID, vet.getId());
        assertEquals(FIRST_NAME_EXPECTED, vet.getFirstName());
        assertEquals(LAST_NAME_EXPECTED, vet.getLastName());
        assertTrue(vet.getActive());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 1: buscar veterinarios por apellido")
    public void testFindVetByLastName() {

        final String LAST_NAME = "Leary";
        final String FIRST_NAME_EXPECTED = "Helen";

        CasoLog.inicio("CASO 1 - VETS", "Busqueda de veterinarios por apellido");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("last_name buscado", LAST_NAME);

        List<VetDTO> vets = this.vetService.findByLastName(LAST_NAME);

        CasoLog.seccion("REGISTROS OBTENIDOS");
        CasoLog.dato("cantidad encontrada", vets.size());
        for (int i = 0; i < vets.size(); i++) {
            CasoLog.dato("registro " + (i + 1),
                    vets.get(i).getId() + " | " + vets.get(i).getFirstName()
                            + " " + vets.get(i).getLastName() + " | " + vets.get(i).getEmail());
        }

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("cantidad", 1, vets.size());
        CasoLog.verificacion("first_name", FIRST_NAME_EXPECTED,
                vets.isEmpty() ? null : vets.get(0).getFirstName());

        assertEquals(1, vets.size());
        assertEquals(FIRST_NAME_EXPECTED, vets.get(0).getFirstName());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 1: listar solo veterinarios activos")
    public void testFindActiveVets() {

        CasoLog.inicio("CASO 1 - VETS", "Listado de veterinarios con active = true");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("filtro", "active = true");

        List<VetDTO> vets = this.vetService.findActive();

        CasoLog.seccion("VETERINARIOS ACTIVOS OBTENIDOS");
        CasoLog.dato("cantidad", vets.size());
        vets.forEach(vet -> CasoLog.dato("veterinario",
                vet.getId() + " | " + vet.getFirstName() + " " + vet.getLastName() + " | active=" + vet.getActive()));

        CasoLog.seccion("VERIFICACION");
        CasoLog.ok("todos los registros tienen active = true");
        CasoLog.ok("la lista contiene a Carter (activo)");
        CasoLog.ok("la lista no contiene a Jenkins (inactivo)");

        assertTrue(vets.size() >= 5);
        assertTrue(vets.stream().allMatch(VetDTO::getActive));
        assertTrue(vets.stream().anyMatch(vet -> "Carter".equals(vet.getLastName())));
        assertFalse(vets.stream().anyMatch(vet -> "Jenkins".equals(vet.getLastName())));

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 1: crear veterinario")
    public void testCreateVet() {

        final String FIRST_NAME = "Jose";
        final String LAST_NAME = "Paredes";
        final String EMAIL = "jose.paredes@petclinic.com";
        final String PHONE = "987654321";

        CasoLog.inicio("CASO 1 - VETS", "Creacion de un veterinario (INSERT)");
        CasoLog.seccion("DATOS ENVIADOS AL SERVICIO");
        CasoLog.dato("first_name", FIRST_NAME);
        CasoLog.dato("last_name", LAST_NAME);
        CasoLog.dato("email", EMAIL);
        CasoLog.dato("phone", PHONE);
        CasoLog.dato("active", true);

        VetDTO vetDTO = VetDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .phone(PHONE)
                .active(true)
                .build();

        VetDTO newVetDTO = this.vetService.create(vetDTO);

        CasoLog.seccion("REGISTRO GUARDADO EN LA BASE DE DATOS");
        CasoLog.dato("id generado", newVetDTO.getId());
        CasoLog.dato("first_name", newVetDTO.getFirstName());
        CasoLog.dato("last_name", newVetDTO.getLastName());
        CasoLog.dato("email", newVetDTO.getEmail());
        CasoLog.dato("phone", newVetDTO.getPhone());
        CasoLog.dato("active", newVetDTO.getActive());

        CasoLog.seccion("VERIFICACION");
        CasoLog.ok("la base de datos genero el id " + newVetDTO.getId());
        CasoLog.verificacion("first_name", FIRST_NAME, newVetDTO.getFirstName());
        CasoLog.verificacion("last_name", LAST_NAME, newVetDTO.getLastName());
        CasoLog.verificacion("email", EMAIL, newVetDTO.getEmail());
        CasoLog.verificacion("phone", PHONE, newVetDTO.getPhone());

        assertNotNull(newVetDTO.getId());
        assertEquals(FIRST_NAME, newVetDTO.getFirstName());
        assertEquals(LAST_NAME, newVetDTO.getLastName());
        assertEquals(EMAIL, newVetDTO.getEmail());
        assertEquals(PHONE, newVetDTO.getPhone());
        assertTrue(newVetDTO.getActive());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 1: actualizar veterinario")
    public void testUpdateVet() {

        final String UP_FIRST_NAME = "Jose Antonio";
        final String UP_LAST_NAME = "Paredes Huaracha";
        final String UP_PHONE = "999888777";

        CasoLog.inicio("CASO 1 - VETS", "Actualizacion de un veterinario (UPDATE)");
        CasoLog.seccion("DATOS PARA CREAR EL REGISTRO");
        CasoLog.dato("first_name", "Carlos");
        CasoLog.dato("last_name", "Ramirez");
        CasoLog.dato("email", "carlos.ramirez@petclinic.com");
        CasoLog.dato("phone", "959595959");

        VetDTO vetDTO = VetDTO.builder()
                .firstName("Carlos")
                .lastName("Ramirez")
                .email("carlos.ramirez@petclinic.com")
                .phone("959595959")
                .active(true)
                .build();

        VetDTO vetDTOCreated = this.vetService.create(vetDTO);

        CasoLog.seccion("REGISTRO CREADO");
        CasoLog.dato("id", vetDTOCreated.getId());
        CasoLog.dato("first_name anterior", vetDTOCreated.getFirstName());
        CasoLog.dato("last_name anterior", vetDTOCreated.getLastName());
        CasoLog.dato("phone anterior", vetDTOCreated.getPhone());

        Integer createdId = vetDTOCreated.getId();
        vetDTOCreated.setFirstName(UP_FIRST_NAME);
        vetDTOCreated.setLastName(UP_LAST_NAME);
        vetDTOCreated.setPhone(UP_PHONE);

        CasoLog.seccion("DATOS ENVIADOS EN LA ACTUALIZACION");
        CasoLog.dato("id", createdId);
        CasoLog.dato("first_name nuevo", UP_FIRST_NAME);
        CasoLog.dato("last_name nuevo", UP_LAST_NAME);
        CasoLog.dato("phone nuevo", UP_PHONE);

        VetDTO vetDTOUpdated = this.vetService.update(vetDTOCreated);

        CasoLog.seccion("REGISTRO ACTUALIZADO");
        CasoLog.dato("id", vetDTOUpdated.getId());
        CasoLog.dato("first_name", vetDTOUpdated.getFirstName());
        CasoLog.dato("last_name", vetDTOUpdated.getLastName());
        CasoLog.dato("phone", vetDTOUpdated.getPhone());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("id", createdId, vetDTOUpdated.getId());
        CasoLog.verificacion("first_name", UP_FIRST_NAME, vetDTOUpdated.getFirstName());
        CasoLog.verificacion("last_name", UP_LAST_NAME, vetDTOUpdated.getLastName());
        CasoLog.verificacion("phone", UP_PHONE, vetDTOUpdated.getPhone());

        assertEquals(createdId, vetDTOUpdated.getId());
        assertEquals(UP_FIRST_NAME, vetDTOUpdated.getFirstName());
        assertEquals(UP_LAST_NAME, vetDTOUpdated.getLastName());
        assertEquals(UP_PHONE, vetDTOUpdated.getPhone());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 1: eliminar veterinario")
    public void testDeleteVet() throws VetNotFoundException {

        CasoLog.inicio("CASO 1 - VETS", "Eliminacion de un veterinario (DELETE)");
        CasoLog.seccion("DATOS PARA CREAR EL REGISTRO A ELIMINAR");
        CasoLog.dato("first_name", "Lucia");
        CasoLog.dato("last_name", "Torres");
        CasoLog.dato("email", "lucia.torres@petclinic.com");
        CasoLog.dato("phone", "912345678");

        VetDTO vetDTO = VetDTO.builder()
                .firstName("Lucia")
                .lastName("Torres")
                .email("lucia.torres@petclinic.com")
                .phone("912345678")
                .active(true)
                .build();

        VetDTO newVetDTO = this.vetService.create(vetDTO);

        CasoLog.seccion("REGISTRO CREADO");
        CasoLog.dato("id", newVetDTO.getId());
        CasoLog.dato("nombre completo", newVetDTO.getFirstName() + " " + newVetDTO.getLastName());

        this.vetService.delete(newVetDTO.getId());

        CasoLog.seccion("VERIFICACION DESPUES DEL DELETE");
        CasoLog.dato("id eliminado", newVetDTO.getId());

        VetNotFoundException exception = assertThrows(VetNotFoundException.class,
                () -> this.vetService.findById(newVetDTO.getId()));

        CasoLog.dato("excepcion lanzada", exception.getClass().getSimpleName());
        CasoLog.dato("mensaje", exception.getMessage());
        CasoLog.verificacion("tipo de excepcion", "VetNotFoundException",
                exception.getClass().getSimpleName());
        CasoLog.ok("el veterinario eliminado ya no se puede recuperar");

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 1: veterinario inexistente lanza excepcion")
    public void testFindVetById_NotFound() {

        final Integer ID = 9999;

        CasoLog.inicio("CASO 1 - VETS", "Busqueda de un veterinario que no existe");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("id solicitado", ID);

        VetNotFoundException exception = assertThrows(VetNotFoundException.class,
                () -> this.vetService.findById(ID));

        CasoLog.seccion("RESULTADO DE LA BUSQUEDA");
        CasoLog.dato("excepcion lanzada", exception.getClass().getSimpleName());
        CasoLog.dato("mensaje", exception.getMessage());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("tipo de excepcion", "VetNotFoundException",
                exception.getClass().getSimpleName());

        CasoLog.fin();
    }
}
