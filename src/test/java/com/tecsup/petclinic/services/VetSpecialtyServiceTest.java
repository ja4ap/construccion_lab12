package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.util.CasoLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// CASO 6 - Pruebas unitarias de la tabla vet_specialties
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@ActiveProfiles("h2")
public class VetSpecialtyServiceTest {

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    @Test
    @DisplayName("CASO 6: asignar especialidad a un veterinario")
    public void testAssignSpecialtyToVet() throws VetNotFoundException, SpecialtyNotFoundException,
            VetSpecialtyNotFoundException {

        final Integer VET_ID = 1;
        final Integer SPECIALTY_ID = 3;
        final LocalDate CERTIFICATION_DATE = LocalDate.of(2020, 5, 18);
        final Integer YEARS_EXPERIENCE = 6;

        CasoLog.inicio("CASO 6 - VET_SPECIALTIES", "Asignacion de una especialidad a un veterinario (INSERT)");
        CasoLog.seccion("DATOS ENVIADOS AL SERVICIO");
        CasoLog.dato("vet_id", VET_ID);
        CasoLog.dato("specialty_id", SPECIALTY_ID);
        CasoLog.dato("certification_date", CERTIFICATION_DATE);
        CasoLog.dato("years_experience", YEARS_EXPERIENCE);
        CasoLog.dato("is_primary", false);
        CasoLog.dato("notes", "Asignacion de prueba");

        VetSpecialtyDTO vetSpecialtyDTO = VetSpecialtyDTO.builder()
                .vetId(VET_ID)
                .specialtyId(SPECIALTY_ID)
                .certificationDate(CERTIFICATION_DATE)
                .yearsExperience(YEARS_EXPERIENCE)
                .isPrimary(false)
                .notes("Asignacion de prueba")
                .build();

        VetSpecialtyDTO newVetSpecialtyDTO = this.vetSpecialtyService.assign(vetSpecialtyDTO);

        CasoLog.seccion("REGISTRO GUARDADO EN LA BASE DE DATOS");
        CasoLog.dato("vet_id", newVetSpecialtyDTO.getVetId());
        CasoLog.dato("specialty_id", newVetSpecialtyDTO.getSpecialtyId());
        CasoLog.dato("certification_date", newVetSpecialtyDTO.getCertificationDate());
        CasoLog.dato("years_experience", newVetSpecialtyDTO.getYearsExperience());
        CasoLog.dato("is_primary", newVetSpecialtyDTO.getIsPrimary());
        CasoLog.dato("notes", newVetSpecialtyDTO.getNotes());

        VetSpecialtyDTO found = this.vetSpecialtyService.findById(VET_ID, SPECIALTY_ID);

        CasoLog.seccion("RELACION RECUPERADA CON LA CLAVE COMPUESTA (vet_id, specialty_id)");
        CasoLog.dato("vet_id", found.getVetId());
        CasoLog.dato("specialty_id", found.getSpecialtyId());
        CasoLog.dato("years_experience", found.getYearsExperience());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("vet_id", VET_ID, newVetSpecialtyDTO.getVetId());
        CasoLog.verificacion("specialty_id", SPECIALTY_ID, newVetSpecialtyDTO.getSpecialtyId());
        CasoLog.verificacion("certification_date", CERTIFICATION_DATE, newVetSpecialtyDTO.getCertificationDate());
        CasoLog.verificacion("years_experience", YEARS_EXPERIENCE, newVetSpecialtyDTO.getYearsExperience());
        CasoLog.ok("la relacion se recupera con la clave compuesta");

        assertNotNull(newVetSpecialtyDTO);
        assertEquals(VET_ID, newVetSpecialtyDTO.getVetId());
        assertEquals(SPECIALTY_ID, newVetSpecialtyDTO.getSpecialtyId());
        assertEquals(CERTIFICATION_DATE, newVetSpecialtyDTO.getCertificationDate());
        assertEquals(YEARS_EXPERIENCE, newVetSpecialtyDTO.getYearsExperience());
        assertEquals(YEARS_EXPERIENCE, found.getYearsExperience());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 6: actualizar datos de la asignacion")
    public void testUpdateVetSpecialty() throws VetNotFoundException, SpecialtyNotFoundException,
            VetSpecialtyNotFoundException {

        final Integer VET_ID = 1;
        final Integer SPECIALTY_ID = 2;
        final Integer UP_YEARS_EXPERIENCE = 12;

        CasoLog.inicio("CASO 6 - VET_SPECIALTIES", "Actualizacion de una relacion veterinario-especialidad (UPDATE)");
        CasoLog.seccion("DATOS PARA CREAR LA RELACION");
        CasoLog.dato("vet_id", VET_ID);
        CasoLog.dato("specialty_id", SPECIALTY_ID);
        CasoLog.dato("certification_date", "2019-03-10");
        CasoLog.dato("years_experience", 4);
        CasoLog.dato("is_primary", false);

        VetSpecialtyDTO vetSpecialtyDTO = VetSpecialtyDTO.builder()
                .vetId(VET_ID)
                .specialtyId(SPECIALTY_ID)
                .certificationDate(LocalDate.of(2019, 3, 10))
                .yearsExperience(4)
                .isPrimary(false)
                .notes("Registro inicial")
                .build();

        this.vetSpecialtyService.assign(vetSpecialtyDTO);

        CasoLog.seccion("DATOS ENVIADOS EN LA ACTUALIZACION");
        CasoLog.dato("vet_id", VET_ID);
        CasoLog.dato("specialty_id", SPECIALTY_ID);
        CasoLog.dato("years_experience nuevo", UP_YEARS_EXPERIENCE);
        CasoLog.dato("is_primary nuevo", true);
        CasoLog.dato("notes nuevas", "Registro actualizado");

        vetSpecialtyDTO.setYearsExperience(UP_YEARS_EXPERIENCE);
        vetSpecialtyDTO.setIsPrimary(true);
        vetSpecialtyDTO.setNotes("Registro actualizado");

        VetSpecialtyDTO vetSpecialtyDTOUpdated = this.vetSpecialtyService.update(vetSpecialtyDTO);

        CasoLog.seccion("REGISTRO ACTUALIZADO");
        CasoLog.dato("vet_id", vetSpecialtyDTOUpdated.getVetId());
        CasoLog.dato("specialty_id", vetSpecialtyDTOUpdated.getSpecialtyId());
        CasoLog.dato("certification_date", vetSpecialtyDTOUpdated.getCertificationDate());
        CasoLog.dato("years_experience", vetSpecialtyDTOUpdated.getYearsExperience());
        CasoLog.dato("is_primary", vetSpecialtyDTOUpdated.getIsPrimary());
        CasoLog.dato("notes", vetSpecialtyDTOUpdated.getNotes());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("years_experience", UP_YEARS_EXPERIENCE,
                vetSpecialtyDTOUpdated.getYearsExperience());
        CasoLog.verificacion("is_primary", true, vetSpecialtyDTOUpdated.getIsPrimary());
        CasoLog.verificacion("notes", "Registro actualizado", vetSpecialtyDTOUpdated.getNotes());

        assertEquals(UP_YEARS_EXPERIENCE, vetSpecialtyDTOUpdated.getYearsExperience());
        assertTrue(vetSpecialtyDTOUpdated.getIsPrimary());
        assertEquals("Registro actualizado", vetSpecialtyDTOUpdated.getNotes());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 6: buscar especialidades de un veterinario")
    public void testFindSpecialtiesByVet() throws VetNotFoundException {

        final Integer VET_ID = 3;

        CasoLog.inicio("CASO 6 - VET_SPECIALTIES", "Busqueda de las especialidades de un veterinario");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("vet_id buscado", VET_ID);

        List<SpecialtyDTO> specialties = this.vetSpecialtyService.findSpecialtiesByVetId(VET_ID);

        CasoLog.seccion("ESPECIALIDADES OBTENIDAS");
        CasoLog.dato("cantidad", specialties.size());
        for (int i = 0; i < specialties.size(); i++) {
            CasoLog.dato("especialidad " + (i + 1),
                    specialties.get(i).getId() + " | " + specialties.get(i).getName()
                            + " | " + specialties.get(i).getOffice());
        }

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("cantidad", 2, specialties.size());
        CasoLog.ok("la lista contiene surgery y dentistry");

        assertEquals(2, specialties.size());
        assertTrue(specialties.stream().anyMatch(specialty -> "surgery".equals(specialty.getName())));
        assertTrue(specialties.stream().anyMatch(specialty -> "dentistry".equals(specialty.getName())));

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 6: buscar veterinarios de una especialidad")
    public void testFindVetsBySpecialty() throws SpecialtyNotFoundException {

        final Integer SPECIALTY_ID = 1;

        CasoLog.inicio("CASO 6 - VET_SPECIALTIES", "Busqueda de los veterinarios de una especialidad");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("specialty_id buscado", SPECIALTY_ID);

        List<VetDTO> vets = this.vetSpecialtyService.findVetsBySpecialtyId(SPECIALTY_ID);

        CasoLog.seccion("VETERINARIOS OBTENIDOS");
        CasoLog.dato("cantidad", vets.size());
        for (int i = 0; i < vets.size(); i++) {
            CasoLog.dato("veterinario " + (i + 1),
                    vets.get(i).getId() + " | " + vets.get(i).getFirstName()
                            + " " + vets.get(i).getLastName() + " | " + vets.get(i).getEmail());
        }

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("cantidad", 2, vets.size());
        CasoLog.ok("la lista contiene a Leary y Stevens");

        assertEquals(2, vets.size());
        assertTrue(vets.stream().anyMatch(vet -> "Leary".equals(vet.getLastName())));
        assertTrue(vets.stream().anyMatch(vet -> "Stevens".equals(vet.getLastName())));

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 6: eliminar la relacion veterinario-especialidad")
    public void testRemoveSpecialtyFromVet() throws VetNotFoundException, SpecialtyNotFoundException,
            VetSpecialtyNotFoundException {

        final Integer VET_ID = 6;
        final Integer SPECIALTY_ID = 3;

        CasoLog.inicio("CASO 6 - VET_SPECIALTIES", "Eliminacion de una relacion veterinario-especialidad (DELETE)");
        CasoLog.seccion("RELACION A CREAR Y LUEGO ELIMINAR");
        CasoLog.dato("vet_id", VET_ID);
        CasoLog.dato("specialty_id", SPECIALTY_ID);
        CasoLog.dato("certification_date", "2021-08-01");
        CasoLog.dato("years_experience", 2);

        VetSpecialtyDTO vetSpecialtyDTO = VetSpecialtyDTO.builder()
                .vetId(VET_ID)
                .specialtyId(SPECIALTY_ID)
                .certificationDate(LocalDate.of(2021, 8, 1))
                .yearsExperience(2)
                .isPrimary(false)
                .notes("Registro a eliminar")
                .build();

        this.vetSpecialtyService.assign(vetSpecialtyDTO);

        CasoLog.seccion("RELACION REGISTRADA");
        CasoLog.dato("vet_id", VET_ID);
        CasoLog.dato("specialty_id", SPECIALTY_ID);

        this.vetSpecialtyService.remove(VET_ID, SPECIALTY_ID);

        CasoLog.seccion("VERIFICACION DESPUES DEL DELETE");
        CasoLog.dato("clave eliminada", "(" + VET_ID + ", " + SPECIALTY_ID + ")");

        VetSpecialtyNotFoundException exception = assertThrows(VetSpecialtyNotFoundException.class,
                () -> this.vetSpecialtyService.findById(VET_ID, SPECIALTY_ID));

        CasoLog.dato("excepcion lanzada", exception.getClass().getSimpleName());
        CasoLog.dato("mensaje", exception.getMessage());
        CasoLog.verificacion("tipo de excepcion", "VetSpecialtyNotFoundException",
                exception.getClass().getSimpleName());
        CasoLog.ok("la relacion eliminada ya no se puede recuperar");

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 6: relacion inexistente lanza excepcion")
    public void testRemoveVetSpecialty_NotFound() {

        final Integer VET_ID = 9999;
        final Integer SPECIALTY_ID = 9999;

        CasoLog.inicio("CASO 6 - VET_SPECIALTIES", "Eliminacion de una relacion que no existe");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("vet_id", VET_ID);
        CasoLog.dato("specialty_id", SPECIALTY_ID);

        VetSpecialtyNotFoundException exception = assertThrows(VetSpecialtyNotFoundException.class,
                () -> this.vetSpecialtyService.remove(VET_ID, SPECIALTY_ID));

        CasoLog.seccion("RESULTADO DE LA OPERACION");
        CasoLog.dato("excepcion lanzada", exception.getClass().getSimpleName());
        CasoLog.dato("mensaje", exception.getMessage());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("tipo de excepcion", "VetSpecialtyNotFoundException",
                exception.getClass().getSimpleName());

        CasoLog.fin();
    }

    @Test
    @DisplayName("CASO 6: buscar relacion por veterinario y especialidad")
    public void testFindVetSpecialtyById() throws VetSpecialtyNotFoundException {

        final Integer VET_ID = 2;
        final Integer SPECIALTY_ID = 1;

        CasoLog.inicio("CASO 6 - VET_SPECIALTIES", "Busqueda de una relacion por su clave compuesta");
        CasoLog.seccion("DATO DE ENTRADA");
        CasoLog.dato("vet_id", VET_ID);
        CasoLog.dato("specialty_id", SPECIALTY_ID);

        VetSpecialtyDTO vetSpecialty = this.vetSpecialtyService.findById(VET_ID, SPECIALTY_ID);

        CasoLog.seccion("RELACION OBTENIDA");
        CasoLog.dato("vet_id", vetSpecialty.getVetId());
        CasoLog.dato("specialty_id", vetSpecialty.getSpecialtyId());
        CasoLog.dato("certification_date", vetSpecialty.getCertificationDate());
        CasoLog.dato("years_experience", vetSpecialty.getYearsExperience());
        CasoLog.dato("is_primary", vetSpecialty.getIsPrimary());
        CasoLog.dato("notes", vetSpecialty.getNotes());

        CasoLog.seccion("VERIFICACION");
        CasoLog.verificacion("vet_id", VET_ID, vetSpecialty.getVetId());
        CasoLog.verificacion("specialty_id", SPECIALTY_ID, vetSpecialty.getSpecialtyId());
        CasoLog.verificacion("years_experience", 8, vetSpecialty.getYearsExperience());
        CasoLog.verificacion("is_primary", true, vetSpecialty.getIsPrimary());

        assertNotNull(vetSpecialty);
        assertEquals(VET_ID, vetSpecialty.getVetId());
        assertEquals(SPECIALTY_ID, vetSpecialty.getSpecialtyId());
        assertEquals(8, vetSpecialty.getYearsExperience());
        assertTrue(vetSpecialty.getIsPrimary());

        CasoLog.fin();
    }
}
