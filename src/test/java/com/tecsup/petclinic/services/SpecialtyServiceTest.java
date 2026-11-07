package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
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

// CASO 3 - Pruebas unitarias de la tabla specialties
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@ActiveProfiles("h2")
public class SpecialtyServiceTest {

    @Autowired
    private SpecialtyService specialtyService;

    @Test
    @DisplayName("CASO 3: buscar especialidad por id")
    public void testFindSpecialtyById() throws SpecialtyNotFoundException {

        final Integer ID = 1;
        final String NAME_EXPECTED = "radiology";
        final String OFFICE_EXPECTED = "Farewell";

        CasoLog.inicio("CASO 3 - SPECIALTIES", "Busqueda de una especialidad por id");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("id solicitado", ID);

        SpecialtyDTO specialty = this.specialtyService.findById(ID);

        CasoLog.seccion("REGISTRO OBTENIDO");
        CasoLog.dato("id", specialty.getId());
        CasoLog.dato("name", specialty.getName());
        CasoLog.dato("office", specialty.getOffice());
        CasoLog.dato("h_open", specialty.getOpenHour());
        CasoLog.dato("h_close", specialty.getCloseHour());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("id", ID, specialty.getId());
        CasoLog.verificacion("name", NAME_EXPECTED, specialty.getName());
        CasoLog.verificacion("office", OFFICE_EXPECTED, specialty.getOffice());
        CasoLog.verificacion("h_open", 8, specialty.getOpenHour());
        CasoLog.verificacion("h_close", 18, specialty.getCloseHour());

        assertNotNull(specialty);
        assertEquals(ID, specialty.getId());
        assertEquals(NAME_EXPECTED, specialty.getName());
        assertEquals(OFFICE_EXPECTED, specialty.getOffice());
        assertEquals(8, specialty.getOpenHour());
        assertEquals(18, specialty.getCloseHour());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 3: buscar especialidad por nombre")
    public void testFindSpecialtyByName() {

        final String NAME = "surgery";
        final String OFFICE_EXPECTED = "Maryland";

        CasoLog.inicio("CASO 3 - SPECIALTIES", "Busqueda de especialidades por nombre");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("name buscado", NAME);

        List<SpecialtyDTO> specialties = this.specialtyService.findByName(NAME);

        CasoLog.seccion("REGISTROS OBTENIDOS");
        CasoLog.dato("cantidad encontrada", specialties.size());
        for (int i = 0; i < specialties.size(); i++) {
            CasoLog.dato("registro " + (i + 1),
                    specialties.get(i).getId() + " | " + specialties.get(i).getName()
                            + " | " + specialties.get(i).getOffice()
                            + " | " + specialties.get(i).getOpenHour()
                            + "-" + specialties.get(i).getCloseHour());
        }

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("cantidad", 1, specialties.size());
        CasoLog.verificacion("office", OFFICE_EXPECTED,
                specialties.isEmpty() ? null : specialties.get(0).getOffice());

        assertEquals(1, specialties.size());
        assertEquals(OFFICE_EXPECTED, specialties.get(0).getOffice());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 3: buscar especialidad por consultorio")
    public void testFindSpecialtyByOffice() {

        final String OFFICE = "Terranova";
        final String NAME_EXPECTED = "dentistry";

        CasoLog.inicio("CASO 3 - SPECIALTIES", "Busqueda de especialidades por consultorio");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("office buscado", OFFICE);

        List<SpecialtyDTO> specialties = this.specialtyService.findByOffice(OFFICE);

        CasoLog.seccion("REGISTROS OBTENIDOS");
        CasoLog.dato("cantidad encontrada", specialties.size());
        for (int i = 0; i < specialties.size(); i++) {
            CasoLog.dato("registro " + (i + 1),
                    specialties.get(i).getId() + " | " + specialties.get(i).getName()
                            + " | " + specialties.get(i).getOffice());
        }

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("cantidad", 1, specialties.size());
        CasoLog.verificacion("name", NAME_EXPECTED,
                specialties.isEmpty() ? null : specialties.get(0).getName());

        assertEquals(1, specialties.size());
        assertEquals(NAME_EXPECTED, specialties.get(0).getName());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 3: crear especialidad")
    public void testCreateSpecialty() {

        final String NAME = "cardiology";
        final String OFFICE = "Lima";
        final Integer OPEN_HOUR = 7;
        final Integer CLOSE_HOUR = 15;

        CasoLog.inicio("CASO 3 - SPECIALTIES", "Creacion de una especialidad (INSERT)");
        CasoLog.seccion("DATOS ENVIADOS AL SERVICIO");
        CasoLog.dato("name", NAME);
        CasoLog.dato("office", OFFICE);
        CasoLog.dato("h_open", OPEN_HOUR);
        CasoLog.dato("h_close", CLOSE_HOUR);

        SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
                .name(NAME)
                .office(OFFICE)
                .openHour(OPEN_HOUR)
                .closeHour(CLOSE_HOUR)
                .build();

        SpecialtyDTO newSpecialtyDTO = this.specialtyService.create(specialtyDTO);

        CasoLog.seccion("REGISTRO GUARDADO EN LA BASE DE DATOS");
        CasoLog.dato("id generado", newSpecialtyDTO.getId());
        CasoLog.dato("name", newSpecialtyDTO.getName());
        CasoLog.dato("office", newSpecialtyDTO.getOffice());
        CasoLog.dato("h_open", newSpecialtyDTO.getOpenHour());
        CasoLog.dato("h_close", newSpecialtyDTO.getCloseHour());

        CasoLog.seccion("VERIFICACION");
        CasoLog.ok("la base de datos genero el id " + newSpecialtyDTO.getId());
        CasoLog.verificacion("name", NAME, newSpecialtyDTO.getName());
        CasoLog.verificacion("office", OFFICE, newSpecialtyDTO.getOffice());
        CasoLog.verificacion("h_open", OPEN_HOUR, newSpecialtyDTO.getOpenHour());
        CasoLog.verificacion("h_close", CLOSE_HOUR, newSpecialtyDTO.getCloseHour());

        assertNotNull(newSpecialtyDTO.getId());
        assertEquals(NAME, newSpecialtyDTO.getName());
        assertEquals(OFFICE, newSpecialtyDTO.getOffice());
        assertEquals(OPEN_HOUR, newSpecialtyDTO.getOpenHour());
        assertEquals(CLOSE_HOUR, newSpecialtyDTO.getCloseHour());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 3: actualizar especialidad")
    public void testUpdateSpecialty() {

        final String UP_OFFICE = "Arequipa";
        final Integer UP_OPEN_HOUR = 10;
        final Integer UP_CLOSE_HOUR = 20;

        CasoLog.inicio("CASO 3 - SPECIALTIES", "Actualizacion de una especialidad (UPDATE)");
        CasoLog.seccion("DATOS PARA CREAR EL REGISTRO");
        CasoLog.dato("name", "dermatology");
        CasoLog.dato("office", "Trujillo");
        CasoLog.dato("h_open", 8);
        CasoLog.dato("h_close", 14);

        SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
                .name("dermatology")
                .office("Trujillo")
                .openHour(8)
                .closeHour(14)
                .build();

        SpecialtyDTO specialtyDTOCreated = this.specialtyService.create(specialtyDTO);

        CasoLog.seccion("REGISTRO CREADO");
        CasoLog.dato("id", specialtyDTOCreated.getId());
        CasoLog.dato("office anterior", specialtyDTOCreated.getOffice());
        CasoLog.dato("h_open anterior", specialtyDTOCreated.getOpenHour());
        CasoLog.dato("h_close anterior", specialtyDTOCreated.getCloseHour());

        Integer createdId = specialtyDTOCreated.getId();
        specialtyDTOCreated.setOffice(UP_OFFICE);
        specialtyDTOCreated.setOpenHour(UP_OPEN_HOUR);
        specialtyDTOCreated.setCloseHour(UP_CLOSE_HOUR);

        CasoLog.seccion("DATOS ENVIADOS EN LA ACTUALIZACION");
        CasoLog.dato("id", createdId);
        CasoLog.dato("office nuevo", UP_OFFICE);
        CasoLog.dato("h_open nuevo", UP_OPEN_HOUR);
        CasoLog.dato("h_close nuevo", UP_CLOSE_HOUR);

        SpecialtyDTO specialtyDTOUpdated = this.specialtyService.update(specialtyDTOCreated);

        CasoLog.seccion("REGISTRO ACTUALIZADO");
        CasoLog.dato("id", specialtyDTOUpdated.getId());
        CasoLog.dato("name", specialtyDTOUpdated.getName());
        CasoLog.dato("office", specialtyDTOUpdated.getOffice());
        CasoLog.dato("h_open", specialtyDTOUpdated.getOpenHour());
        CasoLog.dato("h_close", specialtyDTOUpdated.getCloseHour());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("id", createdId, specialtyDTOUpdated.getId());
        CasoLog.verificacion("office", UP_OFFICE, specialtyDTOUpdated.getOffice());
        CasoLog.verificacion("h_open", UP_OPEN_HOUR, specialtyDTOUpdated.getOpenHour());
        CasoLog.verificacion("h_close", UP_CLOSE_HOUR, specialtyDTOUpdated.getCloseHour());

        assertEquals(createdId, specialtyDTOUpdated.getId());
        assertEquals(UP_OFFICE, specialtyDTOUpdated.getOffice());
        assertEquals(UP_OPEN_HOUR, specialtyDTOUpdated.getOpenHour());
        assertEquals(UP_CLOSE_HOUR, specialtyDTOUpdated.getCloseHour());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 3: eliminar especialidad")
    public void testDeleteSpecialty() throws SpecialtyNotFoundException {

        CasoLog.inicio("CASO 3 - SPECIALTIES", "Eliminacion de una especialidad (DELETE)");
        CasoLog.seccion("DATOS PARA CREAR EL REGISTRO A ELIMINAR");
        CasoLog.dato("name", "ophthalmology");
        CasoLog.dato("office", "Piura");
        CasoLog.dato("h_open", 9);
        CasoLog.dato("h_close", 17);

        SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
                .name("ophthalmology")
                .office("Piura")
                .openHour(9)
                .closeHour(17)
                .build();

        SpecialtyDTO newSpecialtyDTO = this.specialtyService.create(specialtyDTO);

        CasoLog.seccion("REGISTRO CREADO");
        CasoLog.dato("id", newSpecialtyDTO.getId());
        CasoLog.dato("name", newSpecialtyDTO.getName());

        this.specialtyService.delete(newSpecialtyDTO.getId());

        CasoLog.seccion("VERIFICACION DESPUES DEL DELETE");
        CasoLog.dato("id eliminado", newSpecialtyDTO.getId());

        SpecialtyNotFoundException exception = assertThrows(SpecialtyNotFoundException.class,
                () -> this.specialtyService.findById(newSpecialtyDTO.getId()));

        CasoLog.dato("excepcion lanzada", exception.getClass().getSimpleName());
        CasoLog.dato("mensaje", exception.getMessage());
        CasoLog.verificacion("tipo de excepcion", "SpecialtyNotFoundException",
                exception.getClass().getSimpleName());
        CasoLog.ok("la especialidad eliminada ya no se puede recuperar");

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 3: especialidad inexistente lanza excepcion")
    public void testFindSpecialtyById_NotFound() {

        final Integer ID = 9999;

        CasoLog.inicio("CASO 3 - SPECIALTIES", "Busqueda de una especialidad que no existe");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("id solicitado", ID);

        SpecialtyNotFoundException exception = assertThrows(SpecialtyNotFoundException.class,
                () -> this.specialtyService.findById(ID));

        CasoLog.seccion("RESULTADO DE LA BUSQUEDA");
        CasoLog.dato("excepcion lanzada", exception.getClass().getSimpleName());
        CasoLog.dato("mensaje", exception.getMessage());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("tipo de excepcion", "SpecialtyNotFoundException",
                exception.getClass().getSimpleName());

        CasoLog.fin();
    }
}
