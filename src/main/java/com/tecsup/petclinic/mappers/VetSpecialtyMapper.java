package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.VetSpecialty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VetSpecialtyMapper {

    VetSpecialty mapToEntity(VetSpecialtyDTO dto);

    VetSpecialtyDTO mapToDto(VetSpecialty entity);
}
