package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la tabla owners.
 *
 * @author Jose Paredes
 */
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

    // Buscar propietarios por apellido
    List<Owner> findByLastName(String lastName);

    // Buscar propietarios por ciudad
    List<Owner> findByCity(String city);

    @Override
    List<Owner> findAll();
}
